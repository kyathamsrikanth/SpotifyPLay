module com.example.spotifyplay {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.spotifyplay to javafx.fxml;
    exports com.example.spotifyplay;
}