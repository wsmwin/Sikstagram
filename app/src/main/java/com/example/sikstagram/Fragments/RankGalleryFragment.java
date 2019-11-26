package com.example.sikstagram.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sikstagram.GetBitmapImageFromUrl;
import com.example.sikstagram.R;

public class RankGalleryFragment extends Fragment {

    private String imageResource;
    private Bitmap bitmap;

    // 이미지 url을 받으면 이미지로 가득 찬 fragment 리턴
    public static RankGalleryFragment getInstance(String resourceID) {
        RankGalleryFragment f = new RankGalleryFragment();
        Bundle args = new Bundle();
        args.putString("image_source", resourceID);
        f.setArguments(args);
        return f;
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageResource = getArguments().getString("image_source");
    }

    // fragment 생성 시 이미지를 가져옴
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rank_fragment, container, false);
    }

    //rank_fragmetn.xml 파일에 비트 맵 이미지를 띄우는 함수
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        //비트맵 옵션. 크기를 1/4로 줄이고 가져오는 속도 향상
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 6;
        o.inDither = false;

        Log.d("IMAGERESOURCE", imageResource);

        GetBitmapImageFromUrl imgGetter = new GetBitmapImageFromUrl();

        bitmap = imgGetter.getImageBitmap(imageResource, o);
        imageView.setImageBitmap(bitmap);
    }

    //onDestroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
    }
}
