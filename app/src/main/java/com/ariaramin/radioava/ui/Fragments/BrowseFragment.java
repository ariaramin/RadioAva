package com.ariaramin.radioava.ui.Fragments;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ariaramin.radioava.Adapters.AlbumAdapter;
import com.ariaramin.radioava.Adapters.ArtistAdapter;
import com.ariaramin.radioava.Adapters.ArtistSliderAdapter;
import com.ariaramin.radioava.Adapters.MusicAdapter;
import com.ariaramin.radioava.Adapters.SliderAdapter;
import com.ariaramin.radioava.Adapters.VideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;
import com.ariaramin.radioava.Room.Entities.AllVideoEntity;
import com.ariaramin.radioava.Room.Entities.TrendingVideoEntity;
import com.ariaramin.radioava.databinding.FragmentBrowseBinding;
import com.bumptech.glide.Glide;
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


public class BrowseFragment extends Fragment {

    FragmentBrowseBinding browseBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        browseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        getLatestMusics();
        getPopularVideos();
        getLatestAlbums();
        getTopArtist();
        return browseBinding.getRoot();
    }

    private void getTopArtist() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllArtistEntity>() {
                    @Override
                    public void accept(AllArtistEntity allArtistEntity) throws Throwable {
                        List<Artist> artists = allArtistEntity.getArtist().subList(0, 15);

                        Collections.sort(artists, new Comparator<Artist>() {
                            @Override
                            public int compare(Artist o1, Artist o2) {
                                int o2Followers = convertStringToInt(o2.getFollowers());
                                int o1Followers = convertStringToInt(o1.getFollowers());
                                return Integer.compare(o2Followers, o1Followers);
                            }
                        });

                        if (browseBinding.mustFollowRecyclerView.getAdapter() == null) {
                            ArtistAdapter artistAdapter = new ArtistAdapter(artists);
                            browseBinding.mustFollowRecyclerView.setAdapter(artistAdapter);
                        } else {
                            ArtistAdapter adapter = (ArtistAdapter) browseBinding.mustFollowRecyclerView.getAdapter();
                            adapter.updateList(artists);
                        }

                        if (artists.isEmpty()) {
                            browseBinding.mustFollowSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            browseBinding.mustFollowSpinKit.setVisibility(View.GONE);
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

                        if (browseBinding.latestAlbumsRecyclerView.getAdapter() == null) {
                            AlbumAdapter albumAdapter = new AlbumAdapter(albums);
                            browseBinding.latestAlbumsRecyclerView.setAdapter(albumAdapter);
                        } else {
                            AlbumAdapter adapter = (AlbumAdapter) browseBinding.latestAlbumsRecyclerView.getAdapter();
                            adapter.updateList(albums);
                        }

                        if (albums.isEmpty()) {
                            browseBinding.latestAlbumsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            browseBinding.latestAlbumsSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getPopularVideos() {
        Disposable disposable = mainViewModel.getAllVideosFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllVideoEntity>() {
                    @Override
                    public void accept(AllVideoEntity allVideoEntity) throws Throwable {
                        List<Video> videos = allVideoEntity.getVideos().subList(0, 4);

                        Collections.sort(videos, new Comparator<Video>() {
                            @Override
                            public int compare(Video o1, Video o2) {
                                int o1Likes = convertStringToInt(o1.getLikes());
                                int o2Likes = convertStringToInt(o2.getLikes());
                                return Integer.compare(o2Likes, o1Likes);
                            }
                        });

                        if (browseBinding.popularVideosRecyclerView.getAdapter() == null) {
                            VideoAdapter videoAdapter = new VideoAdapter(videos);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false);
                            browseBinding.popularVideosRecyclerView.setAdapter(videoAdapter);
                            browseBinding.popularVideosRecyclerView.setLayoutManager(gridLayoutManager);
                        } else {
                            VideoAdapter adapter = (VideoAdapter) browseBinding.popularVideosRecyclerView.getAdapter();
                            adapter.updateList(videos);
                        }

                        if (videos.isEmpty()) {
                            browseBinding.popularVideosSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            browseBinding.popularVideosSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private int convertStringToInt(String likes) {
        if (likes.contains("K")) {
            String str = likes.replace("K", "");
            return (int) Double.parseDouble(str);
        } else if (likes.contains("M")) {
            String str = likes.replace("M", "");
            return (int) Double.parseDouble(str);
        }
        return (int) Double.parseDouble(likes);
    }

    private void getLatestMusics() {
        Disposable disposable = mainViewModel.getAllMusicsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllMusicEntity>() {
                    @Override
                    public void accept(AllMusicEntity allMusicEntity) throws Throwable {
                        List<Music> musics = allMusicEntity.getMusics();
                        List<Music> topThreeMusics = allMusicEntity.getMusics().subList(0, 3);
                        List<Music> latestMusics = allMusicEntity.getMusics().subList(3, 18);

                        setTopMusicsImage(topThreeMusics);

                        if (browseBinding.latestMusicsSliderView.getSliderAdapter() == null) {
                            SliderAdapter sliderAdapter = new SliderAdapter(topThreeMusics);
                            browseBinding.latestMusicsSliderView.setSliderAdapter(sliderAdapter);
                        } else {
                            SliderAdapter sliderAdapter = (SliderAdapter) browseBinding.latestMusicsSliderView.getSliderAdapter();
                            sliderAdapter.updateList(topThreeMusics);
                        }
                        browseBinding.latestMusicsSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                        browseBinding.latestMusicsSliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                        browseBinding.latestMusicsSliderView.setScrollTimeInSec(5);
                        browseBinding.latestMusicsSliderView.startAutoCycle();
                        browseBinding.latestMusicsSliderView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                            @Override
                            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                setCurrentMusic(browseBinding.latestMusicsSliderView.getCurrentPagePosition());
                            }
                        });

                        if (browseBinding.latestMusicsRecyclerView.getAdapter() == null) {
                            MusicAdapter musicAdapter = new MusicAdapter(latestMusics);
                            browseBinding.latestMusicsRecyclerView.setAdapter(musicAdapter);
                        } else {
                            MusicAdapter adapter = (MusicAdapter) browseBinding.latestMusicsRecyclerView.getAdapter();
                            adapter.updateList(latestMusics);
                        }

                        if (musics.isEmpty()) {
                            browseBinding.latestMusicSliderSpinKit.setVisibility(View.VISIBLE);
                            browseBinding.latestMusicSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            browseBinding.latestMusicSliderSpinKit.setVisibility(View.GONE);
                            browseBinding.latestMusicSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setCurrentMusic(int position) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(browseBinding.firstMusicImageView);
        imageViews.add(browseBinding.secondMusicImageView);
        imageViews.add(browseBinding.thirdMusicImageView);

        ArrayList<MaterialCardView> cardViews = new ArrayList<>();
        cardViews.add(browseBinding.firstMusicCardView);
        cardViews.add(browseBinding.secondMusicCardView);
        cardViews.add(browseBinding.thirdMusicCardView);

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

    private void setTopMusicsImage(List<Music> musics) {
        ArrayList<ImageView> imageViews = new ArrayList<>();
        imageViews.add(browseBinding.firstMusicImageView);
        imageViews.add(browseBinding.secondMusicImageView);
        imageViews.add(browseBinding.thirdMusicImageView);

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        for (int i = 0; i < imageViews.size(); i++) {
            Glide.with(browseBinding.getRoot().getContext())
                    .load(musics.get(i).getCover())
                    .thumbnail(
                            Glide.with(browseBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageViews.get(i));

            imageViews.get(i).setColorFilter(filter);
        }
    }
}