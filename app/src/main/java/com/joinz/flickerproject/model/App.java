package com.joinz.flickerproject.model;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {
    public static App instance;
    private FlickrApi flickrApi;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public FlickrApi getFlickrApi() {
        if (flickrApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            flickrApi = retrofit.create(FlickrApi.class);
        }
        return flickrApi;
    }
}
