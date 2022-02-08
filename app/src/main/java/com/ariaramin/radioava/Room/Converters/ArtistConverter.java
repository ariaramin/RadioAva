package com.ariaramin.radioava.Room.Converters;

import androidx.room.TypeConverter;

import com.ariaramin.radioava.Models.Artist;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ArtistConverter {

    @TypeConverter
    public String toJson(List<Artist> artists) {
        if (artists == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Artist>>() {
        }.getType();
        return gson.toJson(artists, type);
    }

    @TypeConverter
    public List<Artist> toObject(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Artist>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
