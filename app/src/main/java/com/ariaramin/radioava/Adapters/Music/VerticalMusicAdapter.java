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
import com.ariaramin.radioava.databinding.VerticalItemLayoutBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class VerticalMusicAdapter extends RecyclerView.Adapter<VerticalMusicAdapter.VerticalMusicViewHolder> {

    List<Music> musicList;
    String TAG;

    public VerticalMusicAdapter(List<Music> musicList, String TAG) {
        this.musicList = musicList;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public VerticalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VerticalItemLayoutBinding itemLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_item_layout, parent, false);
        return new VerticalMusicViewHolder(itemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalMusicViewHolder holder, int position) {
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

    static class VerticalMusicViewHolder extends RecyclerView.ViewHolder {

        VerticalItemLayoutBinding itemLayoutBinding;

        public VerticalMusicViewHolder(VerticalItemLayoutBinding itemLayoutBinding) {
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
                    .override(150, 150)
                    .into(itemLayoutBinding.verticalItemImageView);
            itemLayoutBinding.verticalItemNameTextView.setText(stringCutter(music.getName(), 32));
            itemLayoutBinding.verticalItemArtistTextView.setText(stringCutter(music.getArtist(), 46));
            itemLayoutBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Music", music);
                    switch (TAG) {
                        case "music_list":
                            Navigation.findNavController(v).navigate(R.id.action_allMusicsFragment_to_playerFragment, bundle);
                            break;
                        case "album":
                            Navigation.findNavController(v).navigate(R.id.action_albumFragment_to_playerFragment, bundle);
                            break;
                        case "search":
                            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_playerFragment, bundle);
                            break;
                        case "artist_works":
                            Navigation.findNavController(v).navigate(R.id.action_detailArtistFragment_to_playerFragment, bundle);
                            break;
                        case "my_musics":
                            Navigation.findNavController(v).navigate(R.id.action_myMusicFragment_to_playerFragment, bundle);
                            break;
                        case "liked":
                            Navigation.findNavController(v).navigate(R.id.action_likedFragment_to_playerFragment, bundle);
                            break;
                        case "downloaded":
                            Navigation.findNavController(v).navigate(R.id.action_musicsListFragment_to_playerFragment, bundle);
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
