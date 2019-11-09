package com.example.interview.api;

public class ApiInstance {

    public static ApiService apiService (){
        return ApiClient.getRetrofit().create(ApiService.class);
    }
}
