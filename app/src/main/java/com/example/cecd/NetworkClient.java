package com.example.cecd;

import android.content.Context;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
//    private static final String BASE_URL = "http://192.168.43.123:8080/";
//    private static final String BASE_URL = "http://192.168.0.56:8080/";
    private static final String BASE_URL = "http://210.94.194.48:8080/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100,TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.e("LOGLOG",retrofit.baseUrl().toString());
        return retrofit;
    }

}