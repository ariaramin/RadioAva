package com.ariaramin.radioava.ui.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Album.VerticalAlbumAdapter;
import com.ariaramin.radioava.Adapters.Artist.VerticalArtistAdapter;
import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentSearchBinding;

import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchFragment extends Fragment {

    FragmentSearchBinding searchBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    boolean isMusicsEmpty = false;
    boolean isAlbumsEmpty = false;
    boolean isArtistsEmpty = false;
    boolean isVideosEmpty = false;
    private static final String TAG = "search";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        searchBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        search();
        return searchBinding.getRoot();
    }

    private void search() {
        searchBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMusics(s.toString().toLowerCase(Locale.ROOT));
                searchAlbums(s.toString().toLowerCase(Locale.ROOT));
                searchArtists(s.toString().toLowerCase(Locale.ROOT));
                searchVideos(s.toString().toLowerCase(Locale.ROOT));

                if (isMusicsEmpty && isAlbumsEmpty && isArtistsEmpty && isVideosEmpty) {
                    searchBinding.notFoundTextView.setVisibility(View.VISIBLE);
                } else {
                    searchBinding.notFoundTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchMusics(String query) {
        Disposable disposable = mainViewModel.searchInMusicsFromDb(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {
                        if (searchBinding.searchedMusicsRecyclerView.getAdapter() == null) {
                            VerticalMusicAdapter musicAdapter = new VerticalMusicAdapter(musics);
                            searchBinding.searchedMusicsRecyclerView.setAdapter(musicAdapter);
                        } else {
                            VerticalMusicAdapter musicAdapter = (VerticalMusicAdapter) searchBinding.searchedMusicsRecyclerView.getAdapter();
                            musicAdapter.updateList(musics);
                        }

                        if (musics.isEmpty()) {
                            searchBinding.musicsLayout.setVisibility(View.GONE);
                            isMusicsEmpty = true;
                        } else {
                            searchBinding.musicsLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void searchAlbums(String query) {
        Disposable disposable = mainViewModel.searchInAlbumsFromDb(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Album>>() {
                    @Override
                    public void accept(List<Album> albums) throws Throwable {
                        if (searchBinding.searchedAlbumsRecyclerView.getAdapter() == null) {
                            VerticalAlbumAdapter albumAdapter = new VerticalAlbumAdapter(albums);
                            searchBinding.searchedAlbumsRecyclerView.setAdapter(albumAdapter);
                        } else {
                            VerticalAlbumAdapter albumAdapter = (VerticalAlbumAdapter) searchBinding.searchedAlbumsRecyclerView.getAdapter();
                            albumAdapter.updateList(albums);
                        }

                        if (albums.isEmpty()) {
                            searchBinding.albumsLayout.setVisibility(View.GONE);
                            isAlbumsEmpty = true;
                        } else {
                            searchBinding.albumsLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void searchArtists(String query) {
        Disposable disposable = mainViewModel.searchInArtistsFromDb(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Throwable {
                        if (searchBinding.searchedArtistsRecyclerView.getAdapter() == null) {
                            VerticalArtistAdapter artistAdapter = new VerticalArtistAdapter(artists, TAG);
                            searchBinding.searchedArtistsRecyclerView.setAdapter(artistAdapter);
                        } else {
                            VerticalArtistAdapter artistAdapter = (VerticalArtistAdapter) searchBinding.searchedArtistsRecyclerView.getAdapter();
                            artistAdapter.updateList(artists);
                        }

                        if (artists.isEmpty()) {
                            searchBinding.artistsLayout.setVisibility(View.GONE);
                            isArtistsEmpty = true;
                        } else {
                            searchBinding.artistsLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void searchVideos(String query) {
        Disposable disposable = mainViewModel.searchInVideosFromDb(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
                        if (searchBinding.searchedVideosRecyclerView.getAdapter() == null) {
                            VerticalVideoAdapter musicAdapter = new VerticalVideoAdapter(videos);
                            searchBinding.searchedVideosRecyclerView.setAdapter(musicAdapter);
                        } else {
                            VerticalVideoAdapter musicAdapter = (VerticalVideoAdapter) searchBinding.searchedVideosRecyclerView.getAdapter();
                            musicAdapter.updateList(videos);
                        }

                        if (videos.isEmpty()) {
                            searchBinding.videosLayout.setVisibility(View.GONE);
                            isVideosEmpty = true;
                        } else {
                            searchBinding.videosLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}