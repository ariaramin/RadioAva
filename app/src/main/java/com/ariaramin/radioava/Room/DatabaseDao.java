package com.ariaramin.radioava.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;
import com.ariaramin.radioava.Room.Entities.AllVideoEntity;
import com.ariaramin.radioava.Room.Entities.PopularMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingVideoEntity;


import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMusics(AllMusicEntity allMusicEntity);

    @Query("SELECT * FROM music_tbl")
    Flowable<AllMusicEntity> readAllMusics();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrendingMusics(TrendingMusicEntity trendingMusicEntity);

    @Query("SELECT * FROM trending_music_tbl")
    Flowable<TrendingMusicEntity> readTrendingMusics();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPopularMusics(PopularMusicEntity popularMusicEntity);

    @Query("SELECT * FROM popular_music_tbl")
    Flowable<PopularMusicEntity> readPopularMusics();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(AllAlbumEntity allAlbumEntity);

    @Query("SELECT * FROM album_tbl")
    Flowable<AllAlbumEntity> readAllAlbums();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArtists(AllArtistEntity allArtistEntity);

    @Query("SELECT * FROM artist_tbl")
    Flowable<AllArtistEntity> readAllArtists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVideos(AllVideoEntity allVideoEntity);

    @Query("SELECT * FROM video_tbl")
    Flowable<AllVideoEntity> readAllVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrendingVideo(TrendingVideoEntity trendingVideoEntity);

    @Query("SELECT * FROM trending_video_tbl")
    Flowable<TrendingVideoEntity> readTrendingVideos();
}
