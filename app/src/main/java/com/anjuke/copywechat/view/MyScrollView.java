package com.anjuke.copywechat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * desc:
 * <p>
 * Created by SishuiYe
 * E-mail: sishuiye@anjue.com
 * Created on  2016/9/18
 */

public class MyScrollView extends ScrollView {

    private ScrollViewController mScrollViewController;

    public MyScrollView(Context context) {
        super(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(mScrollViewController!=null)
            return mScrollViewController.dispatchEvent(ev)?true:super.dispatchTouchEvent(ev);

        return super.dispatchTouchEvent(ev);
    }

    /**
     *
     * @return
     */
    public int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() -getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    public ScrollViewController getmScrollViewController() {
        return mScrollViewController;
    }

    public void setmScrollViewController(ScrollViewController mScrollViewController) {
        this.mScrollViewController = mScrollViewController;
    }


    public interface ScrollViewController{
        boolean dispatchEvent(MotionEvent ev);
    }
}
