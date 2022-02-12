package com.ariaramin.radioava.ui.Fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.AlbumAdapter;
import com.ariaramin.radioava.Adapters.ArtistAdapter;
import com.ariaramin.radioava.Adapters.MusicAdapter;
import com.ariaramin.radioava.Adapters.SliderAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingMusicEntity;
import com.ariaramin.radioava.databinding.FragmentHomeBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        getTrendingMusics();
        getLatestAlbums();
        getPopularArtists();
        return homeBinding.getRoot();
    }

    private void getPopularArtists() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllArtistEntity>() {
                    @Override
                    public void accept(AllArtistEntity allArtistEntity) throws Throwable {
                        List<Artist> artists = allArtistEntity.getArtist().subList(0, 15);

                        if (homeBinding.popularArtistsRecyclerView.getAdapter() == null) {
                            ArtistAdapter artistAdapter = new ArtistAdapter(artists);
                            homeBinding.popularArtistsRecyclerView.setAdapter(artistAdapter);
                        } else {
                            ArtistAdapter adapter = (ArtistAdapter) homeBinding.popularArtistsRecyclerView.getAdapter();
                            adapter.updateList(artists);
                        }

                        if (artists.isEmpty()) {
                            homeBinding.popularArtistsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            homeBinding.popularArtistsSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getLatestAlbums() {
        Disposable disposable = mainViewModel.getAllAlbumsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllAlbumEntity>() {
                    @Override
                    public void accept(AllAlbumEntity allAlbumEntity) throws Throwable {
                        List<Album> albums = allAlbumEntity.getAlbums().subList(0, 15);


                        if (homeBinding.latestAlbumsRecyclerView.getAdapter() == null) {
                            AlbumAdapter albumAdapter = new AlbumAdapter(albums);
                            homeBinding.latestAlbumsRecyclerView.setAdapter(albumAdapter);
                        } else {
                            AlbumAdapter adapter = (AlbumAdapter) homeBinding.latestAlbumsRecyclerView.getAdapter();
                            adapter.updateList(albums);
                        }

                        if (albums.isEmpty()) {
                            homeBinding.latestAlbumsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            homeBinding.latestAlbumsSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getTrendingMusics() {
        Disposable disposable = mainViewModel.getTrendingMusicsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrendingMusicEntity>() {
                    @Override
                    public void accept(TrendingMusicEntity trendingMusicEntity) throws Throwable {
                        List<Music> musics = trendingMusicEntity.getMusics();
                        List<Music> topFiveTrending = musics.subList(0, 5);
                        List<Music> topTrending = musics.subList(5, 20);

                        if (homeBinding.sliderView.getSliderAdapter() == null) {
                            SliderAdapter sliderAdapter = new SliderAdapter(topFiveTrending);
                            homeBinding.sliderView.setSliderAdapter(sliderAdapter);
                        } else {
                            SliderAdapter sliderAdapter = (SliderAdapter) homeBinding.sliderView.getSliderAdapter();
                            sliderAdapter.updateList(topFiveTrending);
                        }
                        homeBinding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        homeBinding.sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                        homeBinding.sliderView.setScrollTimeInSec(5);
                        homeBinding.sliderView.startAutoCycle();

                        if (homeBinding.trendingMusicsRecyclerView.getAdapter() == null) {
                            MusicAdapter musicAdapter = new MusicAdapter(topTrending);
                            homeBinding.trendingMusicsRecyclerView.setAdapter(musicAdapter);
                        } else {
                            MusicAdapter adapter = (MusicAdapter) homeBinding.trendingMusicsRecyclerView.getAdapter();
                            adapter.updateList(topTrending);
                        }

                        if (musics.isEmpty()) {
                            homeBinding.sliderSpinKit.setVisibility(View.VISIBLE);
                            homeBinding.trendingMusicsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            homeBinding.sliderSpinKit.setVisibility(View.GONE);
                            homeBinding.trendingMusicsSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }
}