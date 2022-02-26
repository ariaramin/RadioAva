package com.ariaramin.radioava.ui.Fragments.Music;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.MusicPlayer;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentPlayerBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.tankery.lib.circularseekbar.CircularSeekBar;

@AndroidEntryPoint
public class PlayerFragment extends Fragment {

    FragmentPlayerBinding playerBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    @Inject
    MusicPlayer musicPlayer;
    Music music;
    ArrayList<String> likedMusics;
    boolean musicLiked = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        playerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.homeImageView.setVisibility(View.GONE);
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.playBackLayout.setVisibility(View.GONE);
        playerBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        Bundle args = getArguments();
        if (args != null) {
            music = args.getParcelable("Music");
        } else {
            onDestroy();
            Toast.makeText(mainActivity, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        if (music != null && musicPlayer.playingMusic.getValue() != null) {
            if (!music.getName().equals(musicPlayer.playingMusic.getValue().getName())) {
                playerBinding.playerDurationTextView.setText("0:00");
                playerBinding.playerSeekBar.setProgress(0);
                setupMusicDetail();
                playMusic();
            } else {
                setupMusicDetail();
                setDuration();
                seekBarProgress();
                updateSeekBar();
            }
        } else {
            playerBinding.playerDurationTextView.setText("0:00");
            playerBinding.playerSeekBar.setProgress(0);
            setupMusicDetail();
            playMusic();
        }
        setupButtons();
        likeMusic();
        downloadMusic();

        return playerBinding.getRoot();
    }

    private void downloadMusic() {
        musicPlayer.playingMusic.observe(requireActivity(), new Observer<Music>() {
            @Override
            public void onChanged(Music playingMusic) {
                playerBinding.playerDownloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        String title = playingMusic.getName();
                        Uri uri = Uri.parse(playingMusic.getSource());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(true)
                                .setTitle(title)
                                .setDescription("Downloading...")
                                .setMimeType("audio/*")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, title + ".m4a");
                        downloadManager.enqueue(request);

                    }
                });
            }
        });
    }

    private void likeMusic() {
        musicPlayer.playingMusic.observe(requireActivity(), new Observer<Music>() {
            @Override
            public void onChanged(Music playingMusic) {
                checkMusicLiked(playingMusic);
                playerBinding.playerLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!musicLiked) {
                            if (!likedMusics.contains(playingMusic.getName() + playingMusic.getArtist())) {
                                likedMusics.add(playingMusic.getName() + playingMusic.getArtist());
                            }
                            storeData();
                            playerBinding.playerLikeButton.setImageResource(R.drawable.ic_heart_fill);
                            musicLiked = true;
                        } else {
                            likedMusics.remove(playingMusic.getName() + playingMusic.getArtist());
                            storeData();
                            playerBinding.playerLikeButton.setImageResource(R.drawable.ic_heart);
                            musicLiked = false;
                        }
                    }
                });
            }
        });
    }

    private void checkMusicLiked(Music playingMusic) {
        readData();
        if (likedMusics.contains(playingMusic.getName() + playingMusic.getArtist())) {
            playerBinding.playerLikeButton.setImageResource(R.drawable.ic_heart_fill);
            musicLiked = true;
        } else {
            playerBinding.playerLikeButton.setImageResource(R.drawable.ic_heart);
            musicLiked = false;
        }
    }

    private void readData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("liked_musics", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        likedMusics = gson.fromJson(json, type);
    }

    private void storeData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(likedMusics);
        editor.putString("liked_musics", json);
        editor.apply();
    }

    private void setupButtons() {
        // Play Button
        musicPlayer.isPlaying.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                playerBinding.playButton.setImageResource(aBoolean ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        });
        playerBinding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.togglePlayBack();
            }
        });

        // Next Button
        playerBinding.nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.next();
            }
        });

        // Previous Button
        playerBinding.previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.previous();
            }
        });

        // Shuffle Button
        musicPlayer.isShuffleModeEnabled.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                int primaryColor = requireContext().getResources().getColor(R.color.primaryColor);
                int white = requireContext().getResources().getColor(R.color.white);
                playerBinding.shuffleImageButton.setColorFilter(aBoolean ? primaryColor : white);
            }
        });
        playerBinding.shuffleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.toggleShuffleMode();
            }
        });

        // Repeat Button
        musicPlayer.isRepeatModeEnabled.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                int primaryColor = requireContext().getResources().getColor(R.color.primaryColor);
                int white = requireContext().getResources().getColor(R.color.white);
                playerBinding.repeatModeImageButton.setColorFilter(aBoolean ? primaryColor : white);
            }
        });
        playerBinding.repeatModeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.toggleRepeatMode();
            }
        });
    }

    private void setupMusicDetail() {
        musicPlayer.playingMusic.observe(requireActivity(), new Observer<Music>() {
            @Override
            public void onChanged(Music playingMusic) {
                Glide.with(playerBinding.getRoot().getContext())
                        .load(playingMusic.getCover())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(playerBinding.backgroundImageView);
                Glide.with(playerBinding.getRoot().getContext())
                        .load(playingMusic.getCover())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(playerBinding.playerImageView);
                playerBinding.playerNameTextView.setText(playingMusic.getName());
                playerBinding.playerArtistTextView.setText(playingMusic.getArtist());
                if (playingMusic.getLyric() != null && !playingMusic.getLyric().equals("")) {
                    playerBinding.playerLyricButton.setVisibility(View.VISIBLE);
                    setupLyric(playingMusic.getLyric());
                } else {
                    playerBinding.playerLyricButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupLyric(String lyric) {
        playerBinding.playerLyricButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LyricBottomSheet lyricBottomSheet = new LyricBottomSheet();
                Bundle bundle = new Bundle();
                bundle.putString("Lyric", lyric);
                lyricBottomSheet.setArguments(bundle);
                lyricBottomSheet.show(requireActivity().getSupportFragmentManager(), lyricBottomSheet.getTag());
            }
        });
    }

    private void playMusic() {
        play();
        setDuration();
        seekBarProgress();
        updateSeekBar();
    }

    private void play() {
        musicPlayer.addMusicToPlaylist(music);

        // add related musics and play
        Disposable disposable = mainViewModel.getArtistMusicsFromDb(music.getArtist())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Music>>() {
                    @Override
                    public void accept(List<Music> musics) throws Throwable {
                        for (int i = 0; i < musics.size(); i++) {
                            if (musics.get(i).getId() == music.getId()) {
                                musics.remove(musics.get(i));
                            }
                        }
                        musicPlayer.addPlaylist(musics);
                        if (!musicPlayer.isPlaying()) {
                            musicPlayer.play();
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setDuration() {
        musicPlayer.duration.observe(requireActivity(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                long duration = (aLong / 1000);
                playerBinding.playerSeekBar.setMax(duration);
                playerBinding.playerDurationTextView.setText(timeToString(aLong));
            }
        });
    }

    private void seekBarProgress() {
        musicPlayer.currentPosition.observe(requireActivity(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                long currentPos = (aLong / 1000);
                playerBinding.playerSeekBar.setProgress(currentPos);
                playerBinding.playerCurrentDurationTextView.setText(timeToString(aLong));
            }
        });
    }

    private String timeToString(long pos) {
        long min = (pos / 1000) / 60;
        long sec = (pos / 1000) % 60;
        if (sec < 10) {
            return min + ":0" + sec;
        }
        return min + ":" + sec;
    }

    private void updateSeekBar() {
        playerBinding.playerSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                if (fromUser) {
                    musicPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.playBackLayout.setVisibility(View.VISIBLE);
        compositeDisposable.clear();
    }
}