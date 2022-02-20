package com.ariaramin.radioava.ui.Fragments;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ariaramin.radioava.Adapters.Artist.ArtistSliderAdapter;
import com.ariaramin.radioava.Adapters.Artist.VerticalArtistAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentArtistsBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.card.MaterialCardView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtistsFragment extends Fragment {

    FragmentArtistsBinding artistsBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    private static final String TAG = "artists";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        artistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artists, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        artistsBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_artistsFragment_to_searchFragment);
            }
        });

        return artistsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllArtists();
    }

    private void getAllArtists() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .map(this::sortByPlays)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Throwable {

                        if (!artists.isEmpty()) {
                            List<Artist> topFourArtists = artists.subList(0, 4);
                            List<Artist> topArtists = artists.subList(4, 19);

                            setTopArtistsImage(topFourArtists);

                            if (artistsBinding.artistSliderView.getSliderAdapter() == null) {
                                ArtistSliderAdapter sliderAdapter = new ArtistSliderAdapter(topFourArtists);
                                artistsBinding.artistSliderView.setSliderAdapter(sliderAdapter);
                            } else {
                                ArtistSliderAdapter sliderAdapter = (ArtistSliderAdapter) artistsBinding.artistSliderView.getSliderAdapter();
                                sliderAdapter.updateList(topFourArtists);
                            }
                            artistsBinding.artistSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            artistsBinding.artistSliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            artistsBinding.artistSliderView.setScrollTimeInSec(5);
                            artistsBinding.artistSliderView.startAutoCycle();
                            artistsBinding.artistSliderView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                @Override
                                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                    setCurrentArtist(artistsBinding.artistSliderView.getCurrentPagePosition());
                                }
                            });

                            if (artistsBinding.topArtistsRecyclerView.getAdapter() == null) {
                                VerticalArtistAdapter artistAdapter = new VerticalArtistAdapter(topArtists, TAG);
                                artistsBinding.topArtistsRecyclerView.setAdapter(artistAdapter);
                            } else {
                                VerticalArtistAdapter adapter = (VerticalArtistAdapter) artistsBinding.topArtistsRecyclerView.getAdapter();
                                adapter.updateList(topArtists);
                            }
                            artistsBinding.topArtistsRecyclerView.setHasFixedSize(false);
                        }

                        if (artists.isEmpty()) {
                            artistsBinding.artistSliderSpinKit.setVisibility(View.VISIBLE);
                            artistsBinding.topArtistSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            artistsBinding.artistSliderSpinKit.setVisibility(View.GONE);
                            artistsBinding.topArtistSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private List<Artist> sortByPlays(List<Artist> artists) {
        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                return Integer.compare(o2.getPlays(), o1.getPlays());
            }
        });
        return artists;
    }

    private void setCurrentArtist(int position) {
        ImageView[] imageViews = {
                artistsBinding.firstArtistImageView,
                artistsBinding.secondArtistImageView,
                artistsBinding.thirdArtistImageView,
                artistsBinding.fourthArtistImageView
        };

        MaterialCardView[] cardViews = {
                artistsBinding.firstArtistCardView,
                artistsBinding.secondArtistCardView,
                artistsBinding.thirdArtistCardView,
                artistsBinding.fourthArtistCardView
        };

        ColorMatrix matrix = new ColorMatrix();

        for (int i = 0; i < imageViews.length; i++) {
            if (i == position) {
                matrix.setSaturation(1);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageViews[i].setColorFilter(filter);
                cardViews[i].setStrokeWidth(3);
            } else {
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageViews[i].setColorFilter(filter);
                cardViews[i].setStrokeWidth(0);
            }
        }
    }

    private void setTopArtistsImage(List<Artist> artists) {
        ImageView[] imageViews = {
                artistsBinding.firstArtistImageView,
                artistsBinding.secondArtistImageView,
                artistsBinding.thirdArtistImageView,
                artistsBinding.fourthArtistImageView
        };

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        for (int i = 0; i < imageViews.length; i++) {
            Glide.with(artistsBinding.getRoot().getContext())
                    .load(artists.get(i).getImage())
                    .thumbnail(
                            Glide.with(artistsBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(200, 200)
                    .into(imageViews[i]);

            imageViews[i].setColorFilter(filter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}