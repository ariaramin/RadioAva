package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Album;

@Entity(tableName = "album_tbl")
public class AllAlbumEntity {

    @PrimaryKey
    long id;

    Album album;

    public AllAlbumEntity(Album album) {
        this.album = album;
    }

    public long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }
}
