package com.ariaramin.radioava.ui.Fragments.Video;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Players.MusicPlayer;
import com.ariaramin.radioava.Players.VideoPlayer;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.SharedPreference.SharedPreferenceManager;
import com.ariaramin.radioava.databinding.FragmentVideoPlayerBinding;
import com.google.android.exoplayer2.ui.PlayerView;
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
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class VideoPlayerFragment extends Fragment implements OnClickVideoListener {

    FragmentVideoPlayerBinding videoPlayerBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    @Inject
    MusicPlayer musicPlayer;
    @Inject
    VideoPlayer videoPlayer;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    boolean fullscreen = false;
    Dialog fullScreenDialog;
    Video video;
    boolean videoLiked;
    ArrayList<String> likedVideos;
    ArrayList<String> recentlyPlayed;
    ArrayList<String> downloads;
    boolean downloaded = false;
    private static final String TAG = "video_player";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        videoPlayerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_player, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        mainActivity.playBackLayout.setVisibility(View.GONE);
        videoPlayerBinding.backStackButton.setOnClickListener(v -> requireActivity().onBackPressed());


        Bundle args = getArguments();
        if (args != null) {
            video = args.getParcelable("Video");
        } else {
            onDestroy();
            Toast.makeText(mainActivity, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        setupDetail();
        playVideo();
        initFullscreenDialog();
        getMoreVideo();
        downloadVideo();
        likeVideo();
        addToRecentlyPlayed();
        return videoPlayerBinding.getRoot();
    }

    private void downloadVideo() {
        videoPlayer.playingVideo.observe(requireActivity(), playingVideo ->
                videoPlayerBinding.videoPlayerDownloadButton.setOnClickListener(v -> {
                    DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    String title = playingVideo.getName();
                    Uri uri = Uri.parse(playingVideo.getSource());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setAllowedOverRoaming(true)
                            .setTitle(title)
                            .setDescription("Downloading...")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, title + ".mp4");
                    downloadManager.enqueue(request);
                    addToDownloads(playingVideo);
                })
        );
    }

    private void addToDownloads(Video playingVideo) {
        downloads = sharedPreferenceManager.readDownloadedData();
        if (!downloaded) {
            if (!downloads.contains(playingVideo.getId() + playingVideo.getName())) {
                downloads.add(playingVideo.getId() + playingVideo.getName());
            }
            sharedPreferenceManager.storeDownloadedData(downloads);
            downloaded = true;
        } else {
            downloads.remove(playingVideo.getId() + playingVideo.getName());
            sharedPreferenceManager.storeDownloadedData(downloads);
            downloaded = false;
        }
    }

    private void likeVideo() {
        videoPlayer.playingVideo.observe(requireActivity(), playingVideo -> {
            checkVideoLiked(playingVideo);
            videoPlayerBinding.videoPlayerLikeButton.setOnClickListener(v -> {
                if (!videoLiked) {
                    if (!likedVideos.contains(playingVideo.getId() + playingVideo.getName())) {
                        likedVideos.add(playingVideo.getId() + playingVideo.getName());
                    }
                    storeData();
                    videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart_fill);
                    videoLiked = true;
                } else {
                    likedVideos.remove(playingVideo.getId() + playingVideo.getName());
                    storeData();
                    videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart);
                    videoLiked = false;
                }
            });
        });
    }

    private void checkVideoLiked(Video playingVideo) {
        readData();
        if (likedVideos.contains(playingVideo.getId() + playingVideo.getName())) {
            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart_fill);
            videoLiked = true;
        } else {
            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart);
            videoLiked = false;
        }
    }

    public void addToRecentlyPlayed() {
        readRecentlyPlayedData();
        videoPlayer.playingVideo.observe(requireActivity(), playingVideo -> {
            if (recentlyPlayed.size() >= 1) {
                if (!recentlyPlayed.get(recentlyPlayed.size() - 1).equals(playingVideo.getId() + playingVideo.getName())) {
                    recentlyPlayed.add(playingVideo.getId() + playingVideo.getName());
                    storeRecentlyPlayedData();
                }
            } else {
                recentlyPlayed.add(playingVideo.getId() + playingVideo.getName());
                storeRecentlyPlayedData();
            }
        });
    }

    private void readData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("liked_musics", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        likedVideos = gson.fromJson(json, type);
    }

    private void storeData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(likedVideos);
        editor.putString("liked_musics", json);
        editor.apply();
    }

    private void readRecentlyPlayedData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("recently_played", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("recently_played", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        recentlyPlayed = gson.fromJson(json, type);
    }

    private void storeRecentlyPlayedData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("recently_played", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentlyPlayed);
        editor.putString("recently_played", json);
        editor.apply();
    }

    private void setupDetail() {
        videoPlayer.playingVideo.observe(requireActivity(), playingVideo -> {
            videoPlayerBinding.videoPlayerNameTextView.setText(playingVideo.getName());
            videoPlayerBinding.videoPlayerArtistTextView.setText(playingVideo.getArtist());
        });
    }

    private void playVideo() {
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
        videoPlayerBinding.videoPlayerView.setPlayer(videoPlayer.getPlayer());
        videoPlayerBinding.videoPlayerView.setShowNextButton(false);
        videoPlayerBinding.videoPlayerView.setShowPreviousButton(false);
        Disposable disposable = mainViewModel.getArtistVideos(video.getArtist())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    for (int i = 0; i < videos.size(); i++) {
                        if (videos.get(i).getId() == video.getId()) {
                            videos.remove(videos.get(i));
                        }
                    }
                    setupRelatedVideos(videos);
                    if (!videoPlayer.isPlaying()) {
                        videoPlayer.play(video);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getMoreVideo() {
        Disposable disposable = mainViewModel.getTrendingVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videos -> {
                    for (int i = 0; i < videos.size(); i++) {
                        if (videos.get(i).getId() == video.getId()) {
                            videos.remove(videos.get(i));
                        }
                    }
                    VerticalVideoAdapter adapter = (VerticalVideoAdapter) videoPlayerBinding.videoPlayerRecyclerView.getAdapter();
                    if (adapter != null) {
                        adapter.addVideos(videos.subList(0, 10));
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void setupRelatedVideos(List<Video> videos) {
        if (videoPlayerBinding.videoPlayerRecyclerView.getAdapter() == null) {
            VerticalVideoAdapter adapter = new VerticalVideoAdapter(videos, TAG);
            adapter.addListener(this);
            videoPlayerBinding.videoPlayerRecyclerView.setAdapter(adapter);
        } else {
            VerticalVideoAdapter adapter = (VerticalVideoAdapter) videoPlayerBinding.videoPlayerRecyclerView.getAdapter();
            adapter.updateList(videos);
        }
    }

    private void initFullscreenDialog() {
        fullScreenDialog = new Dialog(requireActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (fullscreen) {
                    closeFullscreenDialog();
                }
                super.onBackPressed();
            }
        };

        PlayerView playerView = videoPlayerBinding.videoPlayerView;
        View fullScreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullScreenButton.setOnClickListener(v -> {
            if (!fullscreen) {
                openFullscreenDialog();
            } else {
                closeFullscreenDialog();
            }
        });
    }

    private void openFullscreenDialog() {
        // Set layout fullscreen
        PlayerView playerView = videoPlayerBinding.videoPlayerView;
        ViewGroup parent = (ViewGroup) playerView.getParent();
        parent.removeView(playerView);
        fullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Set screen orientation landscape
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        if (mainActivity.getSupportActionBar() != null) {
            mainActivity.getSupportActionBar().hide();
        }
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Change fullscreen button image
        ImageView fullScreenImageView = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullScreenImageView.setImageResource(R.drawable.ic_fullscreen_close);
        fullscreen = true;
        fullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        // Set layout fullscreen
        PlayerView playerView = videoPlayerBinding.videoPlayerView;
        ViewGroup parent = (ViewGroup) playerView.getParent();
        parent.removeView(playerView);
        videoPlayerBinding.videoPlayerFrame.addView(playerView);

        // Set screen orientation portrait
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        if (mainActivity.getSupportActionBar() != null) {
            mainActivity.getSupportActionBar().show();
        }
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Change fullscreen button image
        ImageView fullScreenImageView = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullScreenImageView.setImageResource(R.drawable.ic_fullscreen_open);
        fullscreen = false;
        fullScreenDialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayer.pause();
        compositeDisposable.clear();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
        if (musicPlayer.playingMusic.getValue() != null) {
            mainActivity.playBackLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnClick(Video video) {
        videoPlayer.play(video);
        videoPlayer.playingVideo.setValue(video);
    }
}