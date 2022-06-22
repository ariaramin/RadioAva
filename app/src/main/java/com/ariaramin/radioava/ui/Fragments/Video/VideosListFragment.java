package com.ariaramin.radioava.ui.Fragments.Video;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;
import com.ariaramin.radioava.databinding.FragmentVideosListBinding;
import com.ariaramin.radioava.utils.Constants;

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
public class VideosListFragment extends Fragment {

    FragmentVideosListBinding videosListBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    ArrayList<String> downloaded;
    ArrayList<Video> downloadedVideoList = new ArrayList<>();
    private static final String TAG = "all_videos";
    private static final String TAG2 = "downloaded";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videosListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_list, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        downloaded = sharedPreferenceManager.readDownloadedData();

        Bundle args = getArguments();
        if (args != null) {
            int position = args.getInt("position");
            switch (position) {
                case 0:
                    getTrendingVideos();
                    break;
                case 1:
                    getPopularVideos();
                    break;
            }
        } else {
            videosListBinding.headerLayout.setVisibility(View.VISIBLE);
            videosListBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());
            getDownloadedVideos();
        }

        return videosListBinding.getRoot();
    }

    private void getDownloadedVideos() {
        Disposable disposable = mainViewModel.getAllVideos()
                .map(videos -> {
                    downloadedVideoList.clear();
                    for (int i = 0; i < downloaded.size(); i++) {
                        for (int j = 0; j < videos.size(); j++) {
                            if (downloaded.get(i).equals(videos.get(j).getId() + videos.get(j).getName())) {
                                downloadedVideoList.add(videos.get(j));
                            }
                        }
                    }
                    return downloadedVideoList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Consumer<List<Video>>) videos -> {
                    Collections.reverse(videos);

                    if (videosListBinding.videosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter adapter = new VerticalVideoAdapter(videos, TAG2);
                        videosListBinding.videosRecyclerView.setAdapter(adapter);
                    } else {
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) videosListBinding.videosRecyclerView.getAdapter();
                        adapter.updateList(videos);
                    }

                    if (videos.isEmpty()) {
                        videosListBinding.notFoundVideoTextView.setVisibility(View.VISIBLE);
                    } else {
                        videosListBinding.notFoundVideoTextView.setVisibility(View.GONE);
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void getTrendingVideos() {
        Disposable disposable = mainViewModel.getTrendingVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    List<Video> trending = videos.subList(0, 30);

                    if (videosListBinding.videosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(trending, TAG);
                        videosListBinding.videosRecyclerView.setAdapter(videoAdapter);
                    } else {
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) videosListBinding.videosRecyclerView.getAdapter();
                        adapter.updateList(trending);
                    }

                    if (videos.isEmpty()) {
                        videosListBinding.videosSpinKit.setVisibility(View.VISIBLE);
                    } else {
                        videosListBinding.videosSpinKit.setVisibility(View.GONE);
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private void getPopularVideos() {
        Disposable disposable = mainViewModel.getAllVideos()
                .map(this::sortByLikes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    List<Video> mostViewed = videos.subList(0, 30);

                    if (videosListBinding.videosRecyclerView.getAdapter() == null) {
                        VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(mostViewed, TAG);
                        videosListBinding.videosRecyclerView.setAdapter(videoAdapter);
                    } else {
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) videosListBinding.videosRecyclerView.getAdapter();
                        adapter.updateList(mostViewed);
                    }

                    if (videos.isEmpty()) {
                        videosListBinding.videosSpinKit.setVisibility(View.VISIBLE);
                    } else {
                        videosListBinding.videosSpinKit.setVisibility(View.GONE);
                    }
                }, throwable -> {
                    Constants.raiseError(getActivity(), getString(R.string.something_went_wrong));
                });
        compositeDisposable.add(disposable);
    }

    private List<Video> sortByLikes(List<Video> videos) {
        Collections.sort(videos, (o1, o2) -> Integer.compare(o2.getLikes(), o1.getLikes()));
        return videos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}