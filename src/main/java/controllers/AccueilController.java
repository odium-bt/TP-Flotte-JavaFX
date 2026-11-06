package controllers;

import application.AuthApplication;
import dao.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import modeles.Flotte;
import service.VehiculeService;

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
        loader.setLocation(AuthApplication.class.getResource("accueil-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
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



}
