package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anjuke.android.commonutils.SharedPreferencesHelper;
import com.anjuke.copywechat.copywechat.util.ConstantCls;
import com.anjuke.copywechat.copywechat.viewpage.ActivityWelcome2LastPageFragment;
import com.anjuke.copywechat.copywechat.viewpage.MyViewPagerForWelcome;

public class WelcomeActivity extends Activity {

    private boolean isFirstUse=false;   //是否初始使用

    //for isFisrtUse=true

    private MyViewPagerForWelcome myViewPagerForWelcome;

    private int[] ids = new int[] { R.drawable.a1, R.drawable.a1,
            R.drawable.a1, R.drawable.a1, R.drawable.a1, R.drawable.a1 };

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            if(chekLogined()) {
                intent.setClass(getApplication(), MainActivity.class);
                intent.setAction(Intent.ACTION_MAIN);
            }else {
                intent.setClass(getApplication(), LoginActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if(isFirstUse) {
            setContentView(R.layout.activity_welcome2);


            //for isFirst=true
            myViewPagerForWelcome = (MyViewPagerForWelcome) findViewById(R.id.id_myViewPagerForWelcome);

            for (int i = 0; i < ids.length; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setBackgroundResource(ids[i]);
                // 添加ImageView到自定义的viewgroup

                myViewPagerForWelcome.addView(imageView);
                Log.i(ConstantCls.LOG_DEBUG_TAG, i+"ssssss");
            }

            LayoutInflater inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View activity_welcome2_lastpage = inflater.inflate(R.layout.activity_welcome2_lastpage,null,false);
           // ActivityWelcome2LastPageFragment activityWelcome2LastPageFragment =ActivityWelcome2LastPageFragment.newInstance(null,null);
            //ViewGroup activity_welcome2_lastpage = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.activity_welcome2_lastpage,null);

            //禁止事件向后方传递
           /* activity_welcome2_lastpage.setOnTouchListener(
                    new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;
                        }
                    });*/
            activity_welcome2_lastpage.findViewById(R.id.page_welcome2_enterapp_btn).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            /*Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                            startActivity(intent);*/
                            mMainHandler.sendEmptyMessageDelayed(0, 500);
                            Log.i(ConstantCls.LOG_DEBUG_TAG,"按钮被点击");
                        }
                    });
            RelativeLayout.LayoutParams rrLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            myViewPagerForWelcome.addView(activity_welcome2_lastpage);
        }else {
            setContentView(R.layout.activity_welcome);

            mMainHandler.sendEmptyMessageDelayed(0, 500);
            //mMainHandler.sendEmptyMessage(0);
            /*Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);*/
        }
    }

    /**
     * 检查用户是否登录
     * @return
     */
    public boolean chekLogined(){
        SharedPreferencesHelper sp = SharedPreferencesHelper.getInstance(this);
        if(sp.getString(ConstantCls.LOGIN_CODE_STORED_IN_SHAREPREFERENCE).equals(""))
            return false;
        return true;
    }
}
