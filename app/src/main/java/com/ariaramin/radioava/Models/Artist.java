package com.ariaramin.radioava.Models;

import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("background_image")
    String backgroundImage;

    @SerializedName("image")
    String image;

    @SerializedName("name")
    String name;

    @SerializedName("plays_count")
    String plays;

    @SerializedName("followers")
    String followers;

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
