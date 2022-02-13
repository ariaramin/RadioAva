package com.ariaramin.radioava.Models;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    int id;

    @SerializedName("cover")
    String cover;

    @SerializedName("name")
    String name;

    @SerializedName("artist")
    String artist;

    @SerializedName("source")
    String source;

    @SerializedName("views_count")
    String views;

    @SerializedName("likes_count")
    String likes;

    @SerializedName("dislikes_count")
    String dislikes;

    @SerializedName("release_year")
    String releaseDate;

    @SerializedName("type")
    String type;

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

    public String getSource() {
        return source;
    }

    public String getViews() {
        return views;
    }

    public String getLikes() {
        return likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }
}
