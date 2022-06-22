package com.ariaramin.radioava.Models;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

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

    @SerializedName("views_count")
    public String viewsCount;

    @SerializedName("likes_count")
    public String likesCount;

    @SerializedName("dislikes_count")
    public String dislikesCount;

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

    public String getViewsCount() {
        return viewsCount;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public String getDislikesCount() {
        return dislikesCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.name);
        dest.writeString(this.artist);
        dest.writeString(this.source);
        dest.writeInt(this.views);
        dest.writeInt(this.likes);
        dest.writeInt(this.dislikes);
        dest.writeString(this.releaseDate);
        dest.writeString(this.type);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.cover = source.readString();
        this.name = source.readString();
        this.artist = source.readString();
        this.source = source.readString();
        this.views = source.readInt();
        this.likes = source.readInt();
        this.dislikes = source.readInt();
        this.releaseDate = source.readString();
        this.type = source.readString();
    }

    public Video() {
    }

    protected Video(Parcel in) {
        this.id = in.readInt();
        this.cover = in.readString();
        this.name = in.readString();
        this.artist = in.readString();
        this.source = in.readString();
        this.views = in.readInt();
        this.likes = in.readInt();
        this.dislikes = in.readInt();
        this.releaseDate = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
