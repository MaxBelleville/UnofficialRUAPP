package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

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
                    scheduleHandler.save();
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
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
       String str= new LoginHandler().load(getApplicationContext());
                if(!str.isEmpty()){
            String[] split =str.split("\t");
            webLayout.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.VISIBLE);
            login(split[0],split[1]);
        }
        }
        else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            webLayout.setVisibility(View.GONE);
           // new ScheduleHandler().logLoad(getApplicationContext());
            if(new ScheduleHandler().load(getApplicationContext())){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
