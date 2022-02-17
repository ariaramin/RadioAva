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
        dest.writeString(this.plays);
        dest.writeString(this.followers);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.backgroundImage = source.readString();
        this.image = source.readString();
        this.name = source.readString();
        this.plays = source.readString();
        this.followers = source.readString();
    }

    public Artist() {
    }

    protected Artist(Parcel in) {
        this.id = in.readInt();
        this.backgroundImage = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.plays = in.readString();
        this.followers = in.readString();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
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
