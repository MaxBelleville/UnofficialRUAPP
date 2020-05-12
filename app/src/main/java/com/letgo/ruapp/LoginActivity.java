package com.letgo.ruapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.letgo.ruapp.Handlers.LoginHandler;
import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.Requests.LoginRequests;
import com.letgo.ruapp.Requests.SimpleCallback;
import com.letgo.ruapp.Services.RetrofitClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.ru_username) EditText username;

    @BindView(R.id.ru_password) EditText password;

    @BindView(R.id.ru_token) EditText token;

    @BindView(R.id.loginLayout) ConstraintLayout loginLayout;

    @BindView(R.id.loadingLayout) ConstraintLayout loadingLayout;

    @BindView(R.id.incorrectText) TextView incorrect;

    @OnClick(R.id.ru_login) void submit() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String tok = token.getText().toString();
        login(user,pass,tok);
    }

    private String exec;
    private String savedUsername="";
    private String savedPassword="";
    private void error(){
        incorrect.setVisibility(View.VISIBLE);
        username.setText("");
        password.setText("");
        token.setText("");
    }

    private void startLogin(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            String str= new LoginHandler().load(getApplicationContext());
            if (!str.isEmpty()){
                loadingLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
            }
            Retrofit retrofit= RetrofitClient.startRetrofit(this);
            LoginRequests loginRequests= retrofit.create(LoginRequests.class);
            Call<ResponseBody> call=loginRequests.getExecution("https://cas.ryerson.ca/login");
            call.enqueue(new SimpleCallback<ResponseBody>() {
                @Override
                public void getResponse(ResponseBody body) throws IOException {
                    Document document = Jsoup.parse(body.string());
                    exec=document.getElementsByAttributeValue("name","execution").val();
                    if(!str.isEmpty()){
                        String[] split =str.split("\t");
                        login(split[0],split[1],"None");
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
            loginLayout.setVisibility(View.GONE);
            if(new ScheduleHandler().load(getApplicationContext())){
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void login (String user, String pass, String token){
        incorrect.setVisibility(View.GONE);
        incorrect.setText("Incorrect password or username, please try again.");
        LoginRequests loginRequests=RetrofitClient.getRetrofit().create(LoginRequests.class);
        Call<ResponseBody> call=loginRequests.Login("https://cas.ryerson.ca/login",
                user,pass,exec,"submit");
        call.enqueue(new SimpleCallback<ResponseBody>() {
            @Override
            public void getResponse(ResponseBody body) throws IOException {
                Document document = Jsoup.parse(body.string());
                String exec2=document.getElementsByAttributeValue("name","execution").val();
                if(!document.title().startsWith("Log In Suc")) {
                    incorrect.setText("Authentication error, Please try again.");
                    loadingLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                    Call<ResponseBody> call = loginRequests.Ouath("https://cas.ryerson.ca/login",
                            token, true, exec2, "submit");
                    call.enqueue(new SimpleCallback<ResponseBody>() {
                        @Override
                        public void getResponse(ResponseBody response) throws IOException {
                            new LoginHandler().save(getApplicationContext(),user,pass);
                            new ScheduleHandler().readSchedule(getApplicationContext());
                        }
                        @Override
                        public void handleError(int code) {
                            error();
                        }
                    });
                }
                else new ScheduleHandler().readSchedule(getApplicationContext());
            }
            @Override
            public void handleError(int code){
                error();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        startLogin();
    }
}
