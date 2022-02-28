package com.ariaramin.radioava.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceManager {

    Context context;

    public SharedPreferenceManager(Context context) {
        this.context = context;
    }

    public ArrayList<String> readFollowedArtistData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("followed_artist", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("followed_artist", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public void storeFollowedArtistData(ArrayList<String> followedArtists) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("followed_artist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(followedArtists);
        editor.putString("followed_artist", json);
        editor.apply();
    }

    public void storeData(ArrayList<String> likedMusics) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(likedMusics);
        editor.putString("liked_musics", json);
        editor.apply();
    }

    public ArrayList<String> readLikedMusicsData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("liked_musics", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("liked_musics", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public void storeRecentlyPlayedData(ArrayList<String> recentlyPlayed) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("recently_played", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentlyPlayed);
        editor.putString("recently_played", json);
        editor.apply();
    }

    public ArrayList<String> readRecentlyPlayedData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("recently_played", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recently_played", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
