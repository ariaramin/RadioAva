package com.ariaramin.radioava.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ariaramin.radioava.MusicPlayer;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MusicPlayerBroadcastReceiver extends BroadcastReceiver {

    @Inject
    MusicPlayer musicPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("PLAY/PAUSE") || action.equals(Intent.ACTION_MEDIA_BUTTON)) {
            musicPlayer.togglePlayBack();
        } else if (action.equals("NEXT")) {
            musicPlayer.next();
        } else if (action.equals("PREV")) {
            musicPlayer.previous();
        }
    }
}
