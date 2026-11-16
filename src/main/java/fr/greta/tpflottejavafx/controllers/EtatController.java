package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.service.DataFactory;
import fr.greta.tpflottejavafx.service.ExportService;
import fr.greta.tpflottejavafx.service.MailService;
import fr.greta.tpflottejavafx.service.PdfService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public class EtatController {
    private static final Logger log = LogManager.getLogger(EtatController.class);

    private final PdfService pdfService = DataFactory.getInstance().getPdfService();
    private final MailService mailService = DataFactory.getInstance().getMailService();
    private final ExportService exportService = DataFactory.getInstance().getExportService();

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
    private Button envoiMail;
    @FXML
    private Button exportSalarie;
    @FXML
    private Button exportResa;
    @FXML
    private TextFlow data;
    @FXML
    private Button menuPrincipal;

    @FXML
    private void onMenuClick() {
        try {
            new AccueilController().AccueilScene(menuPrincipal.getScene().getWindow());
        } catch (IOException e) {
            log.error("Erreur : Impossible d'ouvrir l'accueil.", e);
        }
    }

    private void affichageText(String text) {
        javafx.collections.ObservableList<javafx.scene.Node> messages = data.getChildren();
        messages.clear();
        messages.add(new Text(text));
    }


    @FXML
    private void onEnvoiMailClick() {
        Task<Void> tache = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Path fichier = pdfService.creerPdfEtatFlotte().get();   // attend le PDF
                mailService.envoyerPdfParMail(fichier).get(); // puis le courriel
                return null;
            }
        };

        tache.setOnSucceeded(e -> log.info("État de la flotte généré et envoyé par courriel."));

        tache.setOnFailed(e -> {
            Throwable ex = tache.getException();
            log.trace(ex.getMessage());
            log.error("Erreur : " + (ex.getMessage() != null ? ex.getMessage() : "l'envoi a échoué"));
        });

        Thread thread = new Thread(tache, "etat-pdf-mail");
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void onExportSalarieClick() {
        export(ExportService.TypeExport.SALARIES);
    }

    @FXML
    private void onExportResaClick() {
        export(ExportService.TypeExport.RESERVATIONS);
    }

    private void export(ExportService.TypeExport type) {
        Task<String> tache = new Task<>() {

            @Override
            protected String call() throws Exception {
                exportService.chiffrerEnregistrerSalariesReservations(type).get();
                return exportService.dechiffrerEtAfficherFichierExport(type).get();
            }
        };

        tache.setOnSucceeded(e -> {
            log.info("Export chiffré des données des {} effectué.", type.getLibelle());
            affichageText(tache.getValue());
        });

        tache.setOnFailed(e -> {
            Throwable cause = tache.getException();
            if (cause instanceof ExecutionException && cause.getCause() != null) {
                cause = cause.getCause(); // la vraie erreur (DAOException, IOException...)
            }
            String message = cause.getMessage() != null ? cause.getMessage() : "l'export a échoué";
            log.error("Échec de l'export des {}", type.getLibelle(), cause);
            affichageText("Erreur : " + message);
        });

        Thread thread = new Thread(tache, "etat-export");
        thread.setDaemon(true);
        thread.start();
    }
}
