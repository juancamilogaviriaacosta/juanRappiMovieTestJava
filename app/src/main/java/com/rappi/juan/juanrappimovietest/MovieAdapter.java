package com.rappi.juan.juanrappimovietest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rappi.juan.models.Result;

import java.io.File;
import java.util.List;

public class MovieAdapter extends BaseAdapter {


    private Activity actividad;
    private List<Result> lista;
    private static LayoutInflater inflater;

    public MovieAdapter(Activity actividad, List<Result> lista) {
        this.actividad = actividad;
        this.lista = lista;
        inflater = (LayoutInflater) actividad.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(lista.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;

        final Result temp = lista.get(position);
        View row = convertView;
        if (row == null){
            row = inflater.inflate(R.layout.movie_row, null);
            holder = new Holder();
            holder.imagen = (ImageView) row.findViewById(R.id.movieImage);
            holder.nombre = (TextView) row.findViewById(R.id.movieName);
            holder.director = (TextView) row.findViewById(R.id.movieDirector);
            holder.fecha = (TextView) row.findViewById(R.id.movieDate);
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        String imageNameWithPath = actividad.getApplicationContext().getFilesDir().getPath() + '/' + temp.getPoster_path();
        File img = new File(imageNameWithPath);
        holder.imagen.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(img)));

        holder.nombre.setText(temp.getTitle());
        holder.director.setText(temp.getOriginal_language());
        holder.fecha.setText(temp.getRelease_date());

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(actividad, MovieDetailActivity.class);
                intent.putExtra("selectedMovie", temp);
                actividad.startActivity(intent);
            }
        });
        return row;
    }

    public class Holder
    {
        ImageView imagen;
        TextView nombre;
        TextView director;
        TextView fecha;
    }
}
