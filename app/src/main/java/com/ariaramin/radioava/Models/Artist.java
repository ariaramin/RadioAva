package com.ariaramin.radioava.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "artist_tbl")
public class Artist implements Parcelable {

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
    public String playsCount;

    @SerializedName("plays")
    public int plays;

    @SerializedName("followers")
    public String followers;

    @SerializedName("followers_count")
    public int followersCount;

    public int getId() {
        return id;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPlaysCount() {
        return playsCount;
    }

    public int getPlays() {
        return plays;
    }

    public String getFollowers() {
        return followers;
    }

    public int getFollowersCount() {
        return followersCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.backgroundImage);
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.playsCount);
        dest.writeInt(this.plays);
        dest.writeString(this.followers);
        dest.writeInt(this.followersCount);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.backgroundImage = source.readString();
        this.image = source.readString();
        this.name = source.readString();
        this.playsCount = source.readString();
        this.plays = source.readInt();
        this.followers = source.readString();
        this.followersCount = source.readInt();
    }

    public Artist() {
    }

    protected Artist(Parcel in) {
        this.id = in.readInt();
        this.backgroundImage = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.playsCount = in.readString();
        this.plays = in.readInt();
        this.followers = in.readString();
        this.followersCount = in.readInt();
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
