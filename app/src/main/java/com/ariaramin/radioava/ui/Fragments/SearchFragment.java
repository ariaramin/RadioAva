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
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentSearchBinding;
import com.ariaramin.radioava.utils.Constants;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
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
        searchBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());
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
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        });
    }

    private void searchMusics(String search) {
        Disposable disposable = mainViewModel.searchInMusics(search, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musics -> {
                    if (searchBinding.searchedMusicsRecyclerView.getAdapter() == null) {
                        VerticalMusicAdapter musicAdapter = new VerticalMusicAdapter(musics, TAG);
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
                        isMusicsEmpty = false;
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void searchAlbums(String search) {
        Disposable disposable = mainViewModel.searchInAlbums(search, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(albums -> {
                    if (searchBinding.searchedAlbumsRecyclerView.getAdapter() == null) {
                        VerticalAlbumAdapter albumAdapter = new VerticalAlbumAdapter(albums, TAG);
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
                        isAlbumsEmpty = false;
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void searchArtists(String search) {
        Disposable disposable = mainViewModel.searchInArtists(search, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artists -> {
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
                        isArtistsEmpty = false;
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void searchVideos(String search) {
        Disposable disposable = mainViewModel.searchInVideos(search, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    if (searchBinding.searchedVideosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter musicAdapter = new VerticalVideoAdapter(videos, TAG);
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
                        isVideosEmpty = false;
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}