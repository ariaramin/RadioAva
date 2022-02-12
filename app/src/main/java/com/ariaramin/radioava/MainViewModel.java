package com.ariaramin.radioava;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingMusicEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    @Inject
    MainRepository mainRepository;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<List<Music>> getAllMusics() {
        return mainRepository.getAllMusics();
    }

    public void insertMusics(List<Music> musics) {
        mainRepository.insertMusics(musics);
    }

    public Flowable<AllMusicEntity> getAllMusicsFromDb() {
        return mainRepository.getAllMusicsFromDb();
    }

    public Observable<List<Music>> getTrendingMusics() {
        return mainRepository.getTrendingMusics();
    }

    public void insertTrendingMusics(List<Music> musics) {
        mainRepository.insertTrendingMusics(musics);
    }

    public Flowable<TrendingMusicEntity> getTrendingMusicsFromDb() {
        return mainRepository.getTrendingMusicsFromDb();
    }

    public Observable<List<Album>> getLatestAlbums() {
        return mainRepository.getLatestAlbums();
    }

    public void insertAlbums(List<Album> albums) {
        mainRepository.insertAlbums(albums);
    }

    public Flowable<AllAlbumEntity> getAllAlbumsFromDb() {
        return mainRepository.getAllAlbumsFromDb();
    }

    public Observable<List<Artist>> getPopularArtists() {
        return mainRepository.getPopularArtists();
    }

    public void insertArtists(List<Artist> artists) {
        mainRepository.insertArtists(artists);
    }

    public Flowable<AllArtistEntity> getAllArtistsFromDb() {
        return mainRepository.getAllArtistsFromDb();
    }
}
