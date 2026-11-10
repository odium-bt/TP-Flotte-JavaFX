package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.service.DataFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import fr.greta.tpflottejavafx.service.AuthService;

import java.io.IOException;

/// Contrôleur gérant la fenêtre d'authentification
public class AuthController {
    private AuthService authService;

    @FXML
    public TextField login;
    @FXML
    public PasswordField pass;

    @FXML
    public Text error_msg;

    @FXML
    protected void initialize() {
        this.authService = new AuthService();
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
                    error_msg.setText("Impossible d'ouvrir l'accueil.");
                    throw new RuntimeException(e);
                }

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
