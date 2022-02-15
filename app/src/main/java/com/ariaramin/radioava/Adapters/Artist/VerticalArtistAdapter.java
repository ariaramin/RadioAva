package com.ariaramin.radioava.Adapters.Artist;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.VerticalArtistItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VerticalArtistAdapter extends RecyclerView.Adapter<VerticalArtistAdapter.VerticalArtistViewHolder>{

    VerticalArtistItemLayoutBinding itemLayoutBinding;
    List<Artist> artistList;

    public VerticalArtistAdapter(List<Artist> artists) {
        this.artistList = artists;
    }

    @NonNull
    @Override
    public VerticalArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_artist_item_layout, parent, false);
        return new VerticalArtistViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalArtistViewHolder holder, int position) {
        holder.bindData(artistList.get(position));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public void updateList(List<Artist> artists) {
        artistList = artists;
        notifyDataSetChanged();
    }

    static class VerticalArtistViewHolder extends RecyclerView.ViewHolder {

        VerticalArtistItemLayoutBinding itemLayoutBinding;

        public VerticalArtistViewHolder(@NonNull VerticalArtistItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Artist artist) {
            Glide.with(itemLayoutBinding.verticalArtistImageView)
                    .load(artist.getImage())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(150, 150)
                    .into(itemLayoutBinding.verticalArtistImageView);
            itemLayoutBinding.verticalArtistNameTextView.setText(artist.getName());
            itemLayoutBinding.verticalFollowersTextView.setText(artist.getFollowers());
            itemLayoutBinding.verticalPlayTextView.setText(artist.getPlays());
        }
    }
}
