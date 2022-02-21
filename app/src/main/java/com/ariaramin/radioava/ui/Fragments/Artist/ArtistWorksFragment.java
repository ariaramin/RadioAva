package com.ariaramin.radioava.ui.Fragments.Artist;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Album.VerticalAlbumAdapter;
import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentArtistWorksBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtistWorksFragment extends Fragment {

    FragmentArtistWorksBinding artistWorksBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    private static final String TAG = "artist_works";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        artistWorksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_works, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        Bundle args = getArguments();
        int position = args.getInt("position");
        Artist artist = args.getParcelable("Artist");

        switch (position) {
            case 0:
                setupMusicsRecyclerView(artist);
                break;
            case 1:
                setupAlbumsRecyclerView(artist);
                break;
            case 2:
                setupVideosRecyclerView(artist);
                break;
        }
        return artistWorksBinding.getRoot();
    }

    private void setupMusicsRecyclerView(Artist artist) {
        Disposable disposable = mainViewModel.getArtistMusicsFromDb(artist.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musicList) throws Throwable {

                        mainViewModel.setArtistTotalMusics(musicList.size());

                        if (artistWorksBinding.artistWorksRecyclerView.getAdapter() == null) {
                            VerticalMusicAdapter adapter = new VerticalMusicAdapter(musicList);
                            artistWorksBinding.artistWorksRecyclerView.setAdapter(adapter);
                        } else {
                            VerticalMusicAdapter adapter = (VerticalMusicAdapter) artistWorksBinding.artistWorksRecyclerView.getAdapter();
                            adapter.updateList(musicList);
                        }

                        if (musicList.isEmpty()) {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.VISIBLE);
                        } else {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupAlbumsRecyclerView(Artist artist) {
        Disposable disposable = mainViewModel.getArtistAlbumsFromDb(artist.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Album>>() {
                    @Override
                    public void accept(List<Album> albums) throws Throwable {
                        if (artistWorksBinding.artistWorksRecyclerView.getAdapter() == null) {
                            VerticalAlbumAdapter adapter = new VerticalAlbumAdapter(albums, TAG);
                            artistWorksBinding.artistWorksRecyclerView.setAdapter(adapter);
                        } else {
                            VerticalAlbumAdapter adapter = (VerticalAlbumAdapter) artistWorksBinding.artistWorksRecyclerView.getAdapter();
                            adapter.updateList(albums);
                        }

                        if (albums.isEmpty()) {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.VISIBLE);
                        } else {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupVideosRecyclerView(Artist artist) {
        Disposable disposable = mainViewModel.getArtistVideosFromDb(artist.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
                        if (artistWorksBinding.artistWorksRecyclerView.getAdapter() == null) {
                            VerticalVideoAdapter adapter = new VerticalVideoAdapter(videos);
                            artistWorksBinding.artistWorksRecyclerView.setAdapter(adapter);
                        } else {
                            VerticalVideoAdapter adapter = (VerticalVideoAdapter) artistWorksBinding.artistWorksRecyclerView.getAdapter();
                            adapter.updateList(videos);
                        }

                        if (videos.isEmpty()) {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.VISIBLE);
                        } else {
                            artistWorksBinding.artistWorksNotFoundTextView.setVisibility(View.GONE);
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