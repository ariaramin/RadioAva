package com.ariaramin.radioava.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;


import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusics(AllMusicEntity allMusicEntity);

    @Query("SELECT * FROM music_tbl")
    Flowable<AllMusicEntity> readAllMusics();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(AllAlbumEntity allAlbumEntity);

    @Query("SELECT * FROM album_tbl")
    Flowable<AllAlbumEntity> readAllAlbums();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtists(AllArtistEntity allArtistEntity);

    @Query("SELECT * FROM artist_tbl")
    Flowable<AllArtistEntity> readAllArtists();
}
