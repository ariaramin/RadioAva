package com.ariaramin.radioava.Adapters.Video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.HorizontalVideoItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<Video> videoList;
    String TAG;

    public VideoAdapter(List<Video> videoList, String TAG) {
        this.videoList = videoList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HorizontalVideoItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.horizontal_video_item_layout, parent, false);
        return new VideoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bindData(videoList.get(position), TAG);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateList(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        HorizontalVideoItemLayoutBinding itemLayoutBinding;

        public VideoViewHolder(HorizontalVideoItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Video video, String TAG) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(video.getCover())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(itemLayoutBinding.videoImageView);
            itemLayoutBinding.videoNameTextView.setText(video.getName());
            itemLayoutBinding.videoArtistTextView.setText(video.getArtist());
            itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Video", video);
                    switch (TAG) {
                        case "home":
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "browser":
                            Navigation.findNavController(v).navigate(R.id.action_browseFragment_to_videoPlayerFragment, bundle);
                            break;
                    }
                }
            });
            itemLayoutBinding.executePendingBindings();
        }
    }
}
