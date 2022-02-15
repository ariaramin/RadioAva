package com.ariaramin.radioava.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "album_tbl")
public class Album {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("name")
    public String name;

    @SerializedName("artist")
    public String artist;

    @SerializedName("musics")
    public List<Music> musics;

    @SerializedName("release_year")
    public String releaseDate;

    @SerializedName("total_music")
    public int totalMusic;

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
