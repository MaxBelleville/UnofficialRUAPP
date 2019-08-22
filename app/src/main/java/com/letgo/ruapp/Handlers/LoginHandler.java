package com.letgo.ruapp.Handlers;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.letgo.ruapp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginHandler extends Handler {
    @BindView(R.id.webView) WebView webView;

    @BindView(R.id.fakeWebView) ConstraintLayout webLayout;

    @BindView(R.id.loadingLayout) ConstraintLayout loadingLayout;

    @BindView(R.id.incorrectText) TextView incorrect;
    public LoginHandler(){}

    public static boolean isDone=false;

    @Override
    public boolean isDone() {
        return isDone;
    }
    @Override
    public void update(Object ... object){
        String user =(String)object[0];
        String pass =(String)object[1];
        js=js.replace("GET_USERNAME",user).replace("GET_PASSWORD",pass);
        if(Build.VERSION.SDK_INT >= 19){
            view.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    if(s.equals("2")||s.equals("1")) {
                        File file = new File("login");
                        if(!file.exists())
                            save(user,pass);
                    }
                    if(s.equals("2")){
                        webLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                        webView.loadUrl("https://m.ryerson.ca/core_apps/schedule/index.cfm");
                        LoginHandler.isDone=true;
                    }
                    else if(s.equals("1")){
                        webLayout.setVisibility(View.GONE);
                        loadingLayout.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);

                    }
                    else if(s.equals("0")) {
                        incorrect.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public LoginHandler(Activity activity, WebView view, String jsLoc) {
        super(activity.getApplicationContext(),view,jsLoc);
        ButterKnife.bind(this, activity);
    }

    private void save(String user, String pass){
        String filename = "login";
        String fileContents = user+"\t"+pass;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String load(Context context) {
        String str="";
        try {
            FileInputStream fin = context.openFileInput("login");
            int c;
            while( (c = fin.read()) != -1){
                str += (char)c;
            }
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
