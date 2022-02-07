package com.ariaramin.radioava.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album {

    @SerializedName("cover")
    String cover;

    @SerializedName("name")
    String name;

    @SerializedName("artist")
    String artist;

    @SerializedName("musics")
    List<Music> musics;

    @SerializedName("release_year")
    String releaseDate;

    @SerializedName("total_music")
    int totalMusic;

    public String getCover() {
        return cover;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getTotalMusic() {
        return totalMusic;
    }
}
