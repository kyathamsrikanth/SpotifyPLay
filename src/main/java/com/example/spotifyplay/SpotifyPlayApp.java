package com.example.spotifyplay;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SpotifyPlayApp extends Application {

    // Define global variables for the UI components

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Create UI components and set their properties

        // Define the layout of the UI components

        // Add event handlers to the UI components

        // Create the scene and set it on the primary stage
        LoginScreen root =  new LoginScreen(primaryStage);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Spotify App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
