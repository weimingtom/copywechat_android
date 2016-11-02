package com.anjuke.copywechat.copywechat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

public class WebActivityForUrlMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_activity_for_url_message);


        String weburl = getIntent().getExtras().get(ConstantCls.URL_ADDRESS_FOR_WEBACTIVITY).toString();

        final WebView webView = (WebView)findViewById(R.id.webview_for_tab4);
        webView.loadUrl(weburl);

        Button webviewGobackBtn = (Button)findViewById(R.id.webview_goback_btn);
        webviewGobackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.goBack();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
