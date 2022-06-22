package com.ariaramin.radioava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Players.MusicPlayer;
import com.ariaramin.radioava.Service.MusicPlayerService;
import com.ariaramin.radioava.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    MainViewModel mainViewModel;
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
        bottomNavigationView = mainBinding.bottomNavigationView;
        homeImageView = mainBinding.homeIcon;
        playBackLayout = mainBinding.playBackLayout;

        musicPlayer.playingMusic.observe(this, music -> {
            if (music != null) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerService.class);
                startService(intent);
                setupPlayBack(music);
            }
        });
        setupBottomNavigationView();
    }

    private void setupPlayBack(Music music) {
        mainBinding.playBackLayout.setOnClickListener(v -> {
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
            NavController navController = navHostFragment.getNavController();
            Bundle bundle = new Bundle();
            bundle.putParcelable("Music", music);
            navController.navigate(R.id.playerFragment, bundle);
        });
        loadMusicImage(music);
        mainBinding.playBackNameTextView.setText(stringCutter(music.getName(), 27));
        mainBinding.playBackArtistTextView.setText(stringCutter(music.getArtist(), 42));
        setupButtons();
    }

    private void loadMusicImage(Music music) {
        Glide.with(getApplicationContext())
                .load(music.getCover())
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .override(150, 150)
                .into(mainBinding.playBackImageView);
    }

    private void setupButtons() {
        mainBinding.playBackNextImageView.setOnClickListener(v -> musicPlayer.next());
        mainBinding.playBackPrevImageView.setOnClickListener(v -> musicPlayer.previous());
        musicPlayer.isPlaying.observe(this, aBoolean ->
                mainBinding.playBackPlayImageView.setImageResource(aBoolean ? R.drawable.ic_pause : R.drawable.ic_play)
        );
        mainBinding.playBackPlayImageView.setOnClickListener(v -> musicPlayer.togglePlayBack());
    }

    private String stringCutter(String name, int length) {
        int nameLength = name.length();
        if (nameLength > length) {
            String subString = name.substring(0, length);
            return subString + "...";
        }
        return name;
    }

//    private void checkNetworkConnection() {
//        networkCallback = new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(@androidx.annotation.NonNull Network network) {
//                super.onAvailable(network);
//
//            }
//
//            @Override
//            public void onLost(@androidx.annotation.NonNull Network network) {
//                super.onLost(network);
//                Toast.makeText(getApplicationContext(), "Check your network connection.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            connectivityManager.registerDefaultNetworkCallback(networkCallback);
//        } else {
//            connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
//        }
//    }

    private void setupBottomNavigationView() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mainBinding.bottomNavigationView, navController);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        connectivityManager.unregisterNetworkCallback(networkCallback);
//    }
}