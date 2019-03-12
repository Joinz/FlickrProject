package com.joinz.flickerproject.model;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrApi {
    @GET("services/rest/")
    Observable<FlickrResponse> recentPhotos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback
    );

    @GET("services/rest/")
    Observable<FlickrResponse> searchPhotos(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("text") String text
    );
}
