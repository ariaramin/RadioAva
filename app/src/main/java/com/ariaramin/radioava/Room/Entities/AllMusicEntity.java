package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Music;

@Entity(tableName = "music_tbl")
public class AllMusicEntity {

    @PrimaryKey
    long id;

    Music music;

    public AllMusicEntity(Music music) {
        this.music = music;
    }

    public long getId() {
        return id;
    }

    public Music getMusic() {
        return music;
    }
}
