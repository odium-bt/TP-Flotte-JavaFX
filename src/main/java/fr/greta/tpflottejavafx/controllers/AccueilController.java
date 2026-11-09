package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.application.AuthApplication;
import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.service.EtatService;
import fr.greta.tpflottejavafx.service.ReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import fr.greta.tpflottejavafx.modeles.Flotte;
import fr.greta.tpflottejavafx.service.VehiculeService;

import java.io.IOException;

public class AccueilController {

    VehiculeDAO vehiculeDAO = new VehiculeDAOImpl();
    SalarieDAO salarieDAO = new SalarieDAOImpl();
    ReservationDAO reservationDAO = new ReservationDAOImpl();
    Flotte flotte = new Flotte();

    @FXML
    private Button gestionFlotte;

    @FXML
    private Button gestionReservation;

    @FXML
    private Button gestionEtat;

    public void AccueilScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AuthApplication.class.getResource("/fr/greta/tpflottejavafx/views/accueil-view.fxml"));
        Scene scene = new Scene(loader.load() );
        Stage stage = (Stage) w;
        stage.setTitle("Accueil");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onGestionFlotteClick() {
        FlotteController flotteController = new FlotteController();
        try {
            flotteController.FlotteScene(gestionFlotte.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    protected void onGestionReservationClick() {
        ReservationService reservationService = new ReservationService(vehiculeDAO, reservationDAO, salarieDAO);
        ReservationController reservationController = new ReservationController(reservationService);
        /*
        try {
            reservationController.ReservationScene(gestionReservation.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @FXML
    protected void onGestionEtatClick() {
        EtatService etatService = new EtatService(vehiculeDAO, reservationDAO);
        EtatController etatController = new EtatController();
        /*
        try {
            etatController.EtatScene(gestionEtat.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

}
