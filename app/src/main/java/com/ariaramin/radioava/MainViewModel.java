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

    public Observable<List<Music>> getTrendingMusics() {
        return mainRepository.getTrendingMusics();
    }

    public Observable<List<Music>> getPopularMusics() {
        return mainRepository.getPopularMusics();
    }

    public Observable<List<Music>> searchInMusics(String search, int limit) {
        return mainRepository.searchInMusics(search, limit);
    }

    public Observable<List<Music>> getArtistMusics(String artist) {
        return mainRepository.getArtistMusics(artist);
    }

    ////////////////////////////// Album

    public Observable<List<Album>> getAllAlbums() {
        return mainRepository.getAllAlbums();
    }

    public Observable<List<Album>> getArtistAlbums(String artist) {
        return mainRepository.getArtistAlbums(artist);
    }

    public Observable<List<Album>> searchInAlbums(String search, int limit) {
        return mainRepository.searchInAlbums(search, limit);
    }

    //////////////////////////// Artist

    public Observable<List<Artist>> getAllArtists() {
        return mainRepository.getAllArtists();
    }

    public Observable<List<Artist>> searchInArtists(String search, int limit) {
        return mainRepository.searchInArtists(search, limit);
    }

    /////////////////////////// Video

    public Observable<List<Video>> getAllVideos() {
        return mainRepository.getAllVideos();
    }

    public Observable<List<Video>> getTrendingVideos() {
        return mainRepository.getTrendingVideos();
    }

    public Observable<List<Video>> getArtistVideos(String artist) {
        return mainRepository.getArtistVideos(artist);
    }

    public Observable<List<Video>> searchInVideos(String search, int limit) {
        return mainRepository.searchInVideos(search, limit);
    }

}
