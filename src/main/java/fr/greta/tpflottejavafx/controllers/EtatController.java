package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.service.DataFactory;
import fr.greta.tpflottejavafx.service.EtatService;
import fr.greta.tpflottejavafx.service.VehiculeService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class EtatController {

    private final EtatService vehiculeService = DataFactory.getInstance().getEtatService();

    /**
     * Création de la scène.
     *
     * @param w Fenêtre
     * @throws IOException Jette une exception si le chargement de la vue échoue
     */
    public void EtatScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AccueilController.class.getResource("/fr/greta/tpflottejavafx/views/etat-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) w;
        stage.setTitle("Gestion des états");
        stage.setScene(scene);
        stage.show();
    }
}
