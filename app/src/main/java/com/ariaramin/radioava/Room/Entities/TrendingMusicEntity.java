package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Music;

import java.util.List;

@Entity(tableName = "trending_music_tbl")
public class TrendingMusicEntity {

    @PrimaryKey
    public long id;

    public List<Music> musics;

    public TrendingMusicEntity(List<Music> musics) {
        this.musics = musics;
    }

    public long getId() {
        return id;
    }

    public List<Music> getMusics() {
        return musics;
    }
}
