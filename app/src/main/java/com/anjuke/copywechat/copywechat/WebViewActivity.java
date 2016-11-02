package com.anjuke.copywechat.copywechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjuke.copywechat.copywechat.util.ConstantCls;

public class WebViewActivity extends AppCompatActivity {
    private WebView wv;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra("weburl");

        wv = (WebView) findViewById(R.id.webview_for_tab4);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        Log.i(ConstantCls.LOG_DEBUG_TAG, "cacheDirPath="+cacheDirPath);
        //设置数据库缓存路径
        settings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);

        wv.setWebViewClient(wvc);
        wv.setWebChromeClient(wvcc);
        /*
        wv.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView webView, String url){
                webView.loadUrl(url);

                return true;
            }
        });*/
        Toast.makeText(this,"url: "+url, Toast.LENGTH_SHORT).show();
        //wv.loadUrl("http://www.sina.com.cn");
        wv.loadUrl("file:///android_asset/test.html");
        RelativeLayout webview_for_tab4_notify_ly = (RelativeLayout)findViewById(R.id.webview_for_tab4_notify_ly);
        //webview_for_tab4_notify_ly.setBackgroundColor(getResources().getColor(R.color.header_normal));
    }

    WebViewClient wvc = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(WebViewActivity.this,"WebViewClient.shouldOverrideUrlLoading",Toast.LENGTH_SHORT).show();
// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
            wv.loadUrl(url);
// 记得消耗掉这个事件。给不知道的朋友再解释一下，Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Toast.makeText(WebViewActivity.this,"WebViewClient.onPageStarted",Toast.LENGTH_SHORT).show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Toast.makeText(WebViewActivity.this,"WebViewClient.onPageFinished",Toast.LENGTH_SHORT).show();
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Toast.makeText(WebViewActivity.this,"WebViewClient.onLoadResource",Toast.LENGTH_SHORT).show();
            super.onLoadResource(view, url);
        }

    };


    WebChromeClient wvcc = new WebChromeClient() {
        //处理进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
            }
            super.onProgressChanged(view, newProgress);
        }
        // 处理Alert事件

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

// 构建一个Builder来显示网页中的alert对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("计算1+2的值");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }

            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //可以用onReceivedTitle()方法修改网页标题
            WebViewActivity.this.setTitle(title);

            super.onReceivedTitle(view, title);

        }

// 处理Confirm事件

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("删除确认");

            builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }

            });

            builder.setNeutralButton(android.R.string.cancel, new AlertDialog.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }

            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;

        }

// 处理提示事件

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,

                                  JsPromptResult result) {

// 看看默认的效果
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what) {
                    case 0:
//                        pd.show();// 显示进度对话框
                        break;
                    case 1:
//                        pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };
}
