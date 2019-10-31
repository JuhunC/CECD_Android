package com.example.cecd;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeleteAPIs {
    @Headers("Content-Type: application/json")
    @POST("/inpainting")
    Call<String> deleteObjectImage(@Body String body);
}