package com.ariaramin.radioava.Adapters.Artist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.HorizontalArtistItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    List<Artist> artistList;
    String TAG;

    public ArtistAdapter(List<Artist> artistList, String TAG) {
        this.artistList = artistList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HorizontalArtistItemLayoutBinding artistItemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.horizontal_artist_item_layout, parent, false);
        return new ArtistViewHolder(artistItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        holder.bindData(artistList.get(position), TAG);
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public void updateList(List<Artist> artists) {
        artistList = artists;
        notifyDataSetChanged();
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {

        HorizontalArtistItemLayoutBinding artistItemLayoutBinding;

        public ArtistViewHolder(@NonNull HorizontalArtistItemLayoutBinding artistItemLayoutBinding) {
            super(artistItemLayoutBinding.getRoot());
            this.artistItemLayoutBinding = artistItemLayoutBinding;
        }

        private void bindData(Artist artist, String TAG) {
            Glide.with(artistItemLayoutBinding.getRoot().getContext())
                    .load(artist.getImage())
                    .thumbnail(
                            Glide.with(artistItemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(300, 300)
                    .into(artistItemLayoutBinding.horizontalArtistImageView);
            artistItemLayoutBinding.horizontalArtistNameTextView.setText(artist.getName());
            artistItemLayoutBinding.horizontalArtistCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Artist", artist);
                    switch (TAG) {
                        case "home":
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_detailArtistFragment, bundle);
                            break;
                        case "artists":
                            Navigation.findNavController(v).navigate(R.id.action_artistsFragment_to_detailArtistFragment, bundle);
                            break;
                        case "browser":
                            Navigation.findNavController(v).navigate(R.id.action_browseFragment_to_detailArtistFragment, bundle);
                            break;
                    }
                }
            });
            artistItemLayoutBinding.executePendingBindings();
        }
    }
}
