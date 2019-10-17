package com.example.cecd;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadAPIs {
    @Multipart
    @POST("/yolov3")
    Call<String> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);
}