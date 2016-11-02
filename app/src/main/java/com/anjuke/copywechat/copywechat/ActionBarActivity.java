package com.anjuke.copywechat.copywechat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anjuke.copywechat.copywechat.util.UIUtils;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

public class ActionBarActivity extends AppCompatActivity {
    private LinearLayout titleLl;//标题栏
    private TextView titleTv;// 标题文字
    private ImageView arrowIv;// 标题右侧三角箭头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            Log.i(ConstantCls.LOG_DEBUG_TAG, "ActionBar is not null!");
            titleLl = new LinearLayout(this);
            titleLl.setOrientation(LinearLayout.HORIZONTAL);
            titleLl.setGravity(Gravity.CENTER);
            titleLl.setBackgroundColor(getResources().getColor(R.color.blue));

            titleTv = new TextView(this);
            titleTv.setText("抵用明细");
            titleTv.setTextColor(getResources().getColor(R.color.white));
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            LinearLayout.LayoutParams arrowIvLp = new LinearLayout.LayoutParams((int) UIUtils.dip2px(this, 12), (int) UIUtils.dip2px(this, 12));
            arrowIvLp.setMargins((int) UIUtils.dip2px(this, 3), 0, 0, 0);
            titleLl.addView(titleTv);

            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            actionBar.setCustomView(titleLl, layoutParams);
            titleLl.setEnabled(true);
        }
    }
}
