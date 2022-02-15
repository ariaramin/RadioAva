package com.ariaramin.radioava.Room.Converters;

import androidx.room.TypeConverter;

import com.ariaramin.radioava.Models.Album;
import com.ariaramin.radioava.Models.Music;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AlbumConverter {

    @TypeConverter
    public String toJson(List<Music> musics) {
        if (musics == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Music>>() {
        }.getType();
        return gson.toJson(musics, type);
    }

    @TypeConverter
    public List<Music> toObject(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Music>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
