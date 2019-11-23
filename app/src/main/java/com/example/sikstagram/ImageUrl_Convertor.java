package com.example.sikstagram;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ImageUrl_Convertor {

    private String [] url_tails = {"_MF_ATTACH_01.jpg","_MF_REPR_ATTACH_01.jpg","_MF_REPR_ATTACH_01.gif","_MF_ATTACH_01.JPG","_MF_REPR_ATTACH_01.JPG"};

    public String getUrl(String plt_code) {
        MyTask task= new MyTask();
        String url="";
        try {
            url = task.execute(plt_code).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return url;
    }


    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String no_img_url = "http://www.nongsaro.go.kr/cms_contents/301/";
            Document doc = null;

            try {
                doc = Jsoup.connect(no_img_url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //그림 정보가 없는 페이지에서의 타이틀 가져오기.
            String no_img_title = doc.title();

            String url_head = "http://www.nongsaro.go.kr/cms_contents/301/"+params[0];
            String url="";
            String title="";
            for(String url_tail : url_tails) {
                url = url_head+url_tail;
                title="";
                try {
                    doc = Jsoup.connect(url).get();
                    title = doc.title();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!title.equals(no_img_title)){
                    return url;
                }
            }
            return "NO IMAGE :(";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}


