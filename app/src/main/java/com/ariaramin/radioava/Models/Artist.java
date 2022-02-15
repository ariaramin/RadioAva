package com.ariaramin.radioava.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "artist_tbl")
public class Artist {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("background_image")
    public String backgroundImage;

    @SerializedName("image")
    public String image;

    @SerializedName("name")
    public String name;

    @SerializedName("plays_count")
    public String plays;

    @SerializedName("followers")
    public String followers;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPlays() {
        return plays;
    }

    public String getFollowers() {
        return followers;
    }
}
