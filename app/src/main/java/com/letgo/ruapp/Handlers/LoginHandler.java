package com.letgo.ruapp.Handlers;


import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.letgo.ruapp.R;
import com.letgo.ruapp.Requests.LoginRequests;
import com.letgo.ruapp.Requests.SimpleCallback;
import com.letgo.ruapp.Services.RetrofitClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginHandler {

    public LoginHandler(){}

    public void save(Context context, String user, String pass){
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
