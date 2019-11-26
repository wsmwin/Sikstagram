package com.example.sikstagram.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sikstagram.Fragments.RankGalleryFragment;

import java.util.List;

// 후보 top 5 사진을 가져오는 어댑터
public class RankGalleryAdapter extends FragmentStatePagerAdapter {

    public List<String> images;

    // 생성자
    public RankGalleryAdapter(FragmentManager fm) {
        super(fm);
    }

    // 후보 이미지 리스트를 받아줌
    public void setRankGalleryAdapter(List<String> imagesList) {
        this.images = imagesList;
    }

    // fragment에서 url을 통해 이미지를 가져옴.
    @Override
    public Fragment getItem(int position) {
        return RankGalleryFragment.getInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}