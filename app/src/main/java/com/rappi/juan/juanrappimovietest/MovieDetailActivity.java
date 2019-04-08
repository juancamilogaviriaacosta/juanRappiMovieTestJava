package com.rappi.juan.juanrappimovietest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.rappi.juan.models.Result;
import com.rappi.juan.network.DownloadImageThread;


public class MovieDetailActivity extends AppCompatActivity {

    private Result selectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedMovie = (Result) getIntent().getExtras().getSerializable("selectedMovie");

        DownloadImageThread dit = new DownloadImageThread(this, selectedMovie.getPoster_path());
        dit.execute();

        ((TextView) findViewById(R.id.title1)).setText(selectedMovie.getTitle());
        ((TextView) findViewById(R.id.popularity1)).setText(String.valueOf(selectedMovie.getPopularity()));
        ((TextView) findViewById(R.id.vote_average1)).setText(String.valueOf(selectedMovie.getVote_average()));
        ((TextView) findViewById(R.id.original_language1)).setText(selectedMovie.getOriginal_language());
        ((TextView) findViewById(R.id.release_date1)).setText(selectedMovie.getRelease_date());
        ((TextView) findViewById(R.id.overview1)).setText(selectedMovie.getOverview());
    }

}
