package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import service.AuthService;

import java.io.IOException;

public class AuthController {
    private final AuthService authService;

    public TextField login;
    public PasswordField pass;

    public Text error_msg;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
            if (authService.verifier(this.login.getText(), pass.getText())) {

                AccueilController accueilController = new AccueilController();
                try {
                    accueilController.AccueilScene(login.getScene().getWindow());
                } catch (IOException e) {
                    // TODO Gérer exception
                    throw new RuntimeException(e);
                }

                msg.append("Authentification réussie.");

            } else {
                msg.append("Les identifiants entrés sont invalides.");
            }
        } else {
            msg.append("Les identifiants entrés sont invalides.");
        }

        if (!msg.toString().isEmpty()) {
            error_msg.setText(msg.toString());
        }
    }

    /**
     * Nettoie les champs
     */
    @FXML
    public void onClearClick() {
        login.clear();
        pass.clear();
    }

    /**
     * Ferme l'application
     */
    @FXML
    public void onCloseClick() {
        Platform.exit();
    }
}
