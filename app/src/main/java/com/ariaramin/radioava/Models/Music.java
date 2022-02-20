package com.ariaramin.radioava.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "music_tbl")
public class Music {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("name")
    public String name;

    @SerializedName("artist")
    public String artist;

    @SerializedName("album")
    public String album;

    @SerializedName("source")
    public String source;

    @SerializedName("lyric")
    public String lyric;

    @SerializedName("release_year")
    public String releaseDate;

    @SerializedName("type")
    public String type;

    public int getId() {
        return id;
    }

    public String getCover() {
        return cover;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getSource() {
        return source;
    }

    public String getLyric() {
        return lyric;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }
}
