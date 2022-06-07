package com.hindbyte.redfun.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hindbyte.redfun.R;

public class WebActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    String title;
    String newsURL;
    NewWebView myWebView;
    ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("TITLE");
            newsURL = bundle.getString("URL");
        }
        TextView toolbar = findViewById(R.id.toolbar);
        toolbar.setText(title);
        frameLayout = findViewById(R.id.frameLayout);
        progressBar = findViewById(R.id.main_progress_bar);
        myWebView = new NewWebView(this);
        frameLayout.addView(myWebView);
        myWebView.loadUrl(newsURL);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public class NewWebView extends WebView {
        @SuppressLint("SetJavaScriptEnabled")
        public NewWebView(Context context) {
            super(context);
            WebSettings webSettings = getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setSupportMultipleWindows(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);

            setWebViewClient(new NewWebViewClient());
            setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, final int progress) {
                    progressBar.setProgress(progress);
                    progressBar.setVisibility(progress == 100 ? View.GONE : View.VISIBLE);
                }

                @Override
                public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                    NewWebView newWebView = new NewWebView(context);
                    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                    transport.setWebView(newWebView);
                    resultMsg.sendToTarget();
                    frameLayout.addView(newWebView);
                    return true;
                }

                @Override
                public void onCloseWindow(WebView view) {
                    frameLayout.removeViewAt(frameLayout.getChildCount()-1);
                }
            });
        }
    }

    private class NewWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK && onKeyCodeBack();
    }

    private boolean onKeyCodeBack() {
        if (frameLayout.getChildCount() > 1) {
            frameLayout.removeViewAt(frameLayout.getChildCount()-1);
        } else if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            finish();
        }
        return true;
    }

}