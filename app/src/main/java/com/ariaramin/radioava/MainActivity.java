package com.ariaramin.radioava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Artist;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Service.MusicPlayerService;
import com.ariaramin.radioava.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    @Inject
    ConnectivityManager connectivityManager;
    @Inject
    NetworkRequest networkRequest;
    ConnectivityManager.NetworkCallback networkCallback;
    public BottomNavigationView bottomNavigationView;
    public ImageView homeImageView;
    public RelativeLayout playBackLayout;
    @Inject
    MusicPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        bottomNavigationView = mainBinding.bottomNavigationView;
        homeImageView = mainBinding.homeIcon;
        playBackLayout = mainBinding.playBackLayout;

        musicPlayer.playingMusic.observe(this, new androidx.lifecycle.Observer<Music>() {
            @Override
            public void onChanged(Music music) {
                if (music != null) {
                    Intent intent = new Intent(getApplicationContext(), MusicPlayerService.class);
                    startService(intent);
                    setupPlayBack(music);
                }
            }
        });

        checkNetworkConnection();
        setupBottomNavigationView();
    }

    private void setupPlayBack(Music music) {
//        mainBinding.playBackLayout.setVisibility(View.VISIBLE);
        mainBinding.playBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
                NavController navController = navHostFragment.getNavController();
                Bundle bundle = new Bundle();
                bundle.putParcelable("Music", music);
                navController.navigate(R.id.playerFragment, bundle);
            }
        });
        Glide.with(getApplicationContext())
                .load(music.getCover())
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(150, 150)
                .into(mainBinding.playBackImageView);
        mainBinding.playBackNameTextView.setText(stringCutter(music.getName(), 29));
        mainBinding.playBackArtistTextView.setText(stringCutter(music.getArtist(), 42));
        setupButtons();
    }

    private void setupButtons() {
        mainBinding.playBackNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.next();
            }
        });
        mainBinding.playBackPrevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.previous();
            }
        });
        musicPlayer.isPlaying.observe(this, new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mainBinding.playBackPlayImageView.setImageResource(aBoolean ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });
        mainBinding.playBackPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.togglePlayBack();
            }
        });
    }

    private String stringCutter(String name, int length) {
        int nameLength = name.length();
        if (nameLength > length) {
            String subString = name.substring(0, length);
            return subString + "...";
        }
        return name;
    }

    private void checkNetworkConnection() {
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@androidx.annotation.NonNull Network network) {
                super.onAvailable(network);
                getAllMusics();
                getAllAlbums();
                getAllArtists();
                getAllVideos();
            }

            @Override
            public void onLost(@androidx.annotation.NonNull Network network) {
                super.onLost(network);
                Toast.makeText(getApplicationContext(), "Check your network connection.", Toast.LENGTH_SHORT).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        }
    }

    private void getAllMusics() {
        mainViewModel.getAllMusics()
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Music> music) {
                        mainViewModel.insertMusics(music);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getAllAlbums() {
        mainViewModel.getAllAlbums()
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Album>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Album> albums) {
                        mainViewModel.insertAlbums(albums);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getAllArtists() {
        mainViewModel.getAllArtists()
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Artist> artists) {
                        mainViewModel.insertArtists(artists);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getAllVideos() {
        mainViewModel.getAllVideos()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Video>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull List<Video> videos) {
                        mainViewModel.insertVideos(videos);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setupBottomNavigationView() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mainBinding.bottomNavigationView, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.clearCompositeDisposable();
        compositeDisposable.clear();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }
}