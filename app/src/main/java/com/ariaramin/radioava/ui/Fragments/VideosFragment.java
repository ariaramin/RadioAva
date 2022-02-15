package com.ariaramin.radioava.ui.Fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.Adapters.Video.VideoSliderAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentVideosBinding;
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

public class VideosFragment extends Fragment {

    FragmentVideosBinding videosBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();

        getMostViewedVideos();
        return videosBinding.getRoot();
    }

    private void getMostViewedVideos() {
        Disposable disposable = mainViewModel.getAllVideosFromDb()
                .map(this::sortByViewed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {

                        if (!videos.isEmpty()) {
                            List<Video> topThreeVideos = videos.subList(0, 5);
                            List<Video> mostViewedVideos = videos.subList(5, 35);

                            if (videosBinding.videoSliderView.getSliderAdapter() == null) {
                                VideoSliderAdapter sliderAdapter = new VideoSliderAdapter(topThreeVideos);
                                videosBinding.videoSliderView.setSliderAdapter(sliderAdapter);
                            } else {
                                VideoSliderAdapter sliderAdapter = (VideoSliderAdapter) videosBinding.videoSliderView.getSliderAdapter();
                                sliderAdapter.updateList(topThreeVideos);
                            }
                            videosBinding.videoSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            videosBinding.videoSliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            videosBinding.videoSliderView.setScrollTimeInSec(5);
                            videosBinding.videoSliderView.startAutoCycle();

                            if (videosBinding.mostViewedRecyclerView.getAdapter() == null) {
                                VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(mostViewedVideos);
                                videosBinding.mostViewedRecyclerView.setAdapter(videoAdapter);
                            } else {
                                VerticalVideoAdapter adapter = (VerticalVideoAdapter) videosBinding.mostViewedRecyclerView.getAdapter();
                                adapter.updateList(mostViewedVideos);
                            }
                        }

                        if (videos.isEmpty()) {
                            videosBinding.mostViewedSpinKit.setVisibility(View.VISIBLE);
                            videosBinding.videoSliderSpinKit.setVisibility(View.VISIBLE);
                        } else {
                            videosBinding.mostViewedSpinKit.setVisibility(View.GONE);
                            videosBinding.videoSliderSpinKit.setVisibility(View.GONE);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private List<Video> sortByViewed(List<Video> videos) {
        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                int o1Views = convertStringToInt(o1.getViews());
                int o2Views = convertStringToInt(o2.getViews());
                return Integer.compare(o2Views, o1Views);
            }
        });
        return videos;
    }

    private int convertStringToInt(String likes) {
        if (likes.contains("K")) {
            String str = likes.replace("K", "");
            return (int) Double.parseDouble(str);
        } else if (likes.contains("M")) {
            String str = likes.replace("M", "");
            return (int) Double.parseDouble(str);
        }
        return (int) Double.parseDouble(likes);
    }
}