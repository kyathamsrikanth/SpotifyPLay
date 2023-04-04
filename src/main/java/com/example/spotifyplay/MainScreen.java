package com.example.spotifyplay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainScreen extends BorderPane {

    private static final SpotifyDAO backendLogic;

    static {
        try {
            backendLogic = new SpotifyDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private static final ObservableList<String> navItems =
            FXCollections.observableArrayList("Home", "Artists", "Albums", "Playlists");

    private  List<Song> songs ;
    private  List<Playlist> playlists;



    ;
    private  List<String> artists ;
    private  List<String> albums ;

    private VBox navBar;

    private Label songLabel;
    private HBox topBar;
    private HBox bottomBar;
    private HBox albumBar;
    private HBox artistBar;
    private HBox playlistTopBar;
    private TextField searchBar;
    private TextField playlistBar;
    private TextField artistsSearchBar;

    private TextField albumsSearchBar;
    private Button addButton;

    private Button pauseButton;
    private Button playButton;

    private Button createButton;

    private Button removeButton;

    private Button viewButton;
    private ListView<String> listView;

    private  int user_id;

    public MainScreen(List<Song> songs,int user_id) throws SQLException {
        playlists = backendLogic.getPlaylistsByUser(user_id);
        albums = backendLogic.getAllAlbums();
        artists = backendLogic.getAllArtists();
        this.songs = songs;
        this.user_id = user_id;
        initNavBar();
        initTopBar();
        initListView();
        playlistTopBar();
        initalbumBar();
        initartistBar();
        initBottomBar();
        setLeft(navBar);
        setTop(topBar);
        setCenter(listView);
    }

    private void initNavBar() {
        navBar = new VBox();
        navBar.setPadding(new Insets(10));
        navBar.setSpacing(10);
        navBar.setStyle("-fx-background-color: #333");

        for (String item : navItems) {
            Button button = new Button(item);
            button.setTextFill(Color.BLACK);
            button.setFont(Font.font("System", FontWeight.BOLD, 16));
            button.setPrefSize(120, 40);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                try {
                    addEventListner(item,button);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            navBar.getChildren().add(button);
        }
    }

    private void addEventListner(String item,Button button) throws SQLException {

        if(item.equals("Playlists")){
            button.setOnAction(e->{
                listView.getItems().setAll(playlists.stream().map(Playlist::getName).collect(Collectors.toList()));
                setCenter(listView);
                setTop(playlistTopBar);

            });
        } else if (item.equals("Artists")) {
            button.setOnAction(e->{
                listView.getItems().setAll(artists);
                setCenter(listView);
                setTop(artistBar);
            });
        }else if (item.equals("Home")) {
            button.setOnAction(e->{
                listView.getItems().setAll(songs.stream().map(Song::getTitle).collect(Collectors.toList()));
                setCenter(listView);
                setTop(playlistTopBar);
                setTop(topBar);
            });
        }else if (item.equals("Albums")) {
            button.setOnAction(e -> {
                listView.getItems().setAll(albums);
                setCenter(listView);
                setTop(albumBar);
            });
        }

    }

    private void initTopBar() {
        topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setStyle("-fx-background-color: #eee");

        searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);
        topBar.getChildren().add(searchBar);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // filter the songs based on the artist name
            List<String> filteredSongs = filterSongs(newValue);
            listView.getItems().setAll(filteredSongs);
        });

        addButton = new Button("Add To PlayList");
        addButton.setPrefSize(150, 40);
        topBar.getChildren().add(addButton);

        List<String> playlistsNames = playlists.stream().map(Playlist::getName).collect(Collectors.toList());
        ChoiceDialog d = new ChoiceDialog(playlistsNames.get(0),playlistsNames);

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // setheader text
                d.setHeaderText("Select PlayList");

                // set content text
                // d.setContentText("please select the day of the week");

                // show the dialog
                d.showAndWait();

                // get the selected item
                String PlaylistName = (String) d.getSelectedItem();
                String songName = listView.getSelectionModel().getSelectedItem();
                try {
                    SpotifyDAO.addSongToPlaylist(PlaylistName,songName,user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        addButton.setOnAction(event);

        playButton = new Button("Play Song");
        playButton.setPrefSize(80, 40);
        topBar.getChildren().add(playButton);

        EventHandler<ActionEvent> eventPlay = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // show the text input dialog
                String song = listView.getSelectionModel().getSelectedItem();
                songLabel.setText("Playing : " + song);
                songLabel.setPadding(new Insets(4, 0, 0, 4));
                songLabel.setTextFill(Color.DARKGRAY);
                songLabel.setFont(Font.font("System", FontWeight.NORMAL, 20));
                setBottom(bottomBar);

            }
        };

        // set on action of event
        playButton.setOnAction(eventPlay);

        removeButton = new Button("Remove From Playlist");
        removeButton.setPrefSize(150, 40);
        topBar.getChildren().add(removeButton);

        EventHandler<ActionEvent> eventRemove = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // setheader text
                d.setHeaderText("Select PlayList");

                // set content text
                // d.setContentText("please select the day of the week");

                // show the dialog
                d.showAndWait();

                // get the selected item
                String PlaylistName = (String) d.getSelectedItem();
                String songName = listView.getSelectionModel().getSelectedItem();
                try {
                    SpotifyDAO.deleteSongFromPlaylist(PlaylistName,songName,user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                List<String> songsByPlaylist =  new ArrayList<>();
                try {
                    songsByPlaylist = SpotifyDAO.getPlaylistsSongsByPlaylist(PlaylistName,user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                listView.getItems().clear();
                listView.getItems().addAll(songsByPlaylist);
                setCenter(listView);
                setTop(topBar);
            }
        };
        removeButton.setOnAction(eventRemove);


    }
    private void initBottomBar() {
        bottomBar = new HBox();
        bottomBar.setPadding(new Insets(10));
        bottomBar.setSpacing(10);
        bottomBar.setStyle("-fx-background-color: #eee");

        songLabel = new Label("");
        songLabel.setPrefWidth(400);
        bottomBar.getChildren().add(songLabel);


        pauseButton = new Button("Pause");
        pauseButton.setPrefSize(80, 40);
        bottomBar.getChildren().add(pauseButton);


    }

    private void initartistBar() {
        artistBar = new HBox();
        artistBar.setPadding(new Insets(10));
        artistBar.setSpacing(10);
        artistBar.setStyle("-fx-background-color: #eee");

        artistsSearchBar = new TextField();
        artistsSearchBar.setPromptText("Search...");
        artistsSearchBar.setPrefWidth(300);
        artistBar.getChildren().add(artistsSearchBar);

        artistsSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // filter the songs based on the artist name
            List<String> filteredSongs = filterArtist(newValue);
            listView.getItems().setAll(filteredSongs);
        });


        playButton = new Button("Play");
        playButton.setPrefSize(80, 40);
        artistBar.getChildren().add(playButton);
    }

    private void initalbumBar() {
        albumBar = new HBox();
        albumBar.setPadding(new Insets(10));
        albumBar.setSpacing(10);
        albumBar.setStyle("-fx-background-color: #eee");

        albumsSearchBar = new TextField();
        albumsSearchBar.setPromptText("Search...");
        albumsSearchBar.setPrefWidth(300);
        albumBar.getChildren().add(albumsSearchBar);

        albumsSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // filter the songs based on the artist name
            List<String> filteredSongs = filterAlbum(newValue);
            listView.getItems().setAll(filteredSongs);
        });

        addButton = new Button("Add");
        addButton.setPrefSize(80, 40);
        albumBar.getChildren().add(addButton);

    }

    private void playlistTopBar() {
        playlistTopBar = new HBox();
        playlistTopBar.setPadding(new Insets(10));
        playlistTopBar.setSpacing(10);
        playlistTopBar.setStyle("-fx-background-color: #eee");

        playlistBar = new TextField();
        playlistBar.setPromptText("Search...");
        playlistBar.setPrefWidth(300);
        playlistTopBar.getChildren().add(playlistBar);

        playlistBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // filter the songs based on the artist name
            List<String> filteredSongs = filterPlaylist(newValue);
            listView.getItems().setAll(filteredSongs);
        });

        createButton = new Button("Create");
        createButton.setPrefSize(80, 40);
        playlistTopBar.getChildren().add(createButton);

        TextInputDialog td = new TextInputDialog("");

        // setHeaderText
        td.setHeaderText("Enter PlayList Name");

        // create a button


        // create a event handler
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // show the text input dialog
                Optional<String> result = td.showAndWait();
                result.ifPresent(string -> {
                    try {
                        System.out.println(string);
                        Playlist playlistCreated = SpotifyDAO.createPlaylist(string,user_id);
                        playlists = backendLogic.getPlaylistsByUser(user_id);
                        System.out.println(playlists);
                        listView.getItems().removeAll(playlists.stream().map(Playlist::getName).collect(Collectors.toList()));
                        listView.getItems().addAll(playlists.stream().map(Playlist::getName).collect(Collectors.toList()));
                        setCenter(listView);
                        setTop(playlistTopBar);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        };

        // set on action of event
        createButton.setOnAction(event);



        removeButton = new Button("Remove");
        removeButton.setPrefSize(80, 40);
        playlistTopBar.getChildren().add(removeButton);


        // create a event handler
        EventHandler<ActionEvent> eventRemove = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // show the text input dialog
                String playList = listView.getSelectionModel().getSelectedItem();
                try {
                    Playlist playlistRemoved = SpotifyDAO.removePlaylist(playList,user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    playlists = backendLogic.getPlaylistsByUser(user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(playlists);
                listView.getItems().clear();
                listView.getItems().addAll(playlists.stream().map(Playlist::getName).collect(Collectors.toList()));
                setCenter(listView);
                setTop(playlistTopBar);


            }
        };

        // set on action of event
        removeButton.setOnAction(eventRemove);


        viewButton = new Button("View");
        viewButton.setPrefSize(80, 40);
        playlistTopBar.getChildren().add(viewButton);


        // create a event handler
        EventHandler<ActionEvent> eventView = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            { List<String> songsByPlaylist =  new ArrayList<>();
                // show the text input dialog
                String playList = listView.getSelectionModel().getSelectedItem();
                try {
                     songsByPlaylist = SpotifyDAO.getPlaylistsSongsByPlaylist(playList,user_id);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
//                try {
//                    playlists = backendLogic.getPlaylistsByUser(user_id);
//                } catch (SQLException ex) {
//                    throw new RuntimeException(ex);
//                }

                listView.getItems().clear();
                listView.getItems().addAll(songsByPlaylist);
                setCenter(listView);
                setTop(topBar);


            }
        };

        // set on action of event
        viewButton.setOnAction(eventView);
    }


    private List<String> filterSongs( String artistName) {
        // filter the songs by artist name
        return songs.stream().map(Song::getTitle).filter(title -> title.toLowerCase().contains(artistName.toLowerCase())).collect(Collectors.toList());
    }
    private List<String> filterPlaylist( String artistName) {
        // filter the songs by artist name
        return playlists.stream().map(Playlist::getName).filter(title -> title.toLowerCase().contains(artistName.toLowerCase())).collect(Collectors.toList());
    }
    private List<String> filterAlbum( String artistName) {
        // filter the songs by artist name
        return albums.stream().filter(title -> title.toLowerCase().contains(artistName.toLowerCase())).collect(Collectors.toList());
    }
    private List<String> filterArtist( String artistName) {
        // filter the songs by artist name
        return artists.stream().filter(title -> title.toLowerCase().contains(artistName.toLowerCase())).collect(Collectors.toList());
    }

    private void initListView() {
        listView = new ListView<>();
        listView.setPadding(new Insets(10));
        listView.setItems(FXCollections.observableList(songs.stream().map(Song::getTitle).collect(Collectors.toList())));
        listView.setPrefSize(600, 400);
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public ListView<String> getListView() {
        return listView;
    }
}