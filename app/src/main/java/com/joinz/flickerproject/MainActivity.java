package com.joinz.flickerproject;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    //https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=10c6e8498de0f7a868c4c59af1134814&format=json&nojsoncallback=1
    public static final String API_KEY = "10c6e8498de0f7a868c4c59af1134814";

    Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler;
    private Runnable setString;
    RecyclerView rv;
    GridLayoutManager layoutManager;
    rvAdapter rvAdapter;
    List<String> titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(layoutManager);
        rvAdapter = new rvAdapter(mockTitles());
        rv.setAdapter(rvAdapter);

        handler = new Handler(getMainLooper());
        loadPhotosViaHttpUrlConnection();
    }

    private List<String> mockTitles() {
        List<String> mock = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mock.add("Mock" + i);
        }
        return mock;
    }

    private void loadPhotosViaHttpUrlConnection() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.flickr.com/services/rest/?" +
                            "method=flickr.photos.getRecent&" +
                            "api_key=" + API_KEY + "&format=json&nojsoncallback=1");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    final String stringFromIS = getStringFromIS(inputStream);
                    Log.d("TAG", stringFromIS);

                    Gson gson = new Gson();
                    Response response = gson.fromJson(stringFromIS, Response.class);
                    titles = new ArrayList<>();
                    for (int i = 0; i < response.getPhotos().getPhoto().size(); i++) {
                        titles.add(response.getPhotos().getPhoto().get(i).getTitle());
                    }

                    setString = new Runnable() {
                        @Override
                        public void run() {
                            rvAdapter.setTitles(titles);
                        }
                    };
                    handler.post(setString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getStringFromIS(InputStream inputStream) throws IOException {
        int n;
        char[] buffer = new char[1024 * 4];
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        StringWriter stringWriter = new StringWriter();
        for (; (n = inputStreamReader.read(buffer)) != -1 ;) {
            stringWriter.write(buffer, 0, n);
        }
        return stringWriter.toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (setString != null) {
            handler.removeCallbacks(setString);
        }
    }
}

