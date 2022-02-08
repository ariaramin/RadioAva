package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Album;

import java.util.List;

@Entity(tableName = "album_tbl")
public class AllAlbumEntity {

    @PrimaryKey
    public long id;

    public List<Album> albums;

    public AllAlbumEntity(List<Album> albums) {
        this.albums = albums;
    }

    public long getId() {
        return id;
    }

    public List<Album> getAlbums() {
        return albums;
    }
}
