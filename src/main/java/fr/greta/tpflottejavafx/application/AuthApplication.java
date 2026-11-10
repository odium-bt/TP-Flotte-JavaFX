package fr.greta.tpflottejavafx.application;

import fr.greta.tpflottejavafx.service.DataFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthApplication extends Application {
    /// Démarre l'application
    @Override
    public void start(Stage stage) throws IOException {
        // Initialisation de la vue
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/fr/greta/tpflottejavafx/views/auth-view.fxml"));

        // Chargement de la fenêtre
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

}
