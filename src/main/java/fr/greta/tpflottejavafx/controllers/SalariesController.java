package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.DomainException;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.service.DataFactory;
import fr.greta.tpflottejavafx.service.ReservationService;
import fr.greta.tpflottejavafx.service.SalariesService;
import fr.greta.tpflottejavafx.service.VehiculeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/// Contrôleur gérant les salariés
public class SalariesController {
    private final Logger log = LogManager.getLogger(String.valueOf(SalariesController.class));

    private final SalariesService salariesService = DataFactory.getInstance().getSalariesService();

    /**
     * Création de la scène.
     *
     * @param w Fenêtre
     */
    public void SalariesScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AccueilController.class.getResource("/fr/greta/tpflottejavafx/views/salaries-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) w;
        stage.setTitle("Gestion des salariés");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField matricule;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextFlow message;
    @FXML
    private TableView<Salarie> list;
    @FXML
    private TableColumn<Salarie, String> matriculeColumn;
    @FXML
    private TableColumn<Salarie, String> nameColumn;
    @FXML
    private TableColumn<Salarie, String> surnameColumn;
    @FXML
    private ListView<Salarie> list2;

    // Source de données unique, partagée par la table et la ListView
    private final ObservableList<Salarie> salaries = FXCollections.observableArrayList(
            new Salarie("111111", "Dupond", "Valérie"),
            new Salarie("123533", "Higgins", "Clark"),
            new Salarie("253644", "Pantou", "Maurice"),
            new Salarie("867452", "Parmentier", "Yvette"));

    @FXML
    private void initialize() {
        log("Gestion des Utilisateurs" + "\n");
        log("Cliquez sur Ajouter, Modifier ou Supprimer" + "\n");

        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        list.setItems(salaries);
        list2.setItems(salaries);

        // Remplit ou vide les champs selon la ligne sélectionnée
        list.getSelectionModel().selectedItemProperty().addListener((obs, oldP, newP) -> {
            matricule.setText(newP == null ? "" : newP.getMatricule());
            nom.setText(newP == null ? "" : newP.getNom());
            prenom.setText(newP == null ? "" : newP.getPrenom());
        });
    }

    @FXML
    private void onAdd() {
        String m = matricule.getText().trim();
        String n = nom.getText().trim();
        String p = prenom.getText().trim();
        if (m.isEmpty() || n.isEmpty() || p.isEmpty()) {
            log("Erreur : matricule, nom et prénom obligatoires" + "\n");
            return;
        }
        try {
            Salarie s = new Salarie(m, p, n);
            salariesService.ajouterSalarie(s);
            salaries.add(s);
            list.getSelectionModel().select(s);
            list.scrollTo(s);
            log("Ajout " + s + "\n");
        } catch (DAOException e) {
            log("Erreur : Impossible d'ajouter le salarié. Veuillez ré-essayer plus tard" + "\n");
        }
    }

    @FXML
    private void onUpdate() {
        Salarie s = selected();
        if (s == null) return;

        String mat = matricule.getText().trim();
        if (!mat.equals(s.getMatricule())) {
            log("Erreur : Modifier le matricule d'un salarié existant n'est pas autorisé.");
        }

        String nouveauNom = nom.getText().trim();
        String nouveauPrenom = prenom.getText().trim();
        try {
            salariesService.modifierSalarie(s, nouveauNom, nouveauPrenom);
            s.setNom(nouveauNom);
            s.setPrenom(nouveauPrenom);
            list.refresh();
            list2.refresh();
            log("Modif " + s + "\n");
        } catch (DAOException e) {
            log("Erreur : Impossible de modifier le salarié. Veuillez réessayer plus tard" + "\n");
        }
    }

    @FXML
    private void onDelete() {
        Salarie s = selected();
        if (s == null) return;
        try {
            salariesService.supprimerParMatricule(s.getMatricule());
            salaries.remove(s);
            log("Suppression " + s + "\n");
        } catch (DomainException e) {
            log(e.getMessage() + "\n");
        }
    }

    /**
     * Personne sélectionnée, ou null avec un message si aucune.
     */
    private Salarie selected() {
        Salarie s = list.getSelectionModel().getSelectedItem();
        if (s == null) log("Erreur : sélectionnez d'abord un salarié" + "\n");
        return s;
    }

    private void log(String text) {
        var messages = message.getChildren();
        messages.add(new Text((messages.isEmpty() ? "" : "\n") + text));
    }
}
