package com.ariaramin.radioava.ui.Fragments.Music;

import android.app.DownloadManager;
import android.content.Context;
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
import android.widget.Toast;

import com.ariaramin.radioava.Adapters.Music.VerticalMusicAdapter;
import com.ariaramin.radioava.MainActivity;
import com.ariaramin.radioava.MainViewModel;
import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Music;
import com.ariaramin.radioava.MusicPlayer;
import com.ariaramin.radioava.R;
import com.ariaramin.radioava.databinding.FragmentAlbumBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class AlbumFragment extends Fragment {

    FragmentAlbumBinding albumBinding;
    MainViewModel mainViewModel;
    CompositeDisposable compositeDisposable;
    MainActivity mainActivity;
    Album album;
    @Inject
    MusicPlayer musicPlayer;
    private static final String TAG = "album";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        albumBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        compositeDisposable = new CompositeDisposable();
        Bundle args = getArguments();
        if (args != null) {
            album = args.getParcelable("Album");
        } else {
            onDestroy();
            Toast.makeText(mainActivity, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        mainActivity.bottomNavigationView.setVisibility(View.GONE);
        mainActivity.homeImageView.setVisibility(View.GONE);
        albumBinding.backStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        setupDetail();
        setupButtons();
        return albumBinding.getRoot();
    }

    private void setupDetail() {
        Glide.with(albumBinding.getRoot().getContext())
                .load(album.getCover())
                .thumbnail(
                        Glide.with(albumBinding.getRoot().getContext())
                                .load(R.drawable.loading)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(albumBinding.albumImageView);
        albumBinding.albumNameTextView.setText(album.getName());
        albumBinding.albumArtistTextView.setText(album.getArtist());
        albumBinding.albumMusicsCountTextView.setText(album.getTotalMusic() + " Musics");
        VerticalMusicAdapter adapter = new VerticalMusicAdapter(album.getMusics(), TAG);
        albumBinding.albumMusicsRecyclerView.setAdapter(adapter);
    }

    private void setupButtons() {
        albumBinding.albumPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.setNewPlaylist(album.getMusics());
                musicPlayer.play();
            }
        });

        albumBinding.albumDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                for (int i = 0; i < album.getMusics().size(); i++) {
                    Music music = album.getMusics().get(i);
                    String title = music.getName();
                    Uri uri = Uri.parse(music.getSource());
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
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        mainActivity.homeImageView.setVisibility(View.VISIBLE);
    }
}