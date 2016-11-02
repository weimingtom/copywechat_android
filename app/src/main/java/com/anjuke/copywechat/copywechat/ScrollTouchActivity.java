package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjuke.copywechat.view.MyScrollView;
import com.anjuke.copywechat.view.MyScrollViewController;

public class ScrollTouchActivity extends Activity implements View.OnClickListener{

    MyScrollView scrollView;
    LinearLayout cotainer_ll;
    LinearLayout pic_cotainer_ll;
    Button addContent;

    MyScrollViewController myScrollViewController;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            myScrollViewController = new MyScrollViewController(scrollView, ScrollTouchActivity.this, pic_cotainer_ll, new View[0]);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);


        scrollView = (MyScrollView) findViewById(R.id.flag_scrollview);
        cotainer_ll = (LinearLayout)findViewById(R.id.container_ll);
        pic_cotainer_ll = (LinearLayout)findViewById(R.id.pic_container_ll);
        addContent = (Button)findViewById(R.id.add_content_btn);
        addContent.setOnClickListener(this);

        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_content_btn:
                TextView tv = new TextView(this);
                tv.setText("sssssssssssssssssssssssssssssss");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
                cotainer_ll.addView(tv, lp);
                Toast.makeText(this, "AmoutHeight是："+scrollView.getMaxScrollAmount()+"  高度是："+cotainer_ll.getHeight(),Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
