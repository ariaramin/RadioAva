package com.ariaramin.radioava.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ariaramin.radioava.Players.MusicPlayer;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MusicPlayerBroadcastReceiver extends BroadcastReceiver {

    @Inject
    MusicPlayer musicPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case "PLAY/PAUSE":
                musicPlayer.togglePlayBack();
                break;
            case "NEXT":
                musicPlayer.next();
                break;
            case "PREV":
                musicPlayer.previous();
                break;
        }
    }
}
