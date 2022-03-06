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
import com.ariaramin.radioava.databinding.VerticalItemLayoutBinding;
import com.ariaramin.radioava.ui.Fragments.Video.OnClickVideoListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VerticalVideoAdapter extends RecyclerView.Adapter<VerticalVideoAdapter.VerticalVideoViewHolder> {

    List<Video> videoList;
    String TAG;
    OnClickVideoListener listener;

    public VerticalVideoAdapter(List<Video> videoList, String TAG) {
        this.videoList = videoList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public VerticalVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VerticalItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_item_layout, parent, false);
        return new VerticalVideoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalVideoViewHolder holder, int position) {
        holder.bindData(videoList.get(position), TAG, listener);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateList(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    public void addVideos(List<Video> videos) {
        videoList.addAll(videos);
        notifyDataSetChanged();
    }

    public void addListener(OnClickVideoListener videoListener) {
        listener = videoListener;
    }

    static class VerticalVideoViewHolder extends RecyclerView.ViewHolder {

        VerticalItemLayoutBinding itemLayoutBinding;

        public VerticalVideoViewHolder(VerticalItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Video video, String TAG, OnClickVideoListener listener) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(video.getCover())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(150, 150)
                    .into(itemLayoutBinding.verticalItemImageView);
            itemLayoutBinding.verticalItemNameTextView.setText(video.getName());
            itemLayoutBinding.verticalItemArtistTextView.setText(video.getArtist());
            itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Video", video);
                    switch (TAG) {
                        case "videos":
                            Navigation.findNavController(v).navigate(R.id.action_videosFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "all_videos":
                            Navigation.findNavController(v).navigate(R.id.action_allVideosFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "search":
                            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "artist_works":
                            Navigation.findNavController(v).navigate(R.id.action_detailArtistFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "my_musics":
                            Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "liked":
                            Navigation.findNavController(v).navigate(R.id.action_likedFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "downloaded":
                            Navigation.findNavController(v).navigate(R.id.action_videosListFragment_to_videoPlayerFragment, bundle);
                            break;
                        case "video_player":
                            if (listener != null) {
                                listener.OnClick(video);
                            }
                            break;
                    }
                }
            });
            itemLayoutBinding.executePendingBindings();
        }
    }
}
