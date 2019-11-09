package com.example.interview.api;

import com.example.interview.page.main.model.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users")
    Call<ResponseModel<UserModel>> getUserList(@Query("page") int page);
}
