package com.ariaramin.radioava.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.Adapters.Video.VideoSliderAdapter;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentVideosBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
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
    private static final String TAG = "videos";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        videosBinding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_videosFragment_to_searchFragment);
            }
        });
        return videosBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMostViewedVideos();
        setupNavigation();
    }

    private void setupNavigation() {
        videosBinding.seeMoreVideosTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_videosFragment_to_allVideosFragment);
            }
        });
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
                            List<Video> topFiveVideos = videos.subList(0, 5);
                            List<Video> mostViewedVideos = videos.subList(7, 22);

                            loadImage(videos.get(5), videosBinding.sixthVideoImageView);
                            loadImage(videos.get(6), videosBinding.seventhVideoImageView);

                            if (videosBinding.videoSliderView.getSliderAdapter() == null) {
                                VideoSliderAdapter sliderAdapter = new VideoSliderAdapter(topFiveVideos);
                                videosBinding.videoSliderView.setSliderAdapter(sliderAdapter);
                            } else {
                                VideoSliderAdapter sliderAdapter = (VideoSliderAdapter) videosBinding.videoSliderView.getSliderAdapter();
                                sliderAdapter.updateList(topFiveVideos);
                            }
                            videosBinding.videoSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            videosBinding.videoSliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            videosBinding.videoSliderView.setScrollTimeInSec(5);
                            videosBinding.videoSliderView.startAutoCycle();

                            if (videosBinding.mostViewedRecyclerView.getAdapter() == null) {
                                VerticalVideoAdapter videoAdapter = new VerticalVideoAdapter(mostViewedVideos, TAG);
                                videosBinding.mostViewedRecyclerView.setAdapter(videoAdapter);
                            } else {
                                VerticalVideoAdapter adapter = (VerticalVideoAdapter) videosBinding.mostViewedRecyclerView.getAdapter();
                                adapter.updateList(mostViewedVideos);
                            }
                            videosBinding.mostViewedRecyclerView.setHasFixedSize(false);
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

    private void loadImage(Video video, ImageView imageView) {
        Glide.with(videosBinding.getRoot().getContext())
                .load(video.getCover())
                .thumbnail(
                        Glide.with(videosBinding.getRoot().getContext())
                                .load(R.drawable.loading)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    private List<Video> sortByViewed(List<Video> videos) {
        Collections.sort(videos, new Comparator<Video>() {
            @Override
            public int compare(Video o1, Video o2) {
                return Integer.compare(o2.getViews(), o1.getViews());
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