package com.ariaramin.radioava.ui.Fragments.Music;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ariaramin.radioava.Adapters.Album.VerticalAlbumAdapter;
import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentMusicsListBinding;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MusicsListFragment extends Fragment {

    FragmentMusicsListBinding musicsBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    private static final String TAG = "music_list";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        musicsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_musics_list, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        Bundle args = getArguments();
        int position = args.getInt("position");

        switch (position) {
            case 0:
                getTrendingMusics();
                break;
            case 1:
                getPopularMusics();
                break;
            case 2:
                getLatestAlbums();
                break;
        }
        return musicsBinding.getRoot();
    }

    private void getTrendingMusics() {
        Disposable disposable = mainViewModel.getTrendingMusicsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {

                        if (musicsBinding.musicsRecyclerView.getAdapter() == null) {
                            VerticalMusicAdapter musicAdapter = new VerticalMusicAdapter(musics, TAG);
                            musicsBinding.musicsRecyclerView.setAdapter(musicAdapter);
                        } else {
                            VerticalMusicAdapter adapter = (VerticalMusicAdapter) musicsBinding.musicsRecyclerView.getAdapter();
                            adapter.updateList(musics);
                        }

                        if (musics.isEmpty()) {
                            musicsBinding.musicsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            musicsBinding.musicsSpinKit.setVisibility(View.GONE);
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

                        if (musicsBinding.musicsRecyclerView.getAdapter() == null) {
                            VerticalMusicAdapter musicAdapter = new VerticalMusicAdapter(musics, TAG);
                            musicsBinding.musicsRecyclerView.setAdapter(musicAdapter);
                        } else {
                            VerticalMusicAdapter adapter = (VerticalMusicAdapter) musicsBinding.musicsRecyclerView.getAdapter();
                            adapter.updateList(musics);
                        }

                        if (musics.isEmpty()) {
                            musicsBinding.musicsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            musicsBinding.musicsSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getLatestAlbums() {
        Disposable disposable = mainViewModel.getAllAlbumsFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Album>>() {
                    @Override
                    public void accept(List<Album> albums) throws Throwable {
                        List<Album> albumList = albums.subList(0, 30);

                        if (musicsBinding.musicsRecyclerView.getAdapter() == null) {
                            VerticalAlbumAdapter albumAdapter = new VerticalAlbumAdapter(albumList, TAG);
                            musicsBinding.musicsRecyclerView.setAdapter(albumAdapter);
                        } else {
                            VerticalAlbumAdapter adapter = (VerticalAlbumAdapter) musicsBinding.musicsRecyclerView.getAdapter();
                            adapter.updateList(albumList);
                        }

                        if (albums.isEmpty()) {
                            musicsBinding.musicsSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            musicsBinding.musicsSpinKit.setVisibility(View.GONE);
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