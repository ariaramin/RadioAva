package com.ariaramin.radioava.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Artist.ArtistWorkPagerAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentDetailArtistBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DetailArtistFragment extends Fragment {

    FragmentDetailArtistBinding detailArtistBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    Artist artist;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailArtistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_artist, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        Bundle args = getArguments();
        artist = args.getParcelable("Artist");
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        detailArtistBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        setupDetail();
        setupTabLayout();
        return detailArtistBinding.getRoot();
    }

    private void setupDetail() {
        Glide.with(detailArtistBinding.getRoot().getContext())
                .load(artist.getImage())
                .thumbnail(
                        Glide.with(detailArtistBinding.getRoot().getContext())
                                .load(R.drawable.loading)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(detailArtistBinding.detailArtistImageView);
        detailArtistBinding.detailArtistNameTextView.setText(artist.getName());
        mainViewModel.getArtistTotalMusics().observe(requireActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                detailArtistBinding.detailArtistMusicCountTextView.setText(integer.toString());
            }
        });
        detailArtistBinding.detailArtistFollowersTextView.setText(artist.getFollowers());
        detailArtistBinding.detailArtistPlaysTextView.setText(artist.getPlaysCount());
    }

    private void setupTabLayout() {
        ArtistWorkPagerAdapter adapter = new ArtistWorkPagerAdapter(this, artist);
        detailArtistBinding.detailArtistViewPager.setAdapter(adapter);

        new TabLayoutMediator(detailArtistBinding.detailArtistTabLayout, detailArtistBinding.detailArtistViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String title;
                if (position == 0) {
                    title = requireContext().getResources().getString(R.string.musics);
                } else if (position == 1) {
                    title = requireContext().getResources().getString(R.string.albums);
                } else {
                    title = requireContext().getResources().getString(R.string.videos);
                }
                tab.setText(title);
            }
        }).attach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
    }
}