package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.application.AuthApplication;
import fr.greta.tpflottejavafx.dao.*;
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
    private Button choix1;

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
    protected void onChoix1Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix2Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    protected void onChoix3Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix4Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix5Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix6Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix7Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix8Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    protected void onChoix9Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix10Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    protected void onChoix11Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix12Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onChoix13Click() {

        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        FlotteController flotteController = new FlotteController(vehiculeService);
        try {
            flotteController.FlotteScene(choix1.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
