package com.ariaramin.radioava.Adapters.Video;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.VerticalItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VerticalVideoAdapter extends RecyclerView.Adapter<VerticalVideoAdapter.VerticalVideoViewHolder> {

    VerticalItemLayoutBinding itemLayoutBinding;
    List<Video> videoList;

    public VerticalVideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VerticalVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_item_layout, parent, false);
        return new VerticalVideoViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalVideoViewHolder holder, int position) {
        holder.bindData(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public void updateList(List<Video> videos) {
        videoList = videos;
        notifyDataSetChanged();
    }

    static class VerticalVideoViewHolder extends RecyclerView.ViewHolder {

        VerticalItemLayoutBinding itemLayoutBinding;

        public VerticalVideoViewHolder(VerticalItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Video video) {
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
        }
    }
}
