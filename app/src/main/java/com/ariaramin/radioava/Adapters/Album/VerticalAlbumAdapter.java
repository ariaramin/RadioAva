package com.ariaramin.radioava.Adapters.Album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.VerticalItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VerticalAlbumAdapter extends RecyclerView.Adapter<VerticalAlbumAdapter.VerticalAlbumsViewHolder> {

    List<Album> albumList;
    String TAG;

    public VerticalAlbumAdapter(List<Album> albumList, String TAG) {
        this.albumList = albumList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public VerticalAlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VerticalItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_item_layout, parent, false);
        return new VerticalAlbumsViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalAlbumsViewHolder holder, int position) {
        holder.bindData(albumList.get(position), TAG);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void updateList(List<Album> albums) {
        albumList = albums;
        notifyDataSetChanged();
    }

    static class VerticalAlbumsViewHolder extends RecyclerView.ViewHolder {

        VerticalItemLayoutBinding itemLayoutBinding;

        public VerticalAlbumsViewHolder(VerticalItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Album album, String TAG) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(album.getCover())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                                    .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(150, 150)
                    .into(itemLayoutBinding.verticalItemImageView);
            itemLayoutBinding.verticalItemNameTextView.setText(album.getName());
            itemLayoutBinding.verticalItemArtistTextView.setText(album.getArtist());
            itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Album", album);
                    switch (TAG) {
                        case "artist_works":
                            Navigation.findNavController(v).navigate(R.id.action_detailArtistFragment_to_albumFragment, bundle);
                            break;
                        case "search":
                            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_albumFragment, bundle);
                            break;
                        case "music_list":
                            Navigation.findNavController(v).navigate(R.id.action_allMusicsFragment_to_albumFragment, bundle);
                            break;
                        case "liked":
                            Navigation.findNavController(v).navigate(R.id.action_likedFragment_to_albumFragment, bundle);
                            break;
                        case "downloaded":
                            Navigation.findNavController(v).navigate(R.id.action_musicsListFragment_to_albumFragment, bundle);
                            break;
                    }
                }
            });
            itemLayoutBinding.executePendingBindings();
        }
    }
}
