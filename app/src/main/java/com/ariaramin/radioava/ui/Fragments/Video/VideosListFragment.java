package com.ariaramin.radioava.ui.Fragments.Video;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentVideosListBinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class VideosListFragment extends Fragment {

    FragmentVideosListBinding videosListBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videosListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos_list, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        Bundle args = getArguments();
        int position = args.getInt("position");

        switch (position) {
            case 0:
                getTrendingVideos();
                break;
            case 1:
                getPopularVideos();
                break;
        }
        return videosListBinding.getRoot();
    }

    private void getTrendingVideos() {
        Disposable disposable = mainViewModel.getTrendingVideosFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
                        List<Video> trending = videos.subList(0, 30);

                        if (videosListBinding.videosRecyclerView.getAdapter() == null) {
                            VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(trending);
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
                        List<Video> mostViewed = videos.subList(0, 30);

                        if (videosListBinding.videosRecyclerView.getAdapter() == null) {
                            VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(mostViewed);
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
                    }
                });
        compositeDisposable.add(disposable);
    }

    private List<Video> sortByLikes(List<Video> videos) {
        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                return Integer.compare(o2.getLikes(), o1.getLikes());
            }
        });
        return videos;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}