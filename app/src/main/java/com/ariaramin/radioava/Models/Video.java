package com.ariaramin.radioava.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "video_tbl")
public class Video {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("name")
    public String name;

    @SerializedName("artist")
    public String artist;

    @SerializedName("source")
    public String source;

    @SerializedName("views")
    public int views;

    @SerializedName("likes")
    public int likes;

    @SerializedName("dislikes")
    public int dislikes;

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

    public String getSource() {
        return source;
    }

    public int getViews() {
        return views;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }
}
