package com.ariaramin.radioava.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.media.session.MediaButtonReceiver;
import androidx.navigation.NavDeepLinkBuilder;

import com.ariaramin.radioava.Broadcast.MusicPlayerBroadcastReceiver;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.MusicPlayer;
import com.ariaramin.radioava.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.net.URL;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MusicPlayerService extends LifecycleService {

    private final String CHANNEL_ID = "music_playback";
    RemoteViews notificationView;
    RemoteViews notificationLargeView;
    Notification notification;
    @Inject
    MusicPlayer musicPlayer;
    PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getAction() != null && intent.getAction().equals("STOP_SERVICE")) {
            stopForeground(true);
            stopSelf();
        }

        musicPlayer.playingMusic.observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                createPendingIntent(music);
                createNotification();
                updateNotificationInfo(music);
                startForeground(1, notification);
            }
        });

        musicPlayer.isPlaying.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                updateNotificationPlayBack(aBoolean);
            }
        });

        return START_NOT_STICKY;
    }

    private void updateNotificationInfo(Music music) {
        notificationView.setTextViewText(R.id.notificationNameTextView, stringCutter(music.getName(), 23));
        notificationView.setTextViewText(R.id.notificationArtistTextView, stringCutter(music.getArtist(), 32));
        notificationLargeView.setTextViewText(R.id.notificationNameTextView, stringCutter(music.getName(), 33));
        notificationLargeView.setTextViewText(R.id.notificationArtistTextView, stringCutter(music.getArtist(), 42));
        try {
            Bitmap bitmap = Glide.with(this)
                    .asBitmap()
                    .load(music.getCover())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(28)))
                    .submit(150, 150)
                    .get();
            notificationView.setImageViewBitmap(R.id.notificationImageView, bitmap);
            notificationLargeView.setImageViewBitmap(R.id.notificationImageView, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateNotification(notificationView, notificationLargeView);
    }

    private String stringCutter(String name, int length) {
        int nameLength = name.length();
        if (nameLength > length) {
            String subString = name.substring(0, length);
            return subString + "...";
        }
        return name;
    }

    private void updateNotificationPlayBack(boolean isPlaying) {
        Log.i("b", isPlaying+"");
        notificationView.setImageViewResource(R.id.notificationPlayImageView, isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        notificationLargeView.setImageViewResource(R.id.notificationPlayImageView, isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        updateNotification(notificationView, notificationLargeView);
    }

    private void updateNotification(RemoteViews view, RemoteViews largeView) {
        notification.contentView = view;
        notification.bigContentView = largeView;
        startForeground(1, notification);
    }

    private void createPendingIntent(Music music) {
        // open app intent
        Bundle bundle = new Bundle();
        bundle.putParcelable("Music", music);
        pendingIntent = new NavDeepLinkBuilder(this)
                .setComponentName(MainActivity.class)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.playerFragment)
                .setArguments(bundle)
                .createPendingIntent();
    }

    private void createNotification() {
        setNotificationViews();
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(notificationView)
                .setCustomBigContentView(notificationLargeView)
                .setColor(getResources().getColor(R.color.secondaryDarkColor))
                .setColorized(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
    }

    private void setNotificationViews() {
        // play and pause intent
        Intent playAndPauseIntent = new Intent(this, MusicPlayerBroadcastReceiver.class);
        playAndPauseIntent.setAction("PLAY/PAUSE");
        PendingIntent playAndPausePendingIntent = PendingIntent.getBroadcast(this, 1, playAndPauseIntent, 0);

        // next intent
        Intent nextIntent = new Intent(this, MusicPlayerBroadcastReceiver.class);
        nextIntent.setAction("NEXT");
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 2, nextIntent, 0);

        // prev intent
        Intent prevIntent = new Intent(this, MusicPlayerBroadcastReceiver.class);
        prevIntent.setAction("PREV");
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 3, prevIntent, 0);

        Intent deleteIntent = new Intent(this, MusicPlayerService.class);
        deleteIntent.setAction("STOP_SERVICE");
        PendingIntent deletePendingIntent = PendingIntent.getService(this, 4, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // small notification view
        notificationView = new RemoteViews(getPackageName(), R.layout.playback_notification_layout);
        notificationView.setOnClickPendingIntent(R.id.notificationPlayImageView, playAndPausePendingIntent);
        notificationView.setOnClickPendingIntent(R.id.notificationNextImageView, nextPendingIntent);
        notificationView.setOnClickPendingIntent(R.id.notificationPrevImageView, prevPendingIntent);

        // large notification view
        notificationLargeView = new RemoteViews(getPackageName(), R.layout.playback_large_notification_layout);
        notificationLargeView.setOnClickPendingIntent(R.id.notificationPlayImageView, playAndPausePendingIntent);
        notificationLargeView.setOnClickPendingIntent(R.id.notificationNextImageView, nextPendingIntent);
        notificationLargeView.setOnClickPendingIntent(R.id.notificationPrevImageView, prevPendingIntent);
        notificationLargeView.setOnClickPendingIntent(R.id.stopImageView, deletePendingIntent);
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = getString(R.string.channel_name);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
