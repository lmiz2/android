package com.example.songhyeonseok.drawing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
      /*  Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
        setLayout();



        // 웹뷰에서 자바스크립트실행가능
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 구글홈페이지 지정
        if(getIntent().getIntExtra("Here",0) == 1) {
            mWebView.loadUrl("https://hanium-draw-commu.firebaseapp.com/");
        }
        else{
            mWebView.loadUrl("https://hanium-draw-commu.firebaseapp.com/Newsfeed.html");
        }
        // WebViewClient 지정
        mWebView.setWebViewClient(new WebViewClientClass());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
//            mWebView.goBack();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /*
     * Layout
     */
    private void setLayout(){
        mWebView = (WebView) findViewById(R.id.webview);
    }

}
