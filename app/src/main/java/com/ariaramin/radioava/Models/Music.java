package com.ariaramin.radioava.Models;

import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("cover")
    String cover;

    @SerializedName("name")
    String name;

    @SerializedName("artist")
    String artist;

    @SerializedName("album")
    String album;

    @SerializedName("source")
    String source;

    @SerializedName("release_year")
    String releaseDate;

    @SerializedName("type")
    String type;

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }
}
