package com.ariaramin.radioava.ui.Fragments.Artist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Artist.VerticalArtistAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;
import com.ariaramin.radioava.databinding.FragmentFollowedArtistsBinding;

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
public class FollowedArtistsFragment extends Fragment {

    FragmentFollowedArtistsBinding artistBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<String> followedArtist;
    ArrayList<Artist> followedArtistList = new ArrayList<>();
    private static final String TAG = "followed_artists";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        artistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_followed_artists, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        artistBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        followedArtist = sharedPreferenceManager.readFollowedArtistData();
        getFollowedArtists();

        return artistBinding.getRoot();
    }

    private void getFollowedArtists() {
        Disposable disposable = mainViewModel.getAllArtistsFromDb()
                .map(artists -> {
                    followedArtistList.clear();
                    for (int i = 0; i < followedArtist.size(); i++) {
                        for (int j = 0; j < artists.size(); j++) {
                            if (followedArtist.get(i).equals(artists.get(j).getId() + artists.get(j).getName())) {
                                followedArtistList.add(artists.get(j));
                            }
                        }
                    }
                    return followedArtistList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Artist>>() {
                    @Override
                    public void accept(List<Artist> artists) throws Throwable {
                        Collections.reverse(artists);

                        if (artistBinding.artistsRecyclerView.getAdapter() == null) {
                            VerticalArtistAdapter adapter = new VerticalArtistAdapter(artists, TAG);
                            artistBinding.artistsRecyclerView.setAdapter(adapter);
                        } else {
                            VerticalArtistAdapter adapter = (VerticalArtistAdapter) artistBinding.artistsRecyclerView.getAdapter();
                            adapter.updateList(artists);
                        }

                        if (artists.isEmpty()) {
                            artistBinding.notFoundArtistTextView.setVisibility(View.VISIBLE);
                        } else {
                            artistBinding.notFoundArtistTextView.setVisibility(View.GONE);
                        }
                    }
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