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

import com.ariaramin.radioava.Adapters.Album.AlbumAdapter;
import com.ariaramin.radioava.Adapters.Artist.ArtistAdapter;
import com.ariaramin.radioava.Adapters.Music.MusicAdapter;
import com.ariaramin.radioava.Adapters.Music.SliderAdapter;
import com.ariaramin.radioava.Adapters.Video.VideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentBrowseBinding;
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

        return browseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLatestMusics();
        getPopularVideos();
        getLatestAlbums();
        getTopArtist();
    }

    private void getTopArtist() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .map(this::sortByFollowers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Throwable {

                        if (!artists.isEmpty()) {
                            List<Artist> topArtists = artists.subList(0, 15);

                            if (browseBinding.mustFollowRecyclerView.getAdapter() == null) {
                                ArtistAdapter artistAdapter = new ArtistAdapter(topArtists);
                                browseBinding.mustFollowRecyclerView.setAdapter(artistAdapter);
                            } else {
                                ArtistAdapter adapter = (ArtistAdapter) browseBinding.mustFollowRecyclerView.getAdapter();
                                adapter.updateList(topArtists);
                            }
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

    private List<Artist> sortByFollowers(List<Artist> artists) {
        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist o1, Artist o2) {
                int o2Followers = convertStringToInt(o2.getFollowers());
                int o1Followers = convertStringToInt(o1.getFollowers());
                return Integer.compare(o2Followers, o1Followers);
            }
        });
        return artists;
    }

    private void getLatestAlbums() {
        Disposable disposable = mainViewModel.getAllAlbumsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Album>>() {
                    @Override
                    public void accept(List<Album> albums) throws Throwable {

                        if (!albums.isEmpty()) {
                            List<Album> latestAlbums = albums.subList(0, 15);

                            if (browseBinding.latestAlbumsRecyclerView.getAdapter() == null) {
                                AlbumAdapter albumAdapter = new AlbumAdapter(latestAlbums);
                                browseBinding.latestAlbumsRecyclerView.setAdapter(albumAdapter);
                            } else {
                                AlbumAdapter adapter = (AlbumAdapter) browseBinding.latestAlbumsRecyclerView.getAdapter();
                                adapter.updateList(latestAlbums);
                            }
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
                .map(this::sortByLikes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {

                        if (!videos.isEmpty()) {
                            List<Video> popularVideos = videos.subList(0, 10);

                            if (browseBinding.popularVideosRecyclerView.getAdapter() == null) {
                                VideoAdapter videoAdapter = new VideoAdapter(popularVideos);
                                browseBinding.popularVideosRecyclerView.setAdapter(videoAdapter);
                            } else {
                                VideoAdapter adapter = (VideoAdapter) browseBinding.popularVideosRecyclerView.getAdapter();
                                adapter.updateList(popularVideos);
                            }

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

    private List<Video> sortByLikes(List<Video> videos) {
        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                int o1Likes = convertStringToInt(o1.getLikes());
                int o2Likes = convertStringToInt(o2.getLikes());
                return Integer.compare(o2Likes, o1Likes);
            }
        });
        return videos;
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

    private void getLatestMusics() {
        Disposable disposable = mainViewModel.getAllMusicsFromDb()
                .map(this::getLatestMusics)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {

                        if (!musics.isEmpty()) {
                            List<Music> topThreeMusics = musics.subList(0, 3);
                            List<Music> latestMusics = musics.subList(3, 18);

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

    private ArrayList<Music> getLatestMusics(List<Music> musicList) {
        ArrayList<Music> latest = new ArrayList<>();
        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getAlbum() == null) {
                latest.add(musicList.get(i));
            }
        }
        return latest;
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
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(300, 300)
                    .into(imageViews.get(i));

            imageViews.get(i).setColorFilter(filter);
        }
    }
}