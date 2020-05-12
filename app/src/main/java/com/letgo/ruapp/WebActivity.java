package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.webkit.WebView.*;

public class WebActivity extends AppCompatActivity {
    @BindView(R.id.WebView)
    WebView webView;
    @BindView(R.id.loadingLayout)
    ConstraintLayout loadingLayout;
    private boolean loadOnce=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        Log.d("Special","Start");
        String url = getIntent().getStringExtra("Url");
        webView.getSettings().setUserAgentString(getString(R.string.user_agent));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if(loadOnce) {
                    webView.setVisibility(View.VISIBLE);
                    loadingLayout.setVisibility(View.GONE);
                    loadOnce=false;
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack())webView.goBack();
        else finish();
    }
}
