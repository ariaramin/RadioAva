package com.ariaramin.radioava.Retrofit;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("music/list/")
    Observable<List<Music>> getAllMusics();

    @GET("trending/music/list/")
    Observable<List<Music>> getTrendingMusics();

    @GET("latest/music/list/")
    Observable<List<Music>> getLatestMusics();

    @GET("popular/music/list/")
    Observable<List<Music>> getPopularMusics();

    @GET("album/list/")
    Observable<List<Album>> getLatestAlbums();

    @GET("artist/list/")
    Observable<List<Artist>> getAllArtists();

    @GET("trending/video/list/")
    Observable<List<Video>> getTrendingVideos();

    @GET("latest/video/list/")
    Observable<List<Video>> getLatestVideos();
}
