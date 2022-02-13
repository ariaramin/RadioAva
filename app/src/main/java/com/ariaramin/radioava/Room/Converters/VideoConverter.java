package com.ariaramin.radioava.Room.Converters;

import androidx.room.TypeConverter;

import com.ariaramin.radioava.Models.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class VideoConverter {

    @TypeConverter
    public String toJson(List<Video> videos) {
        if (videos == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video>>() {
        }.getType();
        return gson.toJson(videos, type);
    }

    @TypeConverter
    public List<Video> toObject(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Video>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
