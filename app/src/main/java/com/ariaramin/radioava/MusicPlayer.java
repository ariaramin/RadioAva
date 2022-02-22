package com.ariaramin.radioava;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.ariaramin.radioava.Models.Music;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {

    ArrayList<MediaItem> mediaItemPlaylist;
    ArrayList<Music> musicPlaylist;
    public MutableLiveData<Music> playingMusic;
    public MutableLiveData<Boolean> isPlaying;
    MutableLiveData<Boolean> isShuffleModeEnabled;
    MutableLiveData<Boolean> isRepeatModeEnabled;
    ExoPlayer player;

    public MusicPlayer(Context context) {
        mediaItemPlaylist = new ArrayList<>();
        musicPlaylist = new ArrayList<>();
        playingMusic = new MutableLiveData<>();
        isPlaying = new MutableLiveData<>();
        isShuffleModeEnabled = new MutableLiveData<>();
        isRepeatModeEnabled = new MutableLiveData<>();
        player = new ExoPlayer.Builder(context).build();
        isPlaying.setValue(false);
    }

    public void play() {
        if (player != null) {
            player.stop();
            if (!mediaItemPlaylist.isEmpty()) {
                player.setMediaItems(mediaItemPlaylist, true);
                player.prepare();
                player.setPlayWhenReady(true);
                player.play();
                isPlaying.setValue(true);
            }
        }
    }

    public void playMusic(Music music) {
        if (player != null) {
            player.stop();
        }
        Uri musicUri = Uri.parse(music.getSource());
        MediaItem mediaItem = MediaItem.fromUri(musicUri);
        playingMusic.setValue(music);
        player.clearMediaItems();
        mediaItemPlaylist.add(mediaItem);
        player.setMediaItems(mediaItemPlaylist, true);
        player.prepare();
        player.setPlayWhenReady(true);
        player.play();
        isPlaying.setValue(true);
    }

    public void pause() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                isPlaying.setValue(false);
            }
        }
    }

    public void resume() {
        if (player != null) {
            if (!player.isPlaying()) {
                player.play();
                isPlaying.setValue(true);
            }
        }
    }

    public void stop() {
        if (player != null) {
            player.release();
            player.clearMediaItems();
        }
    }

    public Boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void seekTo(int progress) {
        if (player != null) {
            player.seekTo((player.getDuration() / 100) * progress);
        }
    }

    public void togglePlayBack() {
        if (player != null) {
            if (isPlaying()) {
                pause();
                isPlaying.setValue(false);
            } else {
                resume();
                isPlaying.setValue(true);
            }
        }
    }

    public void next() {
        if (player != null) {
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem();
                int musicPosition = player.getCurrentMediaItemIndex();
                playingMusic.setValue(musicPlaylist.get(musicPosition));
            } else {
                player.seekTo(0, 0);
            }
        }
    }

    public void previous() {
        if (player != null) {
            if (player.hasPreviousMediaItem()) {
                player.seekToPreviousMediaItem();
                int musicPosition = player.getCurrentMediaItemIndex();
                playingMusic.setValue(musicPlaylist.get(musicPosition));
            } else {
                player.seekTo(mediaItemPlaylist.size() - 1, 0);
            }
        }
    }

    public void enableShuffleMode() {
        if (player != null) {
            player.setShuffleModeEnabled(true);
            isShuffleModeEnabled.setValue(true);
        }
    }

    public void disableShuffleMode() {
        if (player != null) {
            player.setShuffleModeEnabled(false);
            isShuffleModeEnabled.setValue(false);
        }
    }

    public void toggleShuffleMode() {
        if (player != null) {
            if (player.getShuffleModeEnabled()) {
                disableShuffleMode();
            } else {
                enableShuffleMode();
            }
        }
    }

    public void enableRepeatMode() {
        if (player != null) {
            player.setRepeatMode(player.REPEAT_MODE_ALL);
            isRepeatModeEnabled.setValue(true);
        }
    }

    public void disableRepeatMode() {
        if (player != null) {
            player.setRepeatMode(player.REPEAT_MODE_OFF);
            isRepeatModeEnabled.setValue(false);
        }
    }

    public void toggleRepeatMode() {
        if (player != null) {
            if (player.getRepeatMode() == player.REPEAT_MODE_ALL) {
                disableRepeatMode();
            } else {
                enableRepeatMode();
            }
        }
    }

    public void addMusicToPlaylist(Music music) {
        Uri musicUri = Uri.parse(music.getSource());
        MediaItem mediaItem = MediaItem.fromUri(musicUri);
        mediaItemPlaylist.add(mediaItem);
        musicPlaylist.add(music);
    }

    public void addPlaylist(List<Music> musicList) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for (int i = 0; i < musicList.size(); i++) {
            Uri musicUri = Uri.parse(musicList.get(i).getSource());
            MediaItem mediaItem = MediaItem.fromUri(musicUri);
            mediaItems.add(mediaItem);
        }
        mediaItemPlaylist = mediaItems;
        musicPlaylist = new ArrayList<>(musicList);
        playingMusic.setValue(musicList.get(0));
    }
}
