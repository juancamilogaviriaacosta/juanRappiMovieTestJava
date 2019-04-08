package com.rappi.juan.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rappi.juan.juanrappimovietest.AboutActivity;
import com.rappi.juan.juanrappimovietest.MainActivity;
import com.rappi.juan.juanrappimovietest.PopularActivity;
import com.rappi.juan.juanrappimovietest.R;
import com.rappi.juan.juanrappimovietest.SearchActivity;
import com.rappi.juan.juanrappimovietest.TopratedActivity;
import com.rappi.juan.juanrappimovietest.UpcomingActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utilidades {

    public static boolean onNavigationItemSelected(Activity actividad, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(actividad, MainActivity.class);
            actividad.startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(actividad, PopularActivity.class);
            actividad.startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(actividad, TopratedActivity.class);
            actividad.startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(actividad, UpcomingActivity.class);
            actividad.startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(actividad, AboutActivity.class);
            actividad.startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(actividad, SearchActivity.class);
            actividad.startActivity(intent);
        }

        actividad.finish();
        DrawerLayout drawer = (DrawerLayout) actividad.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static <T> T jsonParse(String jsonLine, Class<T> clase) {
        Gson gson = new GsonBuilder().create();
        T obj = gson.fromJson(jsonLine, clase);
        return obj;
    }

    public static void downloadFile(String fileURL, String saveDir) throws IOException {
        int BUFFER_SIZE = 4096;
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            String contentType = httpConn.getContentType();
            int contentLength = httpConn.getContentLength();

            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            } else {
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + fileName);

            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;

            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            return true;
        }else{
            return false;
        }
    }

    public static boolean setObjectCache(Object obj, String name, Activity activity) {
        final File suspend_f = new File(activity.getCacheDir(), name);

        FileOutputStream   fos  = null;
        ObjectOutputStream oos  = null;
        boolean            keep = true;

        try {
            fos = new FileOutputStream(suspend_f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (Exception e) {
            keep = false;
        } finally {
            try {
                if (oos != null)   oos.close();
                if (fos != null)   fos.close();
                if (keep == false) suspend_f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return keep;
    }

    public static Object getObjectCache(String name, Activity activity) {
        final File suspend_f = new File(activity.getCacheDir(), name);

        Object simpleClass = null;
        FileInputStream fis = null;
        ObjectInputStream is = null;

        try {
            fis = new FileInputStream(suspend_f);
            is = new ObjectInputStream(fis);
            simpleClass = is.readObject();
        } catch(Exception e) {
            String val= e.getMessage();
        } finally {
            try {
                if (fis != null)   fis.close();
                if (is != null)   is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return simpleClass;
    }
}
