package com.ariaramin.radioava;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ariaramin.radioava.Models.Music;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    @Inject
    MainRepository mainRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<List<Music>> getTrendingMusics() {
        return mainRepository.getTrendingMusics();
    }
}
