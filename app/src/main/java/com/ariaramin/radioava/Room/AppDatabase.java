package com.ariaramin.radioava.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Room.Converters.AlbumConverter;

@TypeConverters({AlbumConverter.class})
@Database(entities = {
        Music.class,
        Album.class,
        Artist.class,
        Video.class}, version = 1)
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
