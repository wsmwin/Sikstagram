package com.example.sikstagram.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sikstagram.Fragments.RankGalleryFragment;

import java.util.List;

public class RankGalleryAdapter extends FragmentStatePagerAdapter {

    private List<String> images;

    public RankGalleryAdapter(FragmentManager fm, List<String> imagesList) {
        super(fm);
        this.images = imagesList;
    }

    @Override
    public Fragment getItem(int position) {
        return RankGalleryFragment.getInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}