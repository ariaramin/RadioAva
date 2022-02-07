package com.ariaramin.radioava.Retrofit;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("trending/music/list/")
    Observable<List<Music>> getTrendingMusics();

    @GET("latest/music/list/")
    Observable<List<Music>> getLatestMusics();

    @GET("album/list/")
    Observable<List<Album>> getLatestAlbums();

    @GET("artist/list/")
    Observable<List<Artist>> getPopularArtists();
}
