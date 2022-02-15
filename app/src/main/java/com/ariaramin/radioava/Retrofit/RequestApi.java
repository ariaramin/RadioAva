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

    @GET("album/list/")
    Observable<List<Album>> getAllAlbums();

    @GET("artist/list/")
    Observable<List<Artist>> getAllArtists();

    @GET("video/list/")
    Observable<List<Video>> getAllVideos();
}
