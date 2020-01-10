package com.letgo.ruapp.Requests;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class SimpleCallback<T> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response){
        if(response.isSuccessful()) {
            try {
                getResponse(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            handleError(response.code());
        }
    }
    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        t.printStackTrace();
    }
    public abstract void getResponse(T response) throws IOException;
    public void handleError(int code) {
        Log.d("Special","HTML Error: "+code);
    };
}
