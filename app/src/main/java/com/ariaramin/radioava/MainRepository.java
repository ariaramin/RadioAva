package com.ariaramin.radioava;

import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Retrofit.RequestApi;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class MainRepository {

    RequestApi requestApi;

    public MainRepository(RequestApi requestApi) {
        this.requestApi = requestApi;
    }

    public Observable<List<Music>> getTrendingMusics() {
        return requestApi.getTrendingMusics();
    }
}
