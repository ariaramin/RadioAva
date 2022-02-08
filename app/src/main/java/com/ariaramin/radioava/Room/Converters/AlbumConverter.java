package com.ariaramin.radioava.Room.Converters;

import androidx.room.TypeConverter;

import com.ariaramin.radioava.Models.Album;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AlbumConverter {

    @TypeConverter
    public String toJson(List<Album> albums) {
        if (albums == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Album>>() {
        }.getType();
        return gson.toJson(albums, type);
    }

    @TypeConverter
    public List<Album> toObject(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Album>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
