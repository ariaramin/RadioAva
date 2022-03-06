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

    public void storeDownloadedData(ArrayList<String> downloaded) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloaded", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(downloaded);
        editor.putString("downloaded", json);
        editor.apply();
    }

    public ArrayList<String> readDownloadedData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("downloaded", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("downloaded", String.valueOf(new ArrayList<String>()));
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public void storeLikedData(ArrayList<String> liked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("liked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(liked);
        editor.putString("liked", json);
        editor.apply();
    }

    public ArrayList<String> readLikedData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("liked", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("liked", String.valueOf(new ArrayList<String>()));
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
