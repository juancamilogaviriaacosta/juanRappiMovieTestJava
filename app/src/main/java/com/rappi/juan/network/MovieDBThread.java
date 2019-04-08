package com.rappi.juan.network;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.google.gson.Gson;
import com.rappi.juan.juanrappimovietest.MovieAdapter;
import com.rappi.juan.juanrappimovietest.R;
import com.rappi.juan.models.MovieDBResult;
import com.rappi.juan.util.Utilidades;
import com.rappi.juan.models.Result;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MovieDBThread extends AsyncTask {

    private Activity activity;
    private String type;
    private String url;
    private String apiKey;
    private MovieDBResult movieDBResult;

    public MovieDBThread(Activity activity, String url, String type) {
        this.activity = activity;
        this.url = url;
        this.type = type;
        this.apiKey = "&api_key=4dd8fd0ba2a9e77b20b257dbbbfd85d3";
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {

            if(Utilidades.isConnected(activity.getApplicationContext())) {
                URL githubEndpoint = new URL(url + apiKey + "&page=1");
                HttpsURLConnection connection = (HttpsURLConnection) githubEndpoint.openConnection();

                if (connection.getResponseCode() == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    movieDBResult = new Gson().fromJson(bufferedReader, MovieDBResult.class);
                    Utilidades.setObjectCache(movieDBResult, type, activity);

                    for (Result tmp : movieDBResult.getResults()) {
                        String path = activity.getApplicationContext().getFilesDir().getPath();
                        String posterPath = "https://image.tmdb.org/t/p/w185/" + tmp.getPoster_path();
                        Utilidades.downloadFile(posterPath, path);
                    }
                }
                connection.disconnect();
            } else {
                movieDBResult = (MovieDBResult) Utilidades.getObjectCache(type, activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ListView listView = (ListView) activity.findViewById(R.id.movie_list);
        MovieAdapter movieAdapter = new MovieAdapter(activity, movieDBResult != null ? movieDBResult.getResults() : new ArrayList<Result>());
        listView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
    }
}
