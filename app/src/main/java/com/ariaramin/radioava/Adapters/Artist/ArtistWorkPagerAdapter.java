package com.ariaramin.radioava.Adapters.Artist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.ui.Fragments.ArtistWorksFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ArtistWorkPagerAdapter extends FragmentStateAdapter {

    Artist artist;

    public ArtistWorkPagerAdapter(@NonNull Fragment fragment, Artist artist) {
        super(fragment);
        this.artist = artist;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ArtistWorksFragment fragment = new ArtistWorksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("Artist", artist);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
