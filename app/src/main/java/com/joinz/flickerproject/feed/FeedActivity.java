package com.joinz.flickerproject.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.joinz.flickerproject.R;
import com.joinz.flickerproject.imagebig.ImageActivity;
import com.joinz.flickerproject.model.App;
import com.joinz.flickerproject.model.FlickrApi;
import com.joinz.flickerproject.model.FlickrResponse;
import com.joinz.flickerproject.model.PhotoItem;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedActivity extends AppCompatActivity {

    public static final String API_KEY = "10c6e8498de0f7a868c4c59af1134814";
    public static final String TAG = "DEBUG_TAG";
    public static final FlickrApi service = App.getInstance().getFlickrApi();

    private RecyclerView rv;
    private EditText etSearch;
    private FeedAdapter feedAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearch = findViewById(R.id.etSearch);
        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(layoutManager);
        feedAdapter = new FeedAdapter(Collections.emptyList(), listener);
        rv.setAdapter(feedAdapter);

        observeTextChanges();
//        loadPhotosViaRetrofit();
    }

    OnPhotoClickListener listener = photoItem -> {
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
    };

    private Observable<FlickrResponse> searchPhotos(String s) {
        Log.d(TAG, "searchPhotos()");
        return service.searchPhotos("flickr.photos.search", API_KEY, "json", 1, s);
    }

    private void observeTextChanges() {
        Disposable flickrResponseSearch = RxTextView.textChanges(etSearch)
                .observeOn(Schedulers.io())
                .map(CharSequence::toString)
                .map(String::trim)
                .filter(s -> s.length() > 2)
                .debounce(450, TimeUnit.MILLISECONDS)
                .switchMap(this::searchPhotos)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(flickrResponse -> {
                    Log.d(TAG, "flickrResponseSearch");
                    List<PhotoItem> photos = flickrResponse.getPhotos().getPhoto();
                    feedAdapter.setData(photos, listener);
//                    rv.setAdapter(feedAdapter);
//                    Log.d(TAG, "rv.SetAdapter()");
                }, this::showSnakeBar);
        compositeDisposable.add(flickrResponseSearch);
    }

    private void showSnakeBar(Throwable throwable) {
        Snackbar.make(etSearch, throwable.getMessage(), Snackbar.LENGTH_INDEFINITE)
//        .setAction("Repeat request", v -> searchPhotos(etSearch.getText().toString()));
        .setAction("Repeat request", v -> observeTextChanges())
        .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}



