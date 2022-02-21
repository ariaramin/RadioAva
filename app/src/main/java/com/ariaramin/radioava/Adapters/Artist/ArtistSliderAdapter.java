package com.ariaramin.radioava.Adapters.Artist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.ArtistSliderItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ArtistSliderAdapter extends SliderViewAdapter<ArtistSliderAdapter.ArtistSliderViewHolder> {

    List<Artist> artistList;

    public ArtistSliderAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public ArtistSliderViewHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ArtistSliderItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.artist_slider_item_layout, parent, false);
        return new ArtistSliderViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(ArtistSliderViewHolder viewHolder, int position) {
        viewHolder.bindData(artistList.get(position));
    }

    @Override
    public int getCount() {
        return artistList.size();
    }

    public void updateList(List<Artist> artists) {
        artistList = artists;
        notifyDataSetChanged();
    }

    static class ArtistSliderViewHolder extends SliderViewAdapter.ViewHolder {

        ArtistSliderItemLayoutBinding itemLayoutBinding;

        public ArtistSliderViewHolder(ArtistSliderItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Artist artist) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(artist.getBackgroundImage())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(itemLayoutBinding.sliderArtistImageView);

            itemLayoutBinding.sliderArtistTextView.setText(artist.getName());
            itemLayoutBinding.sliderFollowersTextView.setText(artist.getFollowers());
            itemLayoutBinding.sliderPlayTextView.setText(artist.getPlaysCount());
            itemLayoutBinding.executePendingBindings();
        }
    }
}
