package controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr.greta.tpflottejavafx.AuthApplication;
import fr.greta.tpflottejavafx.Configuration;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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

    public static class AuthController extends Configuration {
        public TextField login;
        public PasswordField pass;
        public Text errormsg;

        /**
         * Nettoie les champs
         */
        @FXML
        public void onClearClick() {
            login.clear();
            pass.clear();
        }

        /**
         * Vérifie les identifiants entrés
         */
        @FXML
        public void chkAuth() {
            StringBuilder msg = new StringBuilder();

            if (this.login.getText().isEmpty()) {
                msg.append("Le champ login est vide !\n");
            }

            if (pass.getText().isEmpty()) {
                msg.append("Le mot de passe est vide !\n");
            }

            if (!this.login.getText().isEmpty() && !pass.getText().isEmpty()) {
                if (this.login.getText().equals(Configuration.login)) {
                    BCrypt.Result res = BCrypt.verifyer().verify(pass.getText().toCharArray(), Configuration.password);
                    if (res.verified) {
                        AccueilController accueilController = new AccueilController();
                        try {
                            accueilController.AccueilScene(login.getScene().getWindow());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        msg.append("Authentification réussie.");
                    } else {
                        msg.append("Les identifiants entrés sont invalides.");
                    }
                } else {
                    msg.append("Les identifiants entrés sont invalides.");
                }
            }

            if (!msg.toString().isEmpty()) {
                errormsg.setText(msg.toString());
            }
        }

        /**
         * Ferme l'application
         */
        @FXML
        public void onCloseClick() {
            Platform.exit();
        }

    }
}
