package com.example.dmrf.intelligentassistant.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.dmrf.intelligentassistant.R;

/**
 * Created by DMRF on 2017/8/9.
 */

public class WebViewActivity extends AppCompatActivity {

    public static String WEB_URL = "set_open_link_from_webview";
    private WebView webView;
    private String url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        Intent intent = getIntent();
        url = intent.getStringExtra(WEB_URL);
        init();
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webview);
        //WebView加载本地资源
        // webView.loadUrl("file:///android_asset/Webview.htlm");
        //Webview加载外部资源
        webView.loadUrl(url);
        //覆盖WebView通过第三方或者系统浏览器打开网页的行为，使得网页可以在WebView中直接打开
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值为true时控制网页在webView中打开，false是在系统浏览器或默认浏览器中打开
                view.loadUrl(url);
                return true;
                //WebViewClient帮助WebView去处理一些页面控制和请求通知
            }
        });

        //启用支持JavaScript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //WebView加载页面优先使用缓存加载
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100之间的整数
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    //改写手机物理按键返回的逻辑

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果有上一层网页

            if (webView.canGoBack()) {
                webView.goBack();//返回上一层界面
            } else {
                WebViewActivity.this.finish();
            }

            return true;
        } else {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
