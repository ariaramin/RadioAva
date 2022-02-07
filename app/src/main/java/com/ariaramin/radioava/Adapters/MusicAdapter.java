package com.ariaramin.radioava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.HorizontalItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    HorizontalItemLayoutBinding itemLayoutBinding;
    ArrayList<Music> musicList;

    public MusicAdapter(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.horizontal_item_layout, parent, false);
        return new MusicViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindData(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void updateList(ArrayList<Music> musics) {
        musicList = musics;
        notifyDataSetChanged();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {

        HorizontalItemLayoutBinding itemLayoutBinding;

        public MusicViewHolder(@NonNull HorizontalItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Music music) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(music.getCover())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                            .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemLayoutBinding.itemImageView);
            itemLayoutBinding.itemNameTextView.setText(music.getName());
            itemLayoutBinding.itemArtistTextView.setText(music.getArtist());
        }
    }
}
