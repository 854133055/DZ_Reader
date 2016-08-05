package com.example.lml.dz_reader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lml.dz_reader.db.DaoMaster;
import com.example.lml.dz_reader.db.DaoSession;

import java.io.ByteArrayOutputStream;

public class WebContentActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase mdb;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private EditText titleText;
    private EditText contentText;
    private WebView mWebView;
    private byte[] icon_byte;
    private String getTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        Intent intent = getIntent();
        String data = intent.getStringExtra("url");

        initWebView(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_webcon,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_collect){
            Intent intent = new Intent(this, DialogActivity.class);
            intent.putExtra("icon_byte[]", icon_byte);
            intent.putExtra("title", getTitle);
            startActivity(intent);
            Toast.makeText(this,"添加开始",Toast.LENGTH_LONG).show();
        } if(itemId == R.id.action_addtion) {
            Toast.makeText(this,"收藏开始",Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * WebView显示界面，同时获取网页title，显示在EditText上；获取网页icon，转换成byte[]格式，方便以后存到数据库中
     */
    public void initWebView(String url){
        mWebView = (WebView)this.findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getTitle = title;
            }

            //获取网页图标
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                //将bitmap转换程byte[]形式，存进数据库
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                icon_byte = byteArrayOutputStream.toByteArray();
            }
        });

//            //获取网页打开进度
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                progressBar.setProgress(newProgress);
//                if(newProgress == 100) {
//                    progressBar.setProgress(0);
//                    //获取当前网页地址
//                    url01.setText("url: " + mWebView.getUrl());
//
//                }
//            }
        mWebView.loadUrl(url);
    }





}
