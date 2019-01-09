package com.joinz.flickerproject.model;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    public static App instance;
    private RetrofitApi retrofitApi;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public RetrofitApi getRetrofitApi() {
        if (retrofitApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitApi = retrofit.create(RetrofitApi.class);
        }

        return retrofitApi;
    }
}
