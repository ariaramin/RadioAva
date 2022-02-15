package com.ariaramin.radioava.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;


import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DatabaseDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusics(List<Music> musicList);

    @Transaction
    @Query("SELECT * FROM music_tbl ORDER BY releaseDate DESC")
    Flowable<List<Music>> readAllMusics();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albumList);

    @Transaction
    @Query("SELECT * FROM album_tbl ORDER BY releaseDate DESC")
    Flowable<List<Album>> readAllAlbums();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtists(List<Artist> artistList);

    @Transaction
    @Query("SELECT * FROM artist_tbl")
    Flowable<List<Artist>> readAllArtists();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(List<Video> videoList);

    @Transaction
    @Query("SELECT * FROM video_tbl ORDER BY releaseDate DESC")
    Flowable<List<Video>> readAllVideos();
}
