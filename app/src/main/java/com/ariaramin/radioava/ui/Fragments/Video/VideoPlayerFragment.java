package com.ariaramin.radioava.ui.Fragments.Video;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ariaramin.radioava.Adapters.Video.VerticalVideoAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.Models.Video;
import com.ariaramin.radioava.Players.MusicPlayer;
import com.ariaramin.radioava.Players.VideoPlayer;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentVideoPlayerBinding;
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
    Video video;
    boolean videoLiked;
    ArrayList<String> likedVideos;
    ArrayList<String> recentlyPlayed;
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
        videoPlayerBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        Bundle args = getArguments();
        if (args != null) {
            video = args.getParcelable("Video");
        } else {
            onDestroy();
            Toast.makeText(mainActivity, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

        setupDetail();
        playVideo();
        getMoreVideo();
        downloadVideo();
        likeVideo();
        addToRecentlyPlayed();
        return videoPlayerBinding.getRoot();
    }

    private void downloadVideo() {
        videoPlayer.playingVideo.observe(requireActivity(), new Observer<Video>() {
            @Override
            public void onChanged(Video playingVideo) {
                videoPlayerBinding.videoPlayerDownloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                    }
                });
            }
        });
    }

    private void likeVideo() {
        videoPlayer.playingVideo.observe(requireActivity(), new Observer<Video>() {
            @Override
            public void onChanged(Video playingVideo) {
                checkVideoLiked(playingVideo);
                videoPlayerBinding.videoPlayerLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!videoLiked) {
                            if (!likedVideos.contains(playingVideo.getName() + playingVideo.getArtist())) {
                                likedVideos.add(playingVideo.getName() + playingVideo.getArtist());
                            }
                            storeData();
                            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart_fill);
                            videoLiked = true;
                        } else {
                            likedVideos.remove(playingVideo.getName() + playingVideo.getArtist());
                            storeData();
                            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart);
                            videoLiked = false;
                        }
                    }
                });
            }
        });
    }

    private void checkVideoLiked(Video playingVideo) {
        readData();
        if (likedVideos.contains(playingVideo.getName() + playingVideo.getArtist())) {
            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart_fill);
            videoLiked = true;
        } else {
            videoPlayerBinding.videoPlayerLikeButton.setImageResource(R.drawable.ic_heart);
            videoLiked = false;
        }
    }

    public void addToRecentlyPlayed() {
        readRecentlyPlayedData();
        videoPlayer.playingVideo.observe(requireActivity(), new Observer<Video>() {
            @Override
            public void onChanged(Video playingVideo) {
                if (recentlyPlayed.size() >= 1) {
                    if (!recentlyPlayed.get(recentlyPlayed.size() - 1).equals(playingVideo.getId() + playingVideo.getName())) {
                        recentlyPlayed.add(playingVideo.getId() + playingVideo.getName());
                        storeRecentlyPlayedData();
                    }
                } else {
                    recentlyPlayed.add(playingVideo.getId() + playingVideo.getName());
                    storeRecentlyPlayedData();
                }
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
        videoPlayer.playingVideo.observe(requireActivity(), new Observer<Video>() {
            @Override
            public void onChanged(Video playingVideo) {
                videoPlayerBinding.videoPlayerNameTextView.setText(playingVideo.getName());
                videoPlayerBinding.videoPlayerArtistTextView.setText(playingVideo.getArtist());
            }
        });
    }

    private void playVideo() {
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
        videoPlayerBinding.videoPlayerView.setPlayer(videoPlayer.getPlayer());
        videoPlayerBinding.videoPlayerView.setShowNextButton(false);
        videoPlayerBinding.videoPlayerView.setShowPreviousButton(false);
        Disposable disposable = mainViewModel.getArtistVideosFromDb(video.getArtist())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
                        for (int i = 0; i < videos.size(); i++) {
                            if (videos.get(i).getId() == video.getId()) {
                                videos.remove(videos.get(i));
                            }
                        }
                        setupRelatedVideos(videos);
                        if (!videoPlayer.isPlaying()) {
                            videoPlayer.play(video);
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void getMoreVideo() {
        Disposable disposable = mainViewModel.getTrendingVideosFromDb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Video>>() {
                    @Override
                    public void accept(List<Video> videos) throws Throwable {
//                        for (int i = 0; i < videos.size(); i++) {
//                            if (videos.get(i).getId() == video.getId()) {
//                                videos.remove(videos.get(i));
//                            }
//                        }
                        VerticalVideoAdapter adapter = (VerticalVideoAdapter) videoPlayerBinding.videoPlayerRecyclerView.getAdapter();
                        if (adapter != null) {
                            adapter.addVideos(videos.subList(0, 10));
                        }
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