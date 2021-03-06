package com.ariaramin.radioava.Hilt.Modules;

import android.content.Context;

import com.ariaramin.radioava.Players.MusicPlayer;
import com.ariaramin.radioava.Players.VideoPlayer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class HiltPlayerModule {

    @Provides
    @Singleton
    MusicPlayer ProvideMusicPlayer(@ApplicationContext Context context) {
        return new MusicPlayer(context);
    }

    @Provides
    @Singleton
    VideoPlayer ProvideVideoPlayer(@ApplicationContext Context context) {
        return new VideoPlayer(context);
    }
}
