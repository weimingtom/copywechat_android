package com.anjuke.copywechat.copywechat.viewpage;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/1
 */
public class MyViewPagerForWelcome extends ViewGroup {
    private GestureDetector detector;
    private Context ctx;
    private int firstDownX;
    private int currId = 0;
    private MyScrollerForWelcome myScrollerForWelcome;

    public MyViewPagerForWelcome(Context context, AttributeSet attributeSet){
         super(context, attributeSet);
        this.ctx = context;
        init();
    }

    private void init(){
        myScrollerForWelcome = new MyScrollerForWelcome(ctx);
        detector = new GestureDetector(ctx, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                scrollBy((int) v, 0);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b){
        for(int i=0; i<getChildCount(); i++){
            Log.i(ConstantCls.LOG_DEBUG_TAG, "the "+i+"th page!");
            View view = getChildAt(i);
            view.layout(i*getWidth(), 0, getWidth()+i*getWidth(),getHeight());
            if(i==6)
                ((ViewGroup) view).getChildAt(0).layout(getWidth()/5, getHeight()-getHeight()/3, getWidth()-getWidth()/5,getHeight()-getHeight()/6);
            Log.i(ConstantCls.LOG_DEBUG_TAG, "l t r b 是："+getWidth()+"  "+getHeight()+"   "+(getHeight()-getHeight()/3));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int nextId=0;
                if(event.getX()-firstDownX>getWidth()/2){
                    nextId = (currId-1)<=0?0:currId-1;
                }else if(firstDownX-event.getX()>getWidth()/2){
                    nextId = currId+1;
                }else{
                    nextId = currId;
                }
                moveToDest(nextId);
                break;
            default:
                break;
        }
        return true;
    }

    private void moveToDest(int nextId){
        currId = (nextId>=0)?nextId:0;
        currId = (nextId<=getChildCount()-1)?nextId:(getChildCount()-1);

        int distanceX = currId*getWidth()-getScrollX();
        myScrollerForWelcome.startScroll(getScrollX(), 0, distanceX, 0);
        invalidate();
    }

    @Override
    public void computeScroll(){
        if(myScrollerForWelcome.computeOffset()){
            int newX = (int) myScrollerForWelcome.getCurrX();
            Log.i(ConstantCls.LOG_DEBUG_TAG, "newX:"+newX);
            scrollTo(newX, 0);
            invalidate();
        }
    }
}
