package application;

import controllers.AuthController;
import controllers.FlotteController;
import dao.DAOFactory;
import dao.ReservationDAO;
import dao.VehiculeDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modeles.Flotte;
import service.AuthService;
import service.VehiculeService;

import java.io.IOException;

public class AuthApplication extends Application {
    /// Démarre l'application
    @Override
    public void start(Stage stage) throws IOException {
        // Dépendances
        Flotte flotte = new Flotte();

        DAOFactory daoFactory = DAOFactory.getInstance();
        VehiculeDAO vehiculeDAO = daoFactory.getVehiculeDAO();
        ReservationDAO reservationDAO = daoFactory.getReservationDAO();

        AuthService authService = new AuthService();
        VehiculeService vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);

        // Initialisation de la vue
        FXMLLoader fxmlLoader = new FXMLLoader(AuthApplication.class.getResource("/fr/greta/tpflottejavafx/auth-view.fxml"));

        // Injection du contrôleur
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == AuthController.class) {
                return new AuthController(authService);
            }

            if (controllerClass == FlotteController.class) {
                return new FlotteController(vehiculeService);
            }

            throw new IllegalArgumentException("Contrôleur inconnu : " + controllerClass);
        });

        // Chargement de la fenêtre
        Scene scene = new Scene(fxmlLoader.load(), 479, 366);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
