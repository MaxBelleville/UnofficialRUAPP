package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.letgo.ruapp.Handlers.LoginHandler;
import com.letgo.ruapp.Handlers.ScheduleHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.ru_username) EditText username;

    @BindView(R.id.ru_password) EditText password;

    @BindView(R.id.webView) WebView webView;

    @BindView(R.id.fakeWebView) ConstraintLayout webLayout;

    @BindView(R.id.loadingLayout) ConstraintLayout loadingLayout;

    @OnClick(R.id.ru_login) void submit() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        login(user,pass);
    }

    private void login (String user, String pass){
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
        webView.loadUrl("https:cas.ryerson.ca/login");
        Activity activity=this;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(true);
   //     webView.getSettings().setAppCacheEnabled(true);
    //  webView.getSettings().setUserAgentString("Mozilla/5.0 (Android 8.0; Mobile; rv:64.0) Gecko/64.0 Firefox/64.0");
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                CookieSyncManager.getInstance().sync();
                LoginHandler loginHandler=new LoginHandler(activity,view,"login.js");
                ScheduleHandler scheduleHandler=new ScheduleHandler(activity,view,"schedule.js");
                if(!loginHandler.isDone()){
                    loginHandler.update(user, pass);
                }
                else if(!scheduleHandler.isDone()) {
                    scheduleHandler.update();
                }
                else {
                    Intent intent = new Intent(activity,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
       String str= new LoginHandler().load(getApplicationContext());
                if(!str.isEmpty()){
            String[] split =str.split("\t");
            webLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
            login(split[0],split[1]);
        }
    }
}
