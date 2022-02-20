package com.ariaramin.radioava;

import android.util.Log;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Retrofit.RequestApi;
import com.ariaramin.radioava.Room.DatabaseDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainRepository {

    RequestApi requestApi;
    DatabaseDao databaseDao;
    CompositeDisposable compositeDisposable;

    public MainRepository(RequestApi requestApi, DatabaseDao databaseDao) {
        this.requestApi = requestApi;
        this.databaseDao = databaseDao;
        compositeDisposable = new CompositeDisposable();
    }

    public void clearCompositeDisposable() {
        compositeDisposable.clear();
    }

    ///////////////////////////////////// Music

    public Observable<List<Music>> getAllMusics() {
        return requestApi.getAllMusics();
    }

    public void insertMusics(List<Music> musics) {
        Completable.fromAction(() -> databaseDao.insertMusics(musics))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
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

    public Flowable<List<Music>> getAllMusicsFromDb() {
        return databaseDao.readAllMusics();
    }

    public Flowable<List<Music>> getTrendingMusicsFromDb() {
        return databaseDao.readTrendingMusics();
    }

    public Flowable<List<Music>> getPopularMusicsFromDb() {
        return databaseDao.readPopularMusics();
    }

    public Flowable<List<Music>> getArtistMusicsFromDb(String artist) {
        return databaseDao.readArtistMusics(artist);
    }

    public Flowable<List<Music>> searchInMusicsFromDb(String query) {
        return databaseDao.searchInMusics(query);
    }

    ///////////////////////////////////// Album

    public Observable<List<Album>> getAllAlbums() {
        return requestApi.getAllAlbums();
    }

    public void insertAlbums(List<Album> albums) {
        Completable.fromAction(() -> databaseDao.insertAlbums(albums))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
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

    public Flowable<List<Album>> getAllAlbumsFromDb() {
        return databaseDao.readAllAlbums();
    }

    public Flowable<List<Album>> getArtistAlbumsFromDb(String artist) {
        return databaseDao.readArtistAlbums(artist);
    }

    public Flowable<List<Album>> searchInAlbumsFromDb(String query) {
        return databaseDao.searchInAlbums(query);
    }


    ////////////////////////////////////// Artist

    public Observable<List<Artist>> getAllArtists() {
        return requestApi.getAllArtists();
    }

    public void insertArtists(List<Artist> artists) {
        Completable.fromAction(() -> databaseDao.insertArtists(artists))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
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

    public Flowable<List<Artist>> getAllArtistsFromDb() {
        return databaseDao.readAllArtists();
    }

    public Flowable<List<Artist>> searchInArtistsFromDb(String query) {
        return databaseDao.searchInArtists(query);
    }


    /////////////////////////////////////// Video
    public Observable<List<Video>> getAllVideos() {
        return requestApi.getAllVideos();
    }

    public void insertVideos(List<Video> videos) {
        Completable.fromAction(() -> databaseDao.insertVideos(videos))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertVideo", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<List<Video>> getAllVideosFromDb() {
        return databaseDao.readAllVideos();
    }

    public Flowable<List<Video>> getArtistVideosFromDb(String artist) {
        return databaseDao.readArtistVideos(artist);
    }

    public Flowable<List<Video>> searchInVideosFromDb(String query) {
        return databaseDao.searchInVideos(query);
    }

}
