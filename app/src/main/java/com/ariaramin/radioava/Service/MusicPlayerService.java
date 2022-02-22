package com.ariaramin.radioava.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

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
    RemoteViews notificationLayout;
    RemoteViews notificationLargeLayout;
    Notification notification;
    @Inject
    MusicPlayer musicPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getAction() != null && intent.getAction().equals("STOP_SERVICE")) {
            stopForeground(true);
            stopSelf();
        }

        createNotificationChannel();
        createNotification();
        startForeground(1, notification);

        musicPlayer.playingMusic.observe(this, new Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                updateNotificationInfo(music.getName(), music.getArtist(), music.getCover());
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

    private void updateNotificationInfo(String musicName, String musicArtist, String musicCover) {
        notificationLayout.setTextViewText(R.id.notificationNameTextView, stringCutter(musicName, 23));
        notificationLayout.setTextViewText(R.id.notificationArtistTextView, stringCutter(musicArtist, 32));
        notificationLargeLayout.setTextViewText(R.id.notificationNameTextView, stringCutter(musicName, 33));
        notificationLargeLayout.setTextViewText(R.id.notificationArtistTextView, stringCutter(musicArtist, 42));
        try {
            Bitmap bitmap = Glide.with(this)
                    .asBitmap()
                    .load(musicCover)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(28)))
                    .submit(100, 100)
                    .get();
            notificationLayout.setImageViewBitmap(R.id.notificationImageView, bitmap);
            notificationLargeLayout.setImageViewBitmap(R.id.notificationImageView, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateNotification(notificationLayout, notificationLargeLayout);
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
        notificationLayout.setImageViewResource(R.id.notificationPlayImageView, isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        notificationLargeLayout.setImageViewResource(R.id.notificationPlayImageView, isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
        updateNotification(notificationLayout, notificationLargeLayout);
    }

    private void updateNotification(RemoteViews view, RemoteViews largeView) {
        notification.contentView = view;
        notification.bigContentView = largeView;
        startForeground(1, notification);
    }

    private void createNotification() {
        // open app intent
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        setNotificationLayout();
        setNotificationLargeLayout();

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLargeLayout)
                .setColor(getResources().getColor(R.color.secondaryDarkColor))
                .setColorized(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
    }

    private void setNotificationLayout() {
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

        notificationLayout = new RemoteViews(getPackageName(), R.layout.playback_notification_layout);
        notificationLayout.setOnClickPendingIntent(R.id.notificationPlayImageView, playAndPausePendingIntent);
        notificationLayout.setOnClickPendingIntent(R.id.notificationNextImageView, nextPendingIntent);
        notificationLayout.setOnClickPendingIntent(R.id.notificationPrevImageView, prevPendingIntent);
    }

    private void setNotificationLargeLayout() {
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

        notificationLargeLayout = new RemoteViews(getPackageName(), R.layout.playback_large_notification_layout);
        notificationLargeLayout.setOnClickPendingIntent(R.id.notificationPlayImageView, playAndPausePendingIntent);
        notificationLargeLayout.setOnClickPendingIntent(R.id.notificationNextImageView, nextPendingIntent);
        notificationLargeLayout.setOnClickPendingIntent(R.id.notificationPrevImageView, prevPendingIntent);
        notificationLargeLayout.setOnClickPendingIntent(R.id.stopImageView, deletePendingIntent);
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
        Log.i("d", "destroyed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
