package com.ariaramin.radioava.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ariaramin.radioava.Room.Converters.AlbumConverter;
import com.ariaramin.radioava.Room.Converters.ArtistConverter;
import com.ariaramin.radioava.Room.Converters.MusicConverter;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;

@TypeConverters({MusicConverter.class, AlbumConverter.class, ArtistConverter.class})
@Database(entities = {AllMusicEntity.class, AllAlbumEntity.class, AllArtistEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract DatabaseDao databaseDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "RadioAva_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
