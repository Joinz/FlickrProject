package com.joinz.flickerproject.model;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApi {
    @GET("services/rest/")
    retrofit2.Call<FlickrResponse> listRepos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback
    );
}
