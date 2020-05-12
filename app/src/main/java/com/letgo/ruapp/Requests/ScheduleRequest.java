package com.letgo.ruapp.Requests;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ScheduleRequest {
    @GET
    Call<ResponseBody> initSchedule(@Url String url);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> getSchedule(@Url String url,@Field("startOfWeek") String startWeek);
}
