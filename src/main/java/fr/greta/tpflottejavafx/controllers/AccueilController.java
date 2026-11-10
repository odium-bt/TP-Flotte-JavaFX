package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.application.AuthApplication;
import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/// Contrôleur gérant la fenêtre d'accueil
public class AccueilController {

    @FXML
    private Button gestionFlotte;

    @FXML
    private Button gestionReservation;

    @FXML
    private Button gestionEtat;

    public void AccueilScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AuthApplication.class.getResource("/fr/greta/tpflottejavafx/views/accueil-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) w;
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onGestionFlotteClick() {
        try {
            new FlotteController(DataFactory.getInstance().getVehiculeService()).FlotteScene(gestionFlotte.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onGestionSalarieClick() {
        SalariesController salariesController = new SalariesController(DataFactory.getInstance().getSalariesService());

        salariesController.SalariesScene(gestionReservation.getScene().getWindow());
    }

    @FXML
    protected void onGestionReservationClick() {
        ReservationController reservationController = new ReservationController(DataFactory.getInstance().getReservationService());

        reservationController.ReservationScene(gestionReservation.getScene().getWindow());
    }


    @FXML
    protected void onGestionEtatClick() {
        try {
            new ReservationController().ReservationScene(gestionEtat.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
