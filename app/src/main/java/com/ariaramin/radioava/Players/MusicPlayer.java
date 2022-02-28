package com.ariaramin.radioava.Players;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.ariaramin.radioava.Models.Music;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayer implements Player.Listener {

    ArrayList<MediaItem> mediaItemPlaylist;
    ArrayList<Music> musicPlaylist;
    public MutableLiveData<Music> playingMusic;
    public MutableLiveData<Boolean> isPlaying;
    public MutableLiveData<Long> duration;
    public MutableLiveData<Long> currentPosition;
    public MutableLiveData<Boolean> isShuffleModeEnabled;
    public MutableLiveData<Boolean> isRepeatModeEnabled;
    ExoPlayer player;

    public MusicPlayer(Context context) {
        mediaItemPlaylist = new ArrayList<>();
        musicPlaylist = new ArrayList<>();
        playingMusic = new MutableLiveData<>();
        isPlaying = new MutableLiveData<>();
        duration = new MutableLiveData<>(0L);
        currentPosition = new MutableLiveData<>();
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
                player.addListener(this);
                player.prepare();
                player.setPlayWhenReady(true);
                player.play();
                isPlaying.setValue(true);
            }
        }
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        if (player.getMediaItemCount() > 1) {
            int musicPosition = player.getCurrentMediaItemIndex();
            playingMusic.setValue(musicPlaylist.get(musicPosition));
            if (player.getContentDuration() > 0) {
                duration.setValue(player.getContentDuration());
            }
            isPlaying.setValue(true);
        }
    }

    @Override
    public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == ExoPlayer.STATE_READY) {
            duration.setValue(player.getContentDuration());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    long currentPos = player.getCurrentPosition();
                    currentPosition.setValue(currentPos);
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
        }
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

//    public void stop() {
//        if (player != null) {
//            player.stop();
//            isPlaying.setValue(false);
//        }
//    }

//    public void release() {
//        if (player != null) {
//            player.release();
//        }
//    }

    public Boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void seekTo(float progress) {
        if (player != null) {
            long currentPos = (long) (progress * 1000);
            player.seekTo(currentPos);
            currentPosition.setValue(currentPos);
        }
    }

    public void togglePlayBack() {
        if (player != null) {
            if (isPlaying()) {
                pause();
            } else {
                resume();
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
                if (player.getRepeatMode() == player.REPEAT_MODE_ONE) {
                    disableRepeatMode();
                }
                enableShuffleMode();
            }
        }
    }

    public void enableRepeatMode() {
        if (player != null) {
            player.setRepeatMode(player.REPEAT_MODE_ONE);
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
            if (player.getRepeatMode() == player.REPEAT_MODE_ONE) {
                disableRepeatMode();
            } else {
                if (player.getShuffleModeEnabled()) {
                    disableShuffleMode();
                }
                enableRepeatMode();
            }
        }
    }

    public void addMusicToPlaylist(Music music) {
        player.clearMediaItems();
        mediaItemPlaylist.clear();
        musicPlaylist.clear();
        Uri musicUri = Uri.parse(music.getSource());
        MediaItem mediaItem = MediaItem.fromUri(musicUri);
        mediaItemPlaylist.add(mediaItem);
        musicPlaylist.add(music);
        playingMusic.setValue(music);
    }

    public void setNewPlaylist(List<Music> musicList) {
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

    public void addPlaylist(List<Music> musicList) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        for (int i = 0; i < musicList.size(); i++) {
            Uri musicUri = Uri.parse(musicList.get(i).getSource());
            MediaItem mediaItem = MediaItem.fromUri(musicUri);
            mediaItems.add(mediaItem);
        }
        if (!mediaItemPlaylist.containsAll(mediaItems) && !musicPlaylist.containsAll(musicList)) {
            mediaItemPlaylist.addAll(mediaItems);
            musicPlaylist.addAll(musicList);
        }
    }
}
