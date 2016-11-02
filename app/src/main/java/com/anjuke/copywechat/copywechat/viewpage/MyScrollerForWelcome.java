package com.anjuke.copywechat.copywechat.viewpage;

import android.content.Context;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Scroller;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

/**
 * desc:
 * author: sishuiye
 * email: sishuiye@anjuke.com
 * date: 2016/4/1
 */
public class MyScrollerForWelcome {

    private int startX;
    private int startY;
    private int distanceX;
    private int distanceY;
    private long startTime;
    private boolean isFinish;
    private long currX;
    private long currY;
    private int duration = 500;

    public MyScrollerForWelcome(Context ctx){

    }

    public void startScroll(int startX, int startY, int distanceX, int distanceY){
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.startTime = SystemClock.uptimeMillis();
        this.isFinish = false;
    }

    public boolean computeOffset(){
        if(isFinish){
            return false;
        }

        long passTime = SystemClock.uptimeMillis()-startTime;
        Log.i(ConstantCls.LOG_DEBUG_TAG,"passTime:"+passTime);

        if(passTime<duration){
            currX = startX+distanceX*passTime/duration;
            currY = startY+distanceY*passTime/duration;
        }else{
            currX = startX+distanceX;
            currY = startY+distanceY;
            isFinish = true;
        }
        return true;
    }

    public long getCurrX(){
        return currX;
    }

    public long getCurrY(){
        return currY;
    }

    public void setCurrX(long currX) {
        this.currX = currX;
    }

    public void setCurrY(long currY) {
        this.currY = currY;
    }
}
