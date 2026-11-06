package controllers;

import application.AuthApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class AccueilController {
    public void AccueilScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(AuthApplication.class.getResource("accueil-view.fxml"));

        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = (Stage) w;
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }
}
