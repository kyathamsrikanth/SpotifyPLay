package com.example.spotifyplay;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    // Fields
    private String name;
    private int id;
    private int user_id;

    private List<Song> songs;

    // Constructor
    public Playlist(int id,String name,int user_id) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    // Methods for adding and removing songs from the playlist
    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }
}
