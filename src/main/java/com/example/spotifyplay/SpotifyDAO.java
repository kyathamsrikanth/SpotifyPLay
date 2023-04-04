package com.example.spotifyplay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpotifyDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/spotifyplay?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "srikanth";

    private static Connection conn;

    public SpotifyDAO() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("id");
            String foundUsername = rs.getString("username");
            String foundPassword = rs.getString("password");
            return new User(id, foundUsername, foundPassword);
        }
        return null;
    }

    public User createUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 1) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new User(id, username, password);
            }
        }
        return null;
    }

    public List<Song> getSongsByArtist(String artist) throws SQLException {
        String sql = "SELECT * FROM songs WHERE artist LIKE ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "%" + artist + "%");
        ResultSet rs = statement.executeQuery();
        List<Song> songs = new ArrayList<>();
        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String title = rs.getString("title");
            String foundArtist = rs.getString("artist");
            String album = rs.getString("album");
            int releaseYear = rs.getInt("release_year");
            songs.add(new Song(id, title, foundArtist, album, releaseYear));
        }
        return songs;
    }
    public List<Song> getAllSongs() throws SQLException {
        String sql = "SELECT * FROM songs";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Song> songs = new ArrayList<>();
        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String title = rs.getString("title");
            String foundArtist = rs.getString("artist");
            String album = rs.getString("album");
            int releaseYear = rs.getInt("release_year");
            songs.add(new Song(id, title, foundArtist, album, releaseYear));
        }
        return songs;
    }

    public List<String> getAllArtists() throws SQLException {
        String sql = "SELECT distinct artist FROM songs;";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<String> songs = new ArrayList<>();
        while (rs.next()) {
            String foundArtist = rs.getString("artist");
            songs.add(foundArtist);
        }
        return songs;
    }

    public List<String> getAllAlbums() throws SQLException {
        String sql = "SELECT distinct album FROM songs;";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<String> songs = new ArrayList<>();
        while (rs.next()) {
            String album = rs.getString("album");
            songs.add(album);
        }
        return songs;
    }

    public List<Playlist> getAllPlayLists() throws SQLException {
        String sql = "SELECT * FROM playlists";
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        List<Playlist> playlists = new ArrayList<>();
        while (rs.next()) {
            String name = String.valueOf(rs.getString("name"));
            int id = rs.getInt("id");
            int user_id = rs.getInt("user_id");
            playlists.add(new Playlist(id,name,user_id));
        }
        return playlists;
    }
    public List<Playlist> getPlaylistsByUser(int user) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE user_id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, user);
        ResultSet rs = statement.executeQuery();
        List<Playlist> playlists = new ArrayList<>();
        while (rs.next()) {
            String name = String.valueOf(rs.getString("name"));
            int id = rs.getInt("id");
            int user_id = rs.getInt("user_id");
            playlists.add(new Playlist(id, name,user_id));
        }
        return playlists;
    }

    public static List<String> getPlaylistsSongsByPlaylist(String name, int user) throws SQLException {
        int playlist_id  = getPlaylistID( name,  user);
        String sql = "Select a.title FROM songs a JOIN playlist_songs b ON a.id = b.song_id Where playlist_id = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, playlist_id);
        ResultSet rs = statement.executeQuery();
        List<String> playlists = new ArrayList<>();
        while (rs.next()) {
            String songname = String.valueOf(rs.getString("title"));
            playlists.add(songname);
        }
        return playlists;
    }

    public static Playlist createPlaylist(String name, int user) throws SQLException {
        String sql = "INSERT INTO playlists (name, user_id) VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setInt(2, user);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 1) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Playlist(id, name, user);
            }
        }
        return null;
    }

    public static Playlist removePlaylist(String name, int user) throws SQLException {
        int id  = getPlaylistID( name,  user);
        String sql = "DELETE FROM playlists WHERE id = ?";
        PreparedStatement statement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, id);
        int affectedRows = statement.executeUpdate();
        if (affectedRows == 1) {
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int id2 = rs.getInt(1);
                return new Playlist(id2, name, user);
            }
        }
        return null;
    }

    public static int getPlaylistID(String name, int user) throws SQLException {
        String sql = "SELECT id FROM playlists WHERE name = ? AND user_id =?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, user);
        ResultSet rs = statement.executeQuery();
        int id  = 0;
        while (rs.next()) {
             id = rs.getInt("id");

        }
        return id;
    }

    public static int getSongID(String name) throws SQLException {
        String sql = "SELECT id FROM songs WHERE title = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();
        int id  = 0;
        while (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }
    public static void addSongToPlaylist(String playlistName, String songName ,int user_id) throws SQLException {
        //Connection conn = null;
        int playlistId  = getPlaylistID( playlistName,  user_id);
        int songId  = getSongID(songName);
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //conn = conn;
            stmt = conn.prepareStatement("INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)",Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            stmt.executeUpdate();
        } finally {
            //close(conn, stmt, rs);
        }
    }

    public static void deleteSongFromPlaylist(String playlistName, String songName ,int user_id) throws SQLException {
        //Connection conn = null;
        int playlistId  = getPlaylistID( playlistName,  user_id);
        int songId  = getSongID(songName);

        int id  = getPlaylistSongsID(playlistId,songId);
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            //conn = conn;
            stmt = conn.prepareStatement("DELETE FROM playlist_songs WHERE id = ?",Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            //close(conn, stmt, rs);
        }
    }

    public static int getPlaylistSongsID(int playlistId, int user_id) throws SQLException {
        String sql = "SELECT id FROM playlist_songs WHERE playlist_id = ? AND song_id =?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, playlistId);
        statement.setInt(2, user_id);
        ResultSet rs = statement.executeQuery();
        int id  = 0;
        while (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

}


