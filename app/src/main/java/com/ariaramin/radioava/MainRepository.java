package com.ariaramin.radioava;

import android.util.Log;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Retrofit.RequestApi;
import com.ariaramin.radioava.Room.DatabaseDao;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepository {

    RequestApi requestApi;
    DatabaseDao databaseDao;

    public MainRepository(RequestApi requestApi, DatabaseDao databaseDao) {
        this.requestApi = requestApi;
        this.databaseDao = databaseDao;
    }

    public Observable<List<Music>> getAllMusics() {
        return requestApi.getAllMusics();
    }

    public void insertMusics(List<Music> musics) {
        Completable.fromAction(() -> databaseDao.insertMusics(new AllMusicEntity(musics)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertMusic", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<AllMusicEntity> getAllMusicsFromDb() {
        return databaseDao.readAllMusics();
    }

    public Observable<List<Album>> getLatestAlbums() {
        return requestApi.getLatestAlbums();
    }

    public void insertAlbums(List<Album> albums) {
        Completable.fromAction(() -> databaseDao.insertAlbums(new AllAlbumEntity(albums)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertAlbum", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<AllAlbumEntity> getAllAlbumsFromDb() {
        return databaseDao.readAllAlbums();
    }

    public Observable<List<Artist>> getPopularArtists() {
        return requestApi.getPopularArtists();
    }

    public void insertArtists(List<Artist> artists) {
        Completable.fromAction(() -> databaseDao.insertArtists(new AllArtistEntity(artists)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertArtist", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<AllArtistEntity> getAllArtistsFromDb() {
        return databaseDao.readAllArtists();
    }
}
