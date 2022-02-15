package com.ariaramin.radioava;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

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

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void clearCompositeDisposable() {
        mainRepository.clearCompositeDisposable();
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
}
