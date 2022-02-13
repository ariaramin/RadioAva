package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Video;

import java.util.List;

@Entity(tableName = "video_tbl")
public class AllVideoEntity {

    @PrimaryKey
    public long id;

    public List<Video> videos;

    public AllVideoEntity(List<Video> videos) {
        this.videos = videos;
    }

    public long getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
