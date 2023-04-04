package com.example.spotifyplay;

public class Song {
    private String id;
    private String title;
    private String artist;
    private String album;
    private int releaseYear;

    public Song(String id, String title, String artist, String album, int releaseYear) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.releaseYear = releaseYear;
    }
    public Song(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public int getReleaseYear() {
        return releaseYear;
    }
}

