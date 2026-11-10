package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.service.DataFactory;
import fr.greta.tpflottejavafx.service.EtatService;
import fr.greta.tpflottejavafx.service.VehiculeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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


    @FXML
    private TextField courriel;
    @FXML
    private Button envoiMail;
    @FXML
    private Button exportPdf;
    @FXML
    private TextFlow message;
    @FXML
    private Button menuPrincipal;

    @FXML
    private void onMenuClick() {
        try {
            new AccueilController().AccueilScene(menuPrincipal.getScene().getWindow());
        } catch (IOException e) {
            log("Erreur : Impossible d'ouvrir l'accueil.");
            e.printStackTrace();
        }
    }

    private void log(String text) {
        var messages = message.getChildren();
        messages.add(new Text((messages.isEmpty() ? "" : "\n") + text));
    }


    @FXML
    private void onEnvoiMailClick() {

    }

    @FXML
    private void onExportClick() {

    }

}
