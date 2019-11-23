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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rank_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 4;
        o.inDither = false;

        Log.d("IMAGERESOURCE", imageResource);

        GetBitmapImageFromUrl imgGetter = new GetBitmapImageFromUrl();

        bitmap = imgGetter.getImageBitmap(imageResource);
        Log.d("Origin WIDTH",String.valueOf(bitmap.getWidth()));
        Log.d("Origin Height",String.valueOf(bitmap.getHeight()));

        bitmap = imgGetter.getImageBitmap(imageResource, o);
        Log.d("WIDTH",String.valueOf(bitmap.getWidth()));
        Log.d("Height",String.valueOf(bitmap.getHeight()));
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        bitmap = null;
    }
}
