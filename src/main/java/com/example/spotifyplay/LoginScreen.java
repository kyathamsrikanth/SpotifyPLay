package com.example.spotifyplay;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoginScreen extends GridPane {
    private final SpotifyDAO backendLogic = new SpotifyDAO();
    private Label lblUsername;
    private Label lblPassword;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private Button btnLogin;
    private Button btnSignUp;

    public LoginScreen(Stage primaryStage) throws SQLException {
        // Initialize the UI components
        lblUsername = new Label("Username:");
        lblPassword = new Label("Password:");
        txtUsername = new TextField();
        txtPassword = new PasswordField();
        btnLogin = new Button("Login");
        btnSignUp = new Button("Sign Up");

        // Set the properties of the UI components
        txtUsername.setPrefWidth(200);
        txtPassword.setPrefWidth(200);
        btnLogin.setPrefWidth(100);
        btnSignUp.setPrefWidth(100);

        // Define the layout of the UI components
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));
        add(lblUsername, 0, 0);
        add(txtUsername, 1, 0);
        add(lblPassword, 0, 1);
        add(txtPassword, 1, 1);
        add(btnLogin, 0, 2);
        add(btnSignUp, 1, 2);

        // Add event handlers to the UI components
        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            User isAuthenticated =null;
            try {
                isAuthenticated = backendLogic.getUserByUsernameAndPassword(username, password);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (isAuthenticated!=null) {
                // Navigate to the main screen
                //primaryStage.setScene(new MainScreen().getScene());
                List<Song> songs = null;
                try {
                    songs =  backendLogic.getAllSongs();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                MainScreen root = null;
                try {
                    root = new MainScreen(songs,isAuthenticated.getId());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Scene scene = new Scene(root, 800, 600);
                primaryStage.setTitle("Spotify App");
                primaryStage.setScene(scene);
                primaryStage.show();
            } else {
                // Show an error message
                showError("Invalid username or password.");
            }
            // Check if the username and password are correct
            // If yes, open the main screen
            // If no, display an error message
        });

        btnSignUp.setOnAction(e -> {
            // Open the sign up screen
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
