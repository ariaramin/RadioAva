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

    @SerializedName("views_count")
    public String views;

    @SerializedName("likes_count")
    public String likes;

    @SerializedName("dislikes_count")
    public String dislikes;

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
