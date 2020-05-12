package com.letgo.ruapp.Requests;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LoginRequests {
    @GET
    Call<ResponseBody> getExecution(@Url String url);
    @FormUrlEncoded
    @POST
    Call<ResponseBody> Login(@Url String url,
                             @Field("username") String username,
                             @Field("password") String password,
                             @Field("execution") String execution,
                             @Field("_eventId") String submit);
    @FormUrlEncoded
    @POST
    Call<ResponseBody> Ouath(@Url String url,
                             @Field("token") String token,
                             @Field("rememberMe") Boolean remember,
                             @Field("execution") String execution,
                             @Field("_eventId") String submit);
}
