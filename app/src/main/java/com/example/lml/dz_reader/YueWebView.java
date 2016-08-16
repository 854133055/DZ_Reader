package com.example.lml.dz_reader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YueWebView extends AppCompatActivity {

    @BindView(R.id.yueWebView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue_web_view);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("URL");
        initWebView(url);
    }

    public void initWebView(String url){
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(url);
    }

}
