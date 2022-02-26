package com.ariaramin.radioava.ui.Fragments;

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

import com.ariaramin.radioava.Adapters.Artist.ArtistAdapter;
import com.ariaramin.radioava.Adapters.Music.MusicAdapter;
import com.ariaramin.radioava.Adapters.Music.SliderAdapter;
import com.ariaramin.radioava.Adapters.Video.VideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentHomeBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    FragmentHomeBinding homeBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    private static final String TAG = "home";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        homeBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment);
            }
        });
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrendingMusics();
        getTrendingVideos();
        getPopularArtists();
        getPopularMusics();
        setupNavigation();
    }

    private void setupNavigation() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.seeMoreTrendingMusicsTextView || v.getId() == R.id.seeMorePopularMusicsTextView) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_allMusicsFragment);
                } else if (v.getId() == R.id.seeMoreVideosTextView) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_allVideosFragment);
                }
            }
        };
        homeBinding.seeMoreTrendingMusicsTextView.setOnClickListener(clickListener);
        homeBinding.seeMorePopularMusicsTextView.setOnClickListener(clickListener);
        homeBinding.seeMoreVideosTextView.setOnClickListener(clickListener);
    }

    private void getPopularArtists() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Throwable {

                        if (!artists.isEmpty()) {
                            List<Artist> popularArtists = artists.subList(0, 15);

                            if (homeBinding.popularArtistsRecyclerView.getAdapter() == null) {
                                ArtistAdapter artistAdapter = new ArtistAdapter(popularArtists, TAG);
                                homeBinding.popularArtistsRecyclerView.setAdapter(artistAdapter);
                            } else {
                                ArtistAdapter adapter = (ArtistAdapter) homeBinding.popularArtistsRecyclerView.getAdapter();
                                adapter.updateList(popularArtists);
                            }
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

    private void getPopularMusics() {
        Disposable disposable = mainViewModel.getPopularMusicsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {

                        if (!musics.isEmpty()) {
                            List<Music> popularMusics = musics.subList(0, 15);

                            if (homeBinding.mustListenRecyclerView.getAdapter() == null) {
                                MusicAdapter musicAdapter = new MusicAdapter(popularMusics, TAG);
                                homeBinding.mustListenRecyclerView.setAdapter(musicAdapter);
                            } else {
                                MusicAdapter adapter = (MusicAdapter) homeBinding.mustListenRecyclerView.getAdapter();
                                adapter.updateList(popularMusics);
                            }

                        }

                        if (musics.isEmpty()) {
                            homeBinding.mustListenSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            homeBinding.mustListenSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getTrendingVideos() {
        Disposable disposable = mainViewModel.getTrendingVideosFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
                        if (!videos.isEmpty()) {
                            List<Video> latestVideos = videos.subList(0, 10);

                            if (homeBinding.newVideosRecyclerView.getAdapter() == null) {
                                VideoAdapter videoAdapter = new VideoAdapter(latestVideos);
                                homeBinding.newVideosRecyclerView.setAdapter(videoAdapter);
                            } else {
                                VideoAdapter adapter = (VideoAdapter) homeBinding.newVideosRecyclerView.getAdapter();
                                adapter.updateList(latestVideos);
                            }
                        }

                        if (videos.isEmpty()) {
                            homeBinding.newVideosSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            homeBinding.newVideosSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getTrendingMusics() {
        Disposable disposable = mainViewModel.getTrendingMusicsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {

                        if (!musics.isEmpty()) {
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
                                MusicAdapter musicAdapter = new MusicAdapter(topTrending, TAG);
                                homeBinding.trendingMusicsRecyclerView.setAdapter(musicAdapter);
                            } else {
                                MusicAdapter adapter = (MusicAdapter) homeBinding.trendingMusicsRecyclerView.getAdapter();
                                adapter.updateList(topTrending);
                            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.clear();
    }
}