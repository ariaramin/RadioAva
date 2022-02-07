package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Artist;

@Entity(tableName = "artist_tbl")
public class AllArtistEntity {

    @PrimaryKey
    long id;

    Artist artist;

    public AllArtistEntity(Artist artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public Artist getArtist() {
        return artist;
    }
}
