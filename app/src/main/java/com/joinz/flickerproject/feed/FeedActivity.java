package com.joinz.flickerproject.feed;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joinz.flickerproject.R;
import com.joinz.flickerproject.imagebig.ImageActivity;
import com.joinz.flickerproject.model.App;
import com.joinz.flickerproject.model.FlickrResponse;
import com.joinz.flickerproject.model.PhotoItem;
import com.joinz.flickerproject.model.RetrofitApi;

import java.util.List;


public class FeedActivity extends AppCompatActivity {

    //https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=10c6e8498de0f7a868c4c59af1134814&format=json&nojsoncallback=1
    public static final String API_KEY = "10c6e8498de0f7a868c4c59af1134814";

    private RecyclerView rv;
    private GridLayoutManager layoutManager;
    private FeedAdapter feedAdapter;
    private retrofit2.Call<FlickrResponse> responseCall;
    private View view;
    private retrofit2.Callback<FlickrResponse> callback;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.coordinatorLayout);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(layoutManager);

        loadPhotosViaRetrofit();
    }

    private void loadPhotosViaRetrofit() {
        RetrofitApi service = App.getInstance().getRetrofitApi();
        responseCall = service.listRepos("flickr.photos.getRecent", API_KEY, "json", 1);
        callback = new retrofit2.Callback<FlickrResponse>() {
            @Override
            public void onResponse(retrofit2.Call<FlickrResponse> call, retrofit2.Response<FlickrResponse> response) {
                List<PhotoItem> photos = response.body().getPhotos().getPhoto();
                if (rv.getAdapter() == null) {
                    feedAdapter = new FeedAdapter(photos, photoItem -> {
                        String url = String.format(
                                "https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg",
                                photoItem.getFarm(),
                                photoItem.getServer(),
                                photoItem.getId(),
                                photoItem.getSecret(),
                                "h"
                        );
                        Intent intent = new Intent(FeedActivity.this, ImageActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                    });
                    rv.setAdapter(feedAdapter);
                }
                feedAdapter.setData(photos);
            }

            @Override
            public void onFailure(retrofit2.Call<FlickrResponse> call, Throwable t) {
                snackbar = Snackbar.make(view, "Request failed", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Try again", v -> {
                    responseCall.cancel();
                    responseCall.clone().enqueue(callback);
                }).show();
            }
        };
        responseCall.enqueue(callback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (responseCall != null) {
            responseCall.cancel();
        }
    }
}

