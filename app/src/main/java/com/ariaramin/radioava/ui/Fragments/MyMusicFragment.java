package com.ariaramin.radioava.ui.Fragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;
import com.ariaramin.radioava.databinding.FragmentMyMusicBinding;
import com.ariaramin.radioava.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;


import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MyMusicFragment extends Fragment {

    FragmentMyMusicBinding myMusicBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<String> recentlyPlayed;
    ArrayList<Video> recentlyPlayedVideos = new ArrayList<>();
    ArrayList<Music> recentlyPlayedMusics = new ArrayList<>();
    private static final String TAG = "my_musics";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myMusicBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_music, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        recentlyPlayed = sharedPreferenceManager.readRecentlyPlayedData();
        setupRecyclerViews();
        setupButtons();

        return myMusicBinding.getRoot();
    }

    private void setupButtons() {
        myMusicBinding.followedArtistLayout.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_followedArtistsFragment)
        );
        myMusicBinding.likedLayout.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_likedFragment)
        );
        myMusicBinding.downloadedMusicLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", 3);
            Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_musicsListFragment, bundle);
        });
        myMusicBinding.downloadedVideosLayout.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_videosListFragment)
        );
        myMusicBinding.downloadedAlbumLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", 4);
            Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_musicsListFragment, bundle);
        });
    }

    private void setupRecyclerViews() {
        getMusics();
        getVideos();
    }

    private void getVideos() {
        Disposable disposable = mainViewModel.getAllVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    recentlyPlayedVideos.clear();
                    for (int i = 0; i < recentlyPlayed.size(); i++) {
                        for (int j = 0; j < videos.size(); j++) {
                            if (recentlyPlayed.get(i).equals(videos.get(j).getId() + videos.get(j).getName())) {
                                recentlyPlayedVideos.add(videos.get(j));
                            }
                        }
                    }
                    Collections.reverse(recentlyPlayedVideos);
                    if (myMusicBinding.recentlyPlayedVideosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter adapter = new VerticalVideoAdapter(recentlyPlayedVideos, TAG);
                        myMusicBinding.recentlyPlayedVideosRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) myMusicBinding.recentlyPlayedVideosRecyclerView.getAdapter();
                        adapter.updateList(recentlyPlayedVideos);
                    }

                    if (recentlyPlayedVideos.isEmpty()) {
                        myMusicBinding.recentlyPlayedVideosLayout.setVisibility(View.GONE);
                    } else {
                        myMusicBinding.recentlyPlayedVideosLayout.setVisibility(View.VISIBLE);
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void getMusics() {
        Disposable disposable = mainViewModel.getAllMusics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musics -> {
                    recentlyPlayedMusics.clear();
                    for (int i = 0; i < recentlyPlayed.size(); i++) {
                        for (int j = 0; j < musics.size(); j++) {
                            if (recentlyPlayed.get(i).equals(musics.get(j).getId() + musics.get(j).getName())) {
                                recentlyPlayedMusics.add(musics.get(j));
                            }
                        }
                    }
                    Collections.reverse(recentlyPlayedMusics);
                    if (myMusicBinding.recentlyPlayedMusicsRecyclerView.getAdapter() == null) {
                        VerticalMusicAdapter adapter = new VerticalMusicAdapter(recentlyPlayedMusics, TAG);
                        myMusicBinding.recentlyPlayedMusicsRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalMusicAdapter adapter = (VerticalMusicAdapter) myMusicBinding.recentlyPlayedMusicsRecyclerView.getAdapter();
                        adapter.updateList(recentlyPlayedMusics);
                    }

                    if (recentlyPlayedMusics.isEmpty()) {
                        myMusicBinding.recentlyPlayedMusicsLayout.setVisibility(View.GONE);
                    } else {
                        myMusicBinding.recentlyPlayedMusicsLayout.setVisibility(View.VISIBLE);
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}