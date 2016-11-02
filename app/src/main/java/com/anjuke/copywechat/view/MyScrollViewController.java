package com.anjuke.copywechat.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

/**
 * desc:
 * <p>
 * Created by SishuiYe
 * E-mail: sishuiye@anjue.com
 * Created on  2016/9/18
 */

public class MyScrollViewController implements MyScrollView.ScrollViewController {
    private MyScrollView mMyScrollView;
    private Activity mActivity;
    private View mPicHolder;
    private View[] mExceptViews;


    int mScreenWidth;
    int mScreenHeight;
    int mLastHeight=0;

    ViewGroup.LayoutParams mContainer_Lp;
    private int mLastMotionY;
    private int mLastPosMotionY;

    private int mScrollViewHeight;
    private int mScrollViewContentHeight;

    private int mPicHolderOriginHeight;

    private int mResultVelocity;

    public static final int mAnimalShowTime = 300;
    public static final int mFlexHeight= 80;

    public MyScrollViewController(MyScrollView myScrollView, Activity activity, View picView, View[] views){
        mMyScrollView = myScrollView;
        mActivity = activity;
        mPicHolder = picView;
        mExceptViews = views;

        mMyScrollView.setmScrollViewController(this);

        WindowManager wm = activity.getWindowManager();
        mScreenWidth = wm.getDefaultDisplay().getWidth();
        mScreenHeight = wm.getDefaultDisplay().getHeight();

        mPicHolderOriginHeight = mPicHolder.getHeight();
        mPicHolderOriginHeight = mPicHolder.getMeasuredHeight();
        mScrollViewContentHeight = myScrollView.getScrollRange();
        mScrollViewHeight = myScrollView.getHeight();
    }

    @Override
    public boolean dispatchEvent(MotionEvent ev) {
        boolean result = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int)ev.getY();
                mLastPosMotionY = (int)ev.getY();
                mContainer_Lp = mPicHolder.getLayoutParams();
                mLastHeight = mContainer_Lp.height;
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastMotionY - y;
                mResultVelocity = mLastPosMotionY-y;
                mLastPosMotionY = y;

                //ScrollView可滑动
                if((picHolderAtOrigin()&&deltaY>0)||    //picHolder恢复往上滑
                        (picHolderAtOrigin()&&deltaY<0&&mMyScrollView.getScrollY()>0)){
                    mLastMotionY = (int)ev.getY();
                    result = false;
                    return false;
                }


                //对ScrollView的picHolder进行处理
                ViewGroup.LayoutParams lp = mContainer_Lp;
                lp.height = mLastHeight-deltaY;
                ConstantCls.DebugY("lp.height: "+lp.height+" mContainer_Lp.height:"+mContainer_Lp.height);
                if(lp.height>mMyScrollView.getHeight())
                    lp.height = mMyScrollView.getHeight();
                if(lp.height<=mPicHolderOriginHeight)
                    lp.height = mPicHolderOriginHeight;
                mPicHolder.setLayoutParams(lp);

                final int oldY = mLastMotionY;
                final int pulledToY = oldY + deltaY;
                ConstantCls.DebugY("getScrollRange: "+mMyScrollView.getScrollRange());
                ConstantCls.DebugY(" pulledToY: "+pulledToY+", "+deltaY+", "+oldY);
                return true;

            case MotionEvent.ACTION_UP:
                mLastMotionY = -1;
                result = true;
                startAnimation();
                break;
            default:
                break;
        }
        return result;
    }

    public boolean picHolderAtOrigin(){
        return mContainer_Lp.height==mPicHolderOriginHeight;
    }

    /**
     * ScrollView弹性效果
     */
    public void flexEffect(boolean up){
        if(up) {
            ObjectAnimator anim = ObjectAnimator//
                    .ofInt(mMyScrollView, "height", 0, mFlexHeight)//
                    .setDuration(100);
            anim.start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int curVal = (int) animation.getAnimatedValue();
                    mMyScrollView.scrollTo(0,curVal);
                    if(curVal==mFlexHeight){
                        flexEffect(false);
                    }
                }
            });
        }else{
            ObjectAnimator anim = ObjectAnimator//
                    .ofInt(mMyScrollView, "height", mFlexHeight, 0)//
                    .setDuration(100);
            anim.start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int curVal = (int) animation.getAnimatedValue();
                    mMyScrollView.scrollTo(0,curVal);

                }
            });
        }
    }

    public int computeShowTime(int terminal){
        float x = (mContainer_Lp.height-terminal)*1.0f/(mPicHolderOriginHeight-mScrollViewHeight);
        return Math.abs(Math.round(x*mAnimalShowTime));
    }

    /**
     * 启动动画
     */
    public void startAnimation(){
        if(mResultVelocity>0){
            if(mContainer_Lp.height>mPicHolderOriginHeight){
                picHolderAnimToOrigin(true);

            } else {
                //mMyScrollView.scrollTo(mMyScrollView.getScrollX(), mMyScrollView.getScrollRange());
            }
        } else {
            if(mMyScrollView.getScrollY()>0){
                //mMyScrollView.scrollTo(0,0);
            } else {
                picHolderAnimToOrigin(false);

            }
        }
    }

    private void picHolderAnimToOrigin(final boolean origin){
        ObjectAnimator anim = null;
        if(origin){
            anim = ObjectAnimator//
             .ofInt(mPicHolder, "height", mContainer_Lp.height, mPicHolderOriginHeight)//
                 .setDuration(computeShowTime(mPicHolderOriginHeight));
        }else{
            anim = ObjectAnimator//
             .ofInt(mPicHolder, "height", mContainer_Lp.height, mScrollViewHeight)//
                 .setDuration(computeShowTime(mScrollViewHeight));
        }

        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curVal = (int)animation.getAnimatedValue();
                mContainer_Lp.height = curVal;
                mPicHolder.setLayoutParams(mContainer_Lp);

                if(curVal==mPicHolderOriginHeight&&origin)
                    flexEffect(true);
            }
        });
    }
}
