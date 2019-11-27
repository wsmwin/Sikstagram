package com.example.sikstagram;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

// 농사로 홈페이지에 식물 사진 url이 식물코드 별로 양식이 다르기 때문에 코드 별로 해당 url을 convert 함.
public class ImageUrl_Convertor {

    // 식물 코드 별로 저장되어 있는 서버에 파일 명이 다름. 총 다섯 개의 형식이 존재함.
    private String [] url_tails = {"_MF_ATTACH_01.jpg","_MF_REPR_ATTACH_01.jpg","_MF_REPR_ATTACH_01.gif","_MF_ATTACH_01.JPG","_MF_REPR_ATTACH_01.JPG"};

    // getUrl 함수. 식물 코드를 input으로 받아서 일치하는 url을 return 한다.
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


    //비동기적 처리 방식으로 url에 연결.
    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String no_img_url = "http://www.nongsaro.go.kr/cms_contents/301/";
            Document doc = null;

            try {
                //Jsoup를 이용한 url 연결
                doc = Jsoup.connect(no_img_url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //그림 정보가 없는 페이지에서의 타이틀 가져오기. False flag
            String no_img_title = doc.title();

            String url_head = "http://www.nongsaro.go.kr/cms_contents/301/"+params[0];
            String url="";
            String title="";

            //서버에 저장되어 있는 파일 이름을 하나씩 집어 넣어 이미지를 가져올 수 있는 지 확인
            for(String url_tail : url_tails) {
                url = url_head+url_tail;
                title="";
                try {
                    doc = Jsoup.connect(url).get();
                    title = doc.title();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //false flag와 비교하여 다르면 해당 url return
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


