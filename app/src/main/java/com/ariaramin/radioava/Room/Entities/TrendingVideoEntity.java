package com.ariaramin.radioava.Room.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ariaramin.radioava.Models.Video;

import java.util.List;

@Entity(tableName = "trending_video_tbl")
public class TrendingVideoEntity {

    @PrimaryKey
    public long id;

    public List<Video> videos;

    public TrendingVideoEntity(List<Video> videos) {
        this.videos = videos;
    }

    public long getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
