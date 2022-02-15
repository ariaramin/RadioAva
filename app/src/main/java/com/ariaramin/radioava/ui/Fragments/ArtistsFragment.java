package com.ariaramin.radioava.ui.Fragments;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        artistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artists, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        getAllArtists();
        return artistsBinding.getRoot();
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
                            List<Artist> topArtists = artists.subList(4, 34);

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
                                VerticalArtistAdapter artistAdapter = new VerticalArtistAdapter(topArtists);
                                artistsBinding.topArtistsRecyclerView.setAdapter(artistAdapter);
                            } else {
                                VerticalArtistAdapter adapter = (VerticalArtistAdapter) artistsBinding.topArtistsRecyclerView.getAdapter();
                                adapter.updateList(topArtists);
                            }
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
                int o2Plays = convertStringToInt(o2.getPlays());
                int o1Plays = convertStringToInt(o1.getPlays());
                return Integer.compare(o2Plays, o1Plays);
            }
        });
        return artists;
    }

    private int convertStringToInt(String string) {
        if (string.contains("B")) {
            String str = string.replace("B", "");
            return (int) Double.parseDouble(str);
        } else if (string.contains("M")) {
            String str = string.replace("M", "");
            return (int) Double.parseDouble(str);
        } else if (string.contains("K")) {
            String str = string.replace("K", "");
            return (int) Double.parseDouble(str);
        }
        return (int) Double.parseDouble(string);
    }

    private void setCurrentArtist(int position) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(artistsBinding.firstArtistImageView);
        imageViews.add(artistsBinding.secondArtistImageView);
        imageViews.add(artistsBinding.thirdArtistImageView);
        imageViews.add(artistsBinding.fourthArtistImageView);

        ArrayList<MaterialCardView> cardViews = new ArrayList<>();
        cardViews.add(artistsBinding.firstArtistCardView);
        cardViews.add(artistsBinding.secondArtistCardView);
        cardViews.add(artistsBinding.thirdArtistCardView);
        cardViews.add(artistsBinding.fourthArtistCardView);

        ColorMatrix matrix = new ColorMatrix();

        for (int i = 0; i < imageViews.size(); i++) {
            if (i == position) {
                matrix.setSaturation(1);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageViews.get(i).setColorFilter(filter);
                cardViews.get(i).setStrokeWidth(3);
            } else {
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageViews.get(i).setColorFilter(filter);
                cardViews.get(i).setStrokeWidth(0);
            }
        }
    }

    private void setTopArtistsImage(List<Artist> artists) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(artistsBinding.firstArtistImageView);
        imageViews.add(artistsBinding.secondArtistImageView);
        imageViews.add(artistsBinding.thirdArtistImageView);
        imageViews.add(artistsBinding.fourthArtistImageView);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        for (int i = 0; i < imageViews.size(); i++) {
            Glide.with(artistsBinding.getRoot().getContext())
                    .load(artists.get(i).getImage())
                    .thumbnail(
                            Glide.with(artistsBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(300, 300)
                    .into(imageViews.get(i));

            imageViews.get(i).setColorFilter(filter);
        }
    }

}