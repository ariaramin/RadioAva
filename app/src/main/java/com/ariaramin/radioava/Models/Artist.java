package com.ariaramin.radioava.Models;

import com.google.gson.annotations.SerializedName;

public class Artist {

    @SerializedName("background_image")
    String backgroundImage;

    @SerializedName("image")
    String image;

    @SerializedName("name")
    String name;

    @SerializedName("plays")
    String plays;

    @SerializedName("followers_count")
    int followersCount;

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

    public int getFollowersCount() {
        return followersCount;
    }
}
