package com.letgo.ruapp.Services;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.letgo.ruapp.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static OkHttpClient.Builder builder = new OkHttpClient.Builder();
    static final String BASE_URL = "https://www.ryerson.ca/";
    public static Retrofit startRetrofit(Context context) {
        ClearableCookieJar cookieJar =
                new MyPersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.cookieJar(cookieJar);
        builder.addInterceptor(logging);
        builder.addNetworkInterceptor(new UserAgent(context.getString(R.string.user_agent)));
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }

        return retrofit;
    }
    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
class UserAgent implements Interceptor{

    private final String userAgent;

    public UserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", userAgent)
                .build();
        return chain.proceed(requestWithUserAgent);
    }
}