package com.ariaramin.radioava.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;

@Database(entities = {AllMusicEntity.class, AllAlbumEntity.class, AllArtistEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "radioava_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
