//package com.example.spotifyplay;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BackendLogic {
//    // List of users
//    private final List<User> users = new ArrayList<>();
//
//    // List of songs
//    private final List<Song> songs = new ArrayList<>();
//
//    // List of playlists
//    private final List<Playlist> playlists = new ArrayList<>();
//
//    // Constructor
//    public BackendLogic() {
//        // Add some sample data
//        users.add(new User("john", "doe123"));
//        users.add(new User("jane", "doe456"));
//
//        songs.add(new Song("Song 1", "Artist 1"));
//        songs.add(new Song("Song 2", "Artist 2"));
//        songs.add(new Song("Song 3", "Artist 3"));
//
//        Playlist playlist1 = new Playlist("Playlist 1");
//        playlist1.addSong(songs.get(0));
//        playlist1.addSong(songs.get(1));
//        playlists.add(playlist1);
//
//        Playlist playlist2 = new Playlist("Playlist 2");
//        playlist2.addSong(songs.get(1));
//        playlist2.addSong(songs.get(2));
//        playlists.add(playlist2);
//    }
//
//    // Method to authenticate a user
//    public boolean getUserByUsernameAndPassword(String username, String password) {
//        for (User user : users) {
//            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Method to create a new user
//    public void createUser(String username, String password) {
//        users.add(new User(username, password));
//    }
//
//    // Method to get all songs
//    public List<Song> getAllSongs() {
//        return songs;
//    }
//
//    // Method to filter songs by artist
//    public List<Song> filterSongsByArtist(String artist) {
//        List<Song> filteredSongs = new ArrayList<>();
//        for (Song song : songs) {
//            if (song.getArtist().equals(artist)) {
//                filteredSongs.add(song);
//            }
//        }
//        return filteredSongs;
//    }
//
//    // Method to add a song to a playlist
//    public void addSongToPlaylist(Song song, Playlist playlist) {
//        playlist.addSong(song);
//    }
//
//    // Method to create a new playlist
//    public void createPlaylist(String name) {
//        playlists.add(new Playlist(name));
//    }
//
//    // Method to get all playlists
//    public List<Playlist> getAllPlaylists() {
//        return playlists;
//    }
//
//    // Method to delete a song from a playlist
//    public void deleteSongFromPlaylist(Song song, Playlist playlist) {
//        playlist.removeSong(song);
//    }
//
//    // Method to edit the name of a playlist
//    public void editPlaylistName(Playlist playlist, String newName) {
//        playlist.setName(newName);
//    }
//}
//
