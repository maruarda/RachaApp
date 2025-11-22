package com.example.rachaapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Use 10.0.2.2 for Android Emulator
    // Use your computer's real IP (e.g., 192.168.1.x) if using a real phone
    private static final String BASE_URL = "http://10.0.2.2:8081/";

    private static RetrofitClient instance = null;
    private ApiService myApi;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApi() {
        return myApi;
    }
}