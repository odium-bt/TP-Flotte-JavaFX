package fr.greta.tpflottejavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthApplication extends Application {
    /// Démarre l'application
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("auth-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 479, 366);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
