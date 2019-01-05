package com.joinz.flickerproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


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
//        loadPhotosViaHttpUrlConnection();
//        loadPhotosViaOkHttp();
        loadPhotosViaRetrofit();
    }

    public interface RetrofitPhotos {
        @GET("services/rest/")
        retrofit2.Call<Response> listRepos(
                @Query("method") String method,
                @Query("api_key") String apiKey,
                @Query("format") String format,
                @Query("nojsoncallback") int noJsonCallback
        );
    }

    private void loadPhotosViaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitPhotos service = retrofit.create(RetrofitPhotos.class);
        retrofit2.Call<Response> responseGson = service.listRepos("flickr.photos.getRecent", API_KEY, "json", 1);
        responseGson.enqueue(new retrofit2.Callback<Response>() {
            @Override
            public void onResponse(retrofit2.Call<Response> call, retrofit2.Response<Response> response) {
                titles = new ArrayList<>();
                List<PhotoItem> photos = response.body().getPhotos().getPhoto();
                for (int i = 0; i < photos.size(); i++) {
                    titles.add(photos.get(i).getTitle());
                }
                rvAdapter.setTitles(titles);
            }

            @Override
            public void onFailure(retrofit2.Call<Response> call, Throwable t) {

            }
        });
    }

    private void loadPhotosViaOkHttp() {
        final OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.flickr.com/services/rest/?" +
                            "method=flickr.photos.getRecent&" +
                            "api_key=" + API_KEY + "&format=json&nojsoncallback=1")
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    ResponseBody responseBody = response.body();
                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        Gson gson = new Gson();
                        Response responseGson = gson.fromJson(responseBody.string(), Response.class);
                    titles = new ArrayList<>();
                    for (int i = 0; i < responseGson.getPhotos().getPhoto().size(); i++) {
                        titles.add(responseGson.getPhotos().getPhoto().get(i).getTitle());
                    }

                    setString = new Runnable() {
                        @Override
                        public void run() {
                            rvAdapter.setTitles(titles);
                        }
                    };
                    handler.post(setString);
                }
            });
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
            InputStream inputStream = null;
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.flickr.com/services/rest/?" +
                            "method=flickr.photos.getRecent&" +
                            "api_key=" + API_KEY + "&format=json&nojsoncallback=1");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = httpURLConnection.getInputStream();
                    final String stringFromIS = getStringFromIS(inputStream);
                    Log.d("TAG", stringFromIS);

                    Gson gson = new Gson();
                    Response responseGson = gson.fromJson(stringFromIS, Response.class);
                    titles = new ArrayList<>();
                    for (int i = 0; i < responseGson.getPhotos().getPhoto().size(); i++) {
                        titles.add(responseGson.getPhotos().getPhoto().get(i).getTitle());
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
                finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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

