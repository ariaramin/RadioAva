package com.ariaramin.radioava.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Album.VerticalAlbumAdapter;
import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;
import com.ariaramin.radioava.databinding.FragmentLikedBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class LikedFragment extends Fragment {

    FragmentLikedBinding likedBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<String> liked;
    ArrayList<Music> likedMusics = new ArrayList<>();
    ArrayList<Album> likedAlbums = new ArrayList<>();
    ArrayList<Video> likedVideos = new ArrayList<>();
    boolean isMusicEmpty = false;
    boolean isAlbumEmpty = false;
    boolean isVideoEmpty = false;
    private static final String TAG = "liked";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        likedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_liked, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        likedBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());
        liked = sharedPreferenceManager.readLikedData();
        getLikedMusics();
        getLikedAlbums();
        getLikedVideos();

        return likedBinding.getRoot();
    }

    private void getLikedMusics() {
        Disposable disposable = mainViewModel.getAllMusics()
                .map(musics -> {
                    likedMusics.clear();
                    for (int i = 0; i < liked.size(); i++) {
                        for (int j = 0; j < musics.size(); j++) {
                            if (liked.get(i).equals(musics.get(j).getId() + musics.get(j).getName())) {
                                likedMusics.add(musics.get(j));
                            }
                        }
                    }
                    return likedMusics;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<Music>>) musics -> {
                    Collections.reverse(musics);

                    if (likedBinding.likedMusicsRecyclerView.getAdapter() == null) {
                        VerticalMusicAdapter adapter = new VerticalMusicAdapter(musics, TAG);
                        likedBinding.likedMusicsRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalMusicAdapter adapter = (VerticalMusicAdapter) likedBinding.likedMusicsRecyclerView.getAdapter();
                        adapter.updateList(musics);
                    }

                    if (musics.isEmpty()) {
                        likedBinding.musicsLayout.setVisibility(View.GONE);
                        isMusicEmpty = true;
                    } else {
                        likedBinding.musicsLayout.setVisibility(View.VISIBLE);
                        isMusicEmpty = false;
                    }
                    checkIsEmpty();
                });
        compositeDisposable.add(disposable);
    }

    private void getLikedAlbums() {
        Disposable disposable = mainViewModel.getAllAlbums()
                .map(albums -> {
                    likedAlbums.clear();
                    for (int i = 0; i < liked.size(); i++) {
                        for (int j = 0; j < albums.size(); j++) {
                            if (liked.get(i).equals(albums.get(j).getId() + albums.get(j).getName())) {
                                likedAlbums.add(albums.get(j));
                            }
                        }
                    }
                    return likedAlbums;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<Album>>) albums -> {
                    Collections.reverse(albums);

                    if (likedBinding.likedAlbumsRecyclerView.getAdapter() == null) {
                        VerticalAlbumAdapter adapter = new VerticalAlbumAdapter(albums, TAG);
                        likedBinding.likedAlbumsRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalAlbumAdapter adapter = (VerticalAlbumAdapter) likedBinding.likedAlbumsRecyclerView.getAdapter();
                        adapter.updateList(albums);
                    }

                    if (albums.isEmpty()) {
                        likedBinding.albumsLayout.setVisibility(View.GONE);
                        isAlbumEmpty = true;
                    } else {
                        likedBinding.albumsLayout.setVisibility(View.VISIBLE);
                        isAlbumEmpty = false;
                    }
                    checkIsEmpty();
                });
        compositeDisposable.add(disposable);
    }

    private void getLikedVideos() {
        Disposable disposable = mainViewModel.getAllVideos()
                .map(videos -> {
                    likedVideos.clear();
                    for (int i = 0; i < liked.size(); i++) {
                        for (int j = 0; j < videos.size(); j++) {
                            if (liked.get(i).equals(videos.get(j).getId() + videos.get(j).getName())) {
                                likedVideos.add(videos.get(j));
                            }
                        }
                    }
                    return likedVideos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<Video>>) videos -> {
                    Collections.reverse(videos);

                    if (likedBinding.likedVideosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter adapter = new VerticalVideoAdapter(videos, TAG);
                        likedBinding.likedVideosRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) likedBinding.likedVideosRecyclerView.getAdapter();
                        adapter.updateList(videos);
                    }

                    if (videos.isEmpty()) {
                        likedBinding.videosLayout.setVisibility(View.GONE);
                        isVideoEmpty = true;
                    } else {
                        likedBinding.videosLayout.setVisibility(View.VISIBLE);
                        isVideoEmpty = false;
                    }
                    checkIsEmpty();
                });
        compositeDisposable.add(disposable);
    }

    private void checkIsEmpty() {
        if (isMusicEmpty && isAlbumEmpty && isVideoEmpty) {
            likedBinding.notFoundTextView.setVisibility(View.VISIBLE);
        } else {
            likedBinding.notFoundTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}