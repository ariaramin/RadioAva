package com.ariaramin.radioava.Adapters.Music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.HorizontalItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    List<Music> musicList;
    String TAG;

    public MusicAdapter(List<Music> musicList, String TAG) {
        this.musicList = musicList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HorizontalItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.horizontal_item_layout, parent, false);
        return new MusicViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bindData(musicList.get(position), TAG);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void updateList(List<Music> musics) {
        musicList = musics;
        notifyDataSetChanged();
    }

    static class MusicViewHolder extends RecyclerView.ViewHolder {

        HorizontalItemLayoutBinding itemLayoutBinding;

        public MusicViewHolder(@NonNull HorizontalItemLayoutBinding itemLayoutBinding) {
            super(itemLayoutBinding.getRoot());
            this.itemLayoutBinding = itemLayoutBinding;
        }

        private void bindData(Music music, String TAG) {
            Glide.with(itemLayoutBinding.getRoot().getContext())
                    .load(music.getCover())
                    .thumbnail(
                            Glide.with(itemLayoutBinding.getRoot().getContext())
                            .load(R.drawable.loading)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(300, 300)
                    .into(itemLayoutBinding.itemImageView);
            itemLayoutBinding.itemNameTextView.setText(stringCutter(music.getName(), 16));
            itemLayoutBinding.itemArtistTextView.setText(stringCutter(music.getArtist(), 22));
            itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Music", music);
                    switch (TAG) {
                        case "home":
                            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_playerFragment, bundle);
                            break;
                        case "browser":
                            Navigation.findNavController(v).navigate(R.id.action_browseFragment_to_playerFragment, bundle);
                            break;
                    }
                }
            });
            itemLayoutBinding.executePendingBindings();
        }

        private String stringCutter(String name, int length) {
            int nameLength = name.length();
            if (nameLength > length) {
                String subString = name.substring(0, length);
                return subString + "...";
            }
            return name;
        }
    }
}
