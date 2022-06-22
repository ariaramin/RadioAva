package com.ariaramin.radioava.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Music implements Parcelable {

    @SerializedName("id")
    public int id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("name")
    public String name;

    @SerializedName("artist")
    public String artist;

    @SerializedName("album")
    public String album;

    @SerializedName("source")
    public String source;

    @SerializedName("lyric")
    public String lyric;

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

    public String getAlbum() {
        return album;
    }

    public String getSource() {
        return source;
    }

    public String getLyric() {
        return lyric;
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
        dest.writeString(this.album);
        dest.writeString(this.source);
        dest.writeString(this.lyric);
        dest.writeString(this.releaseDate);
        dest.writeString(this.type);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.cover = source.readString();
        this.name = source.readString();
        this.artist = source.readString();
        this.album = source.readString();
        this.source = source.readString();
        this.lyric = source.readString();
        this.releaseDate = source.readString();
        this.type = source.readString();
    }

    public Music() {
    }

    protected Music(Parcel in) {
        this.id = in.readInt();
        this.cover = in.readString();
        this.name = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.source = in.readString();
        this.lyric = in.readString();
        this.releaseDate = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
}
