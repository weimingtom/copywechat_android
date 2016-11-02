package com.anjuke.copywechat.copywechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anjuke.android.commonutils.SharedPreferencesHelper;
import com.anjuke.copywechat.copywechat.util.ConstantCls;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEt = (EditText)findViewById(R.id.login_username_et);
        mPasswordEt= (EditText)findViewById(R.id.login_password_et);
        mSubmitBtn = (Button)findViewById(R.id.login_submit_btn);
        mSubmitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_submit_btn:
                //处理登录逻辑

                SharedPreferencesHelper sp = SharedPreferencesHelper.getInstance(this);
                sp.putString(ConstantCls.LOGIN_CODE_STORED_IN_SHAREPREFERENCE,mUsernameEt.getText().toString());

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClass(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
