package com.joinz.flickerproject;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitPhotos {
    @GET("services/rest/")
    retrofit2.Call<Response> listRepos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback
    );
}
