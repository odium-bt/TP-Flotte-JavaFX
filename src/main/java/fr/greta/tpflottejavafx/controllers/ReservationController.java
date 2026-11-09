package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.service.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReservationController {
    private final Logger log = LogManager.getLogger(String.valueOf(ReservationController.class));

    private final ReservationService reservationService;

    // Salariés
    @FXML
    private TextField matricule;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
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
    private final ObservableList<Salarie> salaries = FXCollections.observableArrayList();

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @FXML
    private void onAdd() {
        String m = matricule.getText().trim();
        String n = nom.getText().trim();
        String p = prenom.getText().trim();
        if (n.isEmpty() || p.isEmpty()) {
            log.error("Erreur : nom et prénom obligatoires");
            return;
        }
        Salarie s = new Salarie(m, n, p);
        salaries.add(s);
        list.getSelectionModel().select(s);
        list.scrollTo(s);
        log.info("Ajout " + s);
    }

    @FXML
    private void onUpdate() {
        Salarie s = selected();
        if (s == null) return;
        s.setNom(nom.getText().trim());
        s.setPrenom(prenom.getText().trim());
        list.refresh();
        list2.refresh();
        log.info("Modif {}", s);
    }

    @FXML
    private void onDelete() {
        Salarie s = selected();
        if (s == null) return;
        salaries.remove(s);
        log.info("Suppression {}", s);
    }

    /**
     * Personne sélectionnée, ou null avec un message si aucune.
     */
    private Salarie selected() {
        Salarie s = list.getSelectionModel().getSelectedItem();
        if (s == null) log.error("Erreur : sélectionnez d'abord un salarié");
        return s;
    }
}

/*

    public void ajouterSalarie() {

        String matricule = vue.saisieTexte("Matricule (6 chiffres) : ");
        String nom = vue.saisieTexte("Nom : ");
        String prenom = vue.saisieTexte("Prénom : ");
        try {
            Salarie salarie = new Salarie(matricule, prenom, nom);
            reservationService.ajouterSalarie(salarie);
            vue.afficherSucces("Le salarié matriculé " + matricule + " a bien été ajouté!");
        } catch (RegleGestionException | IllegalArgumentException e) {
            vue.afficherRefus(e.getMessage());
        }
    }

    public void departReservation() {

        String matricule = vue.saisieTexte("Matricule du salarié : ");
        String immatricule = vue.saisieTexte("Immatriculation : ").toUpperCase();

        try {
            reservationService.enregistrerDepart(immatricule, matricule);
            vue.afficherSucces("La réservation a été validée avec succès");
        } catch (RegleGestionException e) {
            vue.afficherRefus(e.getMessage());
        }
    }

    public void retourReservation() {

        String immatriculation = vue.saisieTexte("Immatriculation : ").toUpperCase();
        int kilometrage = vue.saisieEntier("Kilométrage au compteur : ");

        try {
            reservationService.enregistrerRetour(immatriculation, kilometrage);
            vue.afficherSucces("Le retour a bien été enregistré.");
        } catch (RegleGestionException e) {
            vue.afficherRefus(e.getMessage());
        }
    }

    public void vehiculeEnDeplacement() {

        vue.afficherVehicules(reservationService.vehiculeEnDeplacement());
    }


    public void historiqueSalarie() {
        String matricule = vue.saisieTexte("Matricule : ");
        Reservations<Salarie, Timestamp, Vehicule> historique = reservationService.historiqueSalarie(matricule);

        if (historique == null) {
            vue.afficherRefus("Le salarié est introuvable.");
        } else {
            vue.afficherHistorique(historique);
        }
    }*/
