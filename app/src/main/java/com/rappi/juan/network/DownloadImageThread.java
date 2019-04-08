package com.rappi.juan.network;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.rappi.juan.juanrappimovietest.R;
import com.rappi.juan.util.Utilidades;

import java.io.File;

public class DownloadImageThread extends AsyncTask {

    private Activity activity;
    private String poster_path;

    public DownloadImageThread(Activity activity, String poster_path) {
        this.activity = activity;
        this.poster_path = poster_path;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            if(Utilidades.isConnected(activity.getApplicationContext())) {
                String path = activity.getApplicationContext().getFilesDir().getPath();
                String posterPath = "https://image.tmdb.org/t/p/w500/" + poster_path;
                Utilidades.downloadFile(posterPath, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ImageView imagenDetalle = (ImageView) activity.findViewById(R.id.imagenDetalle);
        String imageNameWithPath = activity.getApplicationContext().getFilesDir().getPath() + '/' + poster_path;
        File img = new File(imageNameWithPath);
        imagenDetalle.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(img)));
    }
}
