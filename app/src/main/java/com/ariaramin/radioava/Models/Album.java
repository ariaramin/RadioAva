package com.ariaramin.radioava.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "album_tbl")
public class Album implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("cover")
    public String cover;

    @SerializedName("name")
    public String name;

    @SerializedName("artist")
    public String artist;

    @SerializedName("musics")
    public List<Music> musics;

    @SerializedName("release_year")
    public String releaseDate;

    @SerializedName("total_music")
    public int totalMusic;

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

    public List<Music> getMusics() {
        return musics;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getTotalMusic() {
        return totalMusic;
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
        dest.writeList(this.musics);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.totalMusic);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.cover = source.readString();
        this.name = source.readString();
        this.artist = source.readString();
        this.musics = new ArrayList<Music>();
        source.readList(this.musics, Music.class.getClassLoader());
        this.releaseDate = source.readString();
        this.totalMusic = source.readInt();
    }

    public Album() {
    }

    protected Album(Parcel in) {
        this.id = in.readInt();
        this.cover = in.readString();
        this.name = in.readString();
        this.artist = in.readString();
        this.musics = new ArrayList<Music>();
        in.readList(this.musics, Music.class.getClassLoader());
        this.releaseDate = in.readString();
        this.totalMusic = in.readInt();
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
