package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Artist;

import java.util.List;

@Entity(tableName = "artist_tbl")
public class AllArtistEntity {

    @PrimaryKey
    public long id;

    public List<Artist> artist;

    public AllArtistEntity(List<Artist> artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public List<Artist> getArtist() {
        return artist;
    }
}
