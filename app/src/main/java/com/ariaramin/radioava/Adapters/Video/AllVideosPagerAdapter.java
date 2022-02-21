package com.ariaramin.radioava.Adapters.Video;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ariaramin.radioava.ui.Fragments.Video.VideosListFragment;

public class AllVideosPagerAdapter extends FragmentStateAdapter {

    public AllVideosPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        VideosListFragment fragment = new VideosListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
