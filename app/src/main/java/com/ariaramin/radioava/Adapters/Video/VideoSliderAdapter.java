package com.ariaramin.radioava.Adapters.Video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.SliderItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class VideoSliderAdapter extends SliderViewAdapter<VideoSliderAdapter.VideoSliderViewHolder> {

    SliderItemLayoutBinding sliderItemBinding;
    List<Video> videoList;

    public VideoSliderAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @Override
    public VideoSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        sliderItemBinding = DataBindingUtil.inflate(inflater, R.layout.slider_item_layout, parent, false);
        return new VideoSliderViewHolder(sliderItemBinding);
    }

    @Override
    public void onBindViewHolder(VideoSliderViewHolder viewHolder, int position) {
        viewHolder.bindData(videoList.get(position));
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    public void updateList(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    static class VideoSliderViewHolder extends SliderViewAdapter.ViewHolder {

        SliderItemLayoutBinding sliderItemBinding;

        public VideoSliderViewHolder(SliderItemLayoutBinding sliderItemBinding) {
            super(sliderItemBinding.getRoot());
            this.sliderItemBinding = sliderItemBinding;
        }

        private void bindData(Video video) {
            Glide.with(sliderItemBinding.getRoot().getContext())
                    .load(video.getCover())
                    .thumbnail(
                            Glide.with(sliderItemBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(sliderItemBinding.sliderImageView);
        }
    }
}
