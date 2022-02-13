package com.ariaramin.radioava;

import android.util.Log;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Retrofit.RequestApi;
import com.ariaramin.radioava.Room.DatabaseDao;
import com.ariaramin.radioava.Room.Entities.AllAlbumEntity;
import com.ariaramin.radioava.Room.Entities.AllArtistEntity;
import com.ariaramin.radioava.Room.Entities.AllMusicEntity;
import com.ariaramin.radioava.Room.Entities.AllVideoEntity;
import com.ariaramin.radioava.Room.Entities.PopularMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingMusicEntity;
import com.ariaramin.radioava.Room.Entities.TrendingVideoEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
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

    public Observable<List<Music>> getTrendingMusics() {
        return requestApi.getTrendingMusics();
    }

    public void insertTrendingMusics(List<Music> musics) {
        Completable.fromAction(() -> databaseDao.insertTrendingMusics(new TrendingMusicEntity(musics)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertTrendingMusic", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<TrendingMusicEntity> getTrendingMusicsFromDb() {
        return databaseDao.readTrendingMusics();
    }

    public Observable<List<Music>> getPopularMusics() {
        return requestApi.getPopularMusics();
    }

    public void insertPopularMusics(List<Music> musics) {
        Completable.fromAction(() -> databaseDao.insertPopularMusics(new PopularMusicEntity(musics)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertPopularMusic", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<PopularMusicEntity> getPopularMusicsFromDb() {
        return databaseDao.readPopularMusics();
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

    public Observable<List<Artist>> getAllArtists() {
        return requestApi.getAllArtists();
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

    public Observable<List<Video>> getAllVideos() {
        return requestApi.getLatestVideos();
    }

    public void insertVideos(List<Video> videos) {
        Completable.fromAction(() -> databaseDao.insertVideos(new AllVideoEntity(videos)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

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

    public Flowable<AllVideoEntity> getAllVideosFromDb() {
        return databaseDao.readAllVideos();
    }

    public Observable<List<Video>> getTrendingVideos() {
        return requestApi.getLatestVideos();
    }

    public void insertTrendingVideos(List<Video> videos) {
        Completable.fromAction(() -> databaseDao.insertTrendingVideo(new TrendingVideoEntity(videos)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("insertTrendingVideo", "Completed");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    public Flowable<TrendingVideoEntity> getTrendingVideosFromDb() {
        return databaseDao.readTrendingVideos();
    }
}
