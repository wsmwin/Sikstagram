package com.example.sikstagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class GetBitmapImageFromUrl {

    BitmapFactory.Options options = null;

    // 기본 getImageBitmap 함수. Input으로 url을 받고 비트맵 타입으로 리턴한다.
    public Bitmap getImageBitmap(String url) {
        GetImageTask task = new GetImageTask();
        Bitmap bitmap= null;

        try {
            bitmap = task.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // getImageBitmap override. options를 받아서 bitmap을 받아오는 옵션을 설정.
    public Bitmap getImageBitmap(String url, BitmapFactory.Options o) {
        GetImageTask task = new GetImageTask();
        Bitmap bitmap= null;
        options = o;
        try {
            bitmap = task.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    // 비동기 처리 방법 AsyncTask를 이용하여 비트맵을 url에서 가져옴
    private class GetImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap = null;

            try {
                URL url = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();

                bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);

            }  catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
        }
    }
}
