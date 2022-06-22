package com.ariaramin.radioava;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Retrofit.RequestApi;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MainRepository {

    RequestApi requestApi;

    public MainRepository(RequestApi requestApi) {
        this.requestApi = requestApi;
    }

    ///////////////////////////////////// Music

    public Observable<List<Music>> getAllMusics() {
        return requestApi.getAllMusics();
    }

    public Observable<List<Music>> getTrendingMusics() {
        return requestApi.getTrendingMusics();
    }

    public Observable<List<Music>> getPopularMusics() {
        return requestApi.getPopularMusics();
    }

    public Observable<List<Music>> getArtistMusics(String artist) {
        return requestApi.getArtistMusics(artist);
    }

    public Observable<List<Music>> searchInMusics(String search, int limit) {
        return requestApi.searchInMusics(search, limit);
    }

    ///////////////////////////////////// Album

    public Observable<List<Album>> getAllAlbums() {
        return requestApi.getAllAlbums();
    }

    public Observable<List<Album>> getArtistAlbums(String artist) {
        return requestApi.getArtistAlbums(artist);
    }

    public Observable<List<Album>> searchInAlbums(String search, int limit) {
        return requestApi.searchInAlbums(search, limit);
    }


    ////////////////////////////////////// Artist

    public Observable<List<Artist>> getAllArtists() {
        return requestApi.getAllArtists();
    }

    public Observable<List<Artist>> searchInArtists(String search, int limit) {
        return requestApi.searchInArtists(search, limit);
    }


    /////////////////////////////////////// Video
    public Observable<List<Video>> getAllVideos() {
        return requestApi.getAllVideos();
    }

    public Observable<List<Video>> getTrendingVideos() {
        return requestApi.getTrendingVideos();
    }

    public Observable<List<Video>> getArtistVideos(String artist) {
        return requestApi.getArtistVideos(artist);
    }

    public Observable<List<Video>> searchInVideos(String search, int limit) {
        return requestApi.searchInVideos(search, limit);
    }

}
