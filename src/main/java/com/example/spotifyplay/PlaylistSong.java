package com.example.spotifyplay;

public class PlaylistSong {
    private int playlistId;
    private int songId;

    public PlaylistSong(int playlistId, int songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public int getSongId() {
        return songId;
    }
}

