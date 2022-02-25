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

    ////////////////////////////////////////////////////////////// Music
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusics(List<Music> musicList);

    @Transaction
    @Query("SELECT * FROM music_tbl ORDER BY releaseDate DESC")
    Flowable<List<Music>> readAllMusics();

    @Transaction
    @Query("SELECT * FROM music_tbl WHERE type='trending' AND album IS NULL ORDER BY releaseDate DESC")
    Flowable<List<Music>> readTrendingMusics();

    @Transaction
    @Query("SELECT * FROM music_tbl WHERE type='popular' AND album IS NULL ORDER BY releaseDate DESC")
    Flowable<List<Music>> readPopularMusics();

    @Transaction
    @Query("SELECT * FROM music_tbl WHERE name LIKE '%' || :query || '%' LIMIT 20")
    Flowable<List<Music>> searchInMusics(String query);

    @Transaction
    @Query("SELECT * FROM music_tbl WHERE artist=:artist " +
            "OR artist LIKE '%& ' || :artist || '%'" +
            "OR artist LIKE '%' || :artist || ' &%'" +
            "OR artist LIKE '% ,' || :artist || '%'" +
            "OR artist LIKE '%' || :artist || ',%'" +
            "OR artist LIKE '% ,' || :artist || ',%'" +
            "OR name LIKE '%Ft ' || :artist || '%'" +
            "OR name LIKE '%& ' || :artist || '%'" +
            "OR name LIKE '% ,' || :artist || '%'" +
            "OR name LIKE '%' || :artist || ',%'" +
            "OR name LIKE '% ,' || :artist || ',%'" +
            "AND album IS NULL ORDER BY releaseDate DESC")
    Flowable<List<Music>> readArtistMusics(String artist);

    /////////////////////////////////////////////////////////// Album
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albumList);

    @Transaction
    @Query("SELECT * FROM album_tbl ORDER BY releaseDate DESC")
    Flowable<List<Album>> readAllAlbums();

    @Transaction
    @Query("SELECT * FROM album_tbl WHERE name LIKE '%' || :query || '%' LIMIT 20")
    Flowable<List<Album>> searchInAlbums(String query);

    @Transaction
    @Query("SELECT * FROM album_tbl WHERE artist LIKE '%' || :artist || '%' ORDER BY releaseDate DESC")
    Flowable<List<Album>> readArtistAlbums(String artist);

    //////////////////////////////////////////////////////////// Artist
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtists(List<Artist> artistList);

    @Transaction
    @Query("SELECT * FROM artist_tbl WHERE name LIKE '%' || :query || '%' LIMIT 20")
    Flowable<List<Artist>> searchInArtists(String query);

    @Transaction
    @Query("SELECT * FROM artist_tbl")
    Flowable<List<Artist>> readAllArtists();

    //////////////////////////////////////////////////////////// Video
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(List<Video> videoList);

    @Transaction
    @Query("SELECT * FROM video_tbl ORDER BY releaseDate DESC")
    Flowable<List<Video>> readAllVideos();

    @Transaction
    @Query("SELECT * FROM video_tbl WHERE type='trending' ORDER BY releaseDate DESC")
    Flowable<List<Video>> readTrendingVideos();

    @Transaction
    @Query("SELECT * FROM video_tbl WHERE name LIKE '%' || :query || '%' LIMIT 20")
    Flowable<List<Video>> searchInVideos(String query);

    @Transaction
    @Query("SELECT * FROM video_tbl WHERE artist LIKE '%' || :artist || '%' ORDER BY releaseDate DESC")
    Flowable<List<Video>> readArtistVideos(String artist);
}
