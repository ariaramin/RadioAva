package com.ariaramin.radioava.Players;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.ariaramin.radioava.Models.Video;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayer {

    public MutableLiveData<Video> playingVideo;
    public MutableLiveData<Boolean> isPlaying;
    ExoPlayer player;

    public VideoPlayer(Context context) {
        playingVideo = new MutableLiveData<>();
        isPlaying = new MutableLiveData<>();
        player = new ExoPlayer.Builder(context).build();
        isPlaying.setValue(false);
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public void play(Video video) {
        if (player != null) {
            player.stop();
            Uri videoUri = Uri.parse(video.getSource());
            MediaItem mediaItem = MediaItem.fromUri(videoUri);
//                mediaItemPlaylist.add(mediaItem);
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
            player.play();
            playingVideo.setValue(video);
            isPlaying.setValue(true);
        }
    }

    public void pause() {
        if (player != null) {
//            if (player.isPlaying()) {
            player.pause();
            isPlaying.setValue(false);
//            }
        }
    }

    public Boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }
}
