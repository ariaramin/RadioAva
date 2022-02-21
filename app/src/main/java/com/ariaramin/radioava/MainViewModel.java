package com.ariaramin.radioava;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    @Inject
    MainRepository mainRepository;

    MutableLiveData<Integer> artistTotalMusics = new MutableLiveData<>(0);

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void clearCompositeDisposable() {
        mainRepository.clearCompositeDisposable();
    }

    public MutableLiveData<Integer> getArtistTotalMusics() {
        return artistTotalMusics;
    }

    public void setArtistTotalMusics(int totalMusics) {
        artistTotalMusics.postValue(totalMusics);
    }

    ///////////////////////////////// Music

    public Observable<List<Music>> getAllMusics() {
        return mainRepository.getAllMusics();
    }

    public void insertMusics(List<Music> musics) {
        mainRepository.insertMusics(musics);
    }

    public Flowable<List<Music>> getAllMusicsFromDb() {
        return mainRepository.getAllMusicsFromDb();
    }

    public Flowable<List<Music>> getTrendingMusicsFromDb() {
        return mainRepository.getTrendingMusicsFromDb();
    }

    public Flowable<List<Music>> getPopularMusicsFromDb() {
        return mainRepository.getPopularMusicsFromDb();
    }

    public Flowable<List<Music>> searchInMusicsFromDb(String query) {
        return mainRepository.searchInMusicsFromDb(query);
    }

    public Flowable<List<Music>> getArtistMusicsFromDb(String artist) {
        return mainRepository.getArtistMusicsFromDb(artist);
    }

    ////////////////////////////// Album

    public Observable<List<Album>> getAllAlbums() {
        return mainRepository.getAllAlbums();
    }

    public void insertAlbums(List<Album> albums) {
        mainRepository.insertAlbums(albums);
    }

    public Flowable<List<Album>> getAllAlbumsFromDb() {
        return mainRepository.getAllAlbumsFromDb();
    }

    public Flowable<List<Album>> getArtistAlbumsFromDb(String artist) {
        return mainRepository.getArtistAlbumsFromDb(artist);
    }

    public Flowable<List<Album>> searchInAlbumsFromDb(String query) {
        return mainRepository.searchInAlbumsFromDb(query);
    }

    //////////////////////////// Artist

    public Observable<List<Artist>> getAllArtists() {
        return mainRepository.getAllArtists();
    }

    public void insertArtists(List<Artist> artists) {
        mainRepository.insertArtists(artists);
    }

    public Flowable<List<Artist>> getAllArtistsFromDb() {
        return mainRepository.getAllArtistsFromDb();
    }

    public Flowable<List<Artist>> searchInArtistsFromDb(String query) {
        return mainRepository.searchInArtistsFromDb(query);
    }

    /////////////////////////// Video

    public Observable<List<Video>> getAllVideos() {
        return mainRepository.getAllVideos();
    }

    public void insertVideos(List<Video> videos) {
        mainRepository.insertVideos(videos);
    }

    public Flowable<List<Video>> getAllVideosFromDb() {
        return mainRepository.getAllVideosFromDb();
    }

    public Flowable<List<Video>> getTrendingVideosFromDb() {
        return mainRepository.getTrendingVideosFromDb();
    }

    public Flowable<List<Video>> getArtistVideosFromDb(String artist) {
        return mainRepository.getArtistVideosFromDb(artist);
    }

    public Flowable<List<Video>> searchInVideosFromDb(String query) {
        return mainRepository.searchInVideosFromDb(query);
    }

}
