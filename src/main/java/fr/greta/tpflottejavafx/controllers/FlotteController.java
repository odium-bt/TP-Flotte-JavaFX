package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.modeles.Vehicule;
import fr.greta.tpflottejavafx.service.DataFactory;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/// Contrôleur gérant la flotte
public class FlotteController {

    /**
     * Création de la scène.
     *
     * @param w Fenêtre
     * @throws IOException Jette une exception si le chargement de la vue échoue
     */
    public void FlotteScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AccueilController.class.getResource("/fr/greta/tpflottejavafx/views/flotte-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) w;
        stage.setTitle("Flotte de véhicules");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField imma;
    @FXML
    private TextField marque;
    @FXML
    private TextField modele;
    @FXML
    private TextField dateCircul;
    @FXML
    private TextField km;
    @FXML
    private TextField etat;
    @FXML
    private TextFlow message;
    @FXML
    private TableView<Vehicule> list;
    @FXML
    private TableColumn<Vehicule, String> immaColumn;
    @FXML
    private TableColumn<Vehicule, String> marqueColumn;
    @FXML
    private TableColumn<Vehicule, String> modeleColumn;
    @FXML
    private TableColumn<Vehicule, LocalDate> dateColumn;
    @FXML
    private TableColumn<Vehicule, Integer> kmColumn;
    @FXML
    private TableColumn<Vehicule, String> etatColumn;
    @FXML
    private ListView<Vehicule> list2;


    // Source de données unique, partagée par la table et la ListView
    private final ObservableList<Vehicule> listVehicules = FXCollections.observableArrayList(
            DataFactory.getInstance().afficherListVehicules()
    );

    @FXML
    private void initialize() {
        //log("Gestion de la flotte de Véhiclues");
        //log("Cliquez sur Ajouter, Modifier ou Supprimer");

        immaColumn.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modeleColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateMiseEnCirculation"));
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));
        //etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
        list.setItems(listVehicules);
        //list2.setItems(listVehicules);

        // Remplit ou vide les champs selon la ligne sélectionnée
        list.getSelectionModel().selectedItemProperty().addListener((obs, oldP, newP) -> {
            imma.setText(newP == null ? "" : newP.getImmatriculation());
            marque.setText(newP == null ? "" : newP.getMarque());
            modele.setText(newP == null ? "" : newP.getModele());
            dateCircul.setText(newP == null ? "" : String.valueOf(newP.getDateMiseEnCirculation()));
            km.setText(newP == null ? "" : String.valueOf(newP.getKilometrage()));
            //etat.setText(newP == null ? "" : newP.getSurname());
        });
    }


    @FXML
    private void onAdd() {
        String i = imma.getText().trim();
        String ma = marque.getText().trim();
        String mo = modele.getText().trim();
        String dateTxt = dateCircul.getText().trim();
        String kmTxt = km.getText().trim();
        if (i.isEmpty() || ma.isEmpty() || mo.isEmpty() || dateTxt.isEmpty() || kmTxt.isEmpty()) {
            log("Erreur : champs obligatoires");
            return;
        }

        LocalDate date = null;
        try {
            date=LocalDate.parse(dateTxt);
        } catch (DateTimeParseException e) {
            log("Erreur : date invalide (format attendu : AAAA-MM-JJ)");
        }
        if (date.isAfter(LocalDate.now())) {
            log("Erreur : la date de mise en circulation est dans le futur");
            return;
        }

        int k;
        try {
            k = Integer.parseInt(kmTxt);
        } catch (NumberFormatException e) {
            log("Erreur : le kilométrage doit être un nombre entier");
            return;
        }

        if (k < 0) {
            log("Erreur : le kilométrage ne peut pas être négatif");
            return;
        }

        Vehicule v = new Vehicule(i, ma, mo, date, k);
        listVehicules.add(v);
        list.getSelectionModel().select(v);
        list.scrollTo(v);
        log("Ajout " + v);
    }
/*
    @FXML
    private void onUpdate() {
        Personne p = selected();
        if (p == null) return;
        p.setName(name.getText().trim());
        p.setSurname(surname.getText().trim());
        list.refresh();
        list2.refresh();
        log("Modif " + p);
    }

    @FXML
    private void onDelete() {
        Personne p = selected();
        if (p == null) return;
        persons.remove(p);
        log("Suppression " + p);
    }

    private Personne selected() {
        Personne p = list.getSelectionModel().getSelectedItem();
        if (p == null) log("Erreur : sélectionnez d'abord une personne");
        return p;
    }
*/
    private void log(String text) {
        var messages = message.getChildren();
        messages.add(new Text((messages.isEmpty() ? "" : "\n") + text));
    }



    //   public void ajouterVehicule() {
    //       String immatriculation = vue.saisieTexte("Immatriculation (AA-123-AA)").toUpperCase();
    //       String marque = vue.saisieTexte("Marque : ");
    //       String modele = vue.saisieTexte("Modèle : ");
    //       String dateSaisie = vue.saisieTexte("Mise en circulation (jj/MM/aaaa) : ");
    //       int kilometrage = vue.saisieEntier("Kilométrage : ");
//
    //       try {
    //           LocalDate dateMiseEnCirculation = LocalDate.parse(dateSaisie, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//
    //           new Thread(() -> {
    //               try {
    //                   vehiculeService.ajouterVehicule(immatriculation, marque, modele, dateMiseEnCirculation, kilometrage);
//
    //                   vue.afficherSucces("Véhicule ajouté!");
    //               } catch (DAOException e) {
    //                   vue.afficherRefus(e.getMessage());
    //               }
    //           }).start();
    //       } catch (DateTimeParseException e) {
    //           vue.afficherRefus(e.getMessage());
    //       }
    //   }
//
    //   public void afficherFlotte() {
//
    //       List<Vehicule> vehicules = vehiculeService.afficherFlotte();
    //       if (vehicules == null) {
    //           vue.afficherRefus("Impossible de récupérer la liste");
    //           return;
    //       }
    //       vue.afficherVehicules(vehicules);
    //   }
//
    //   public void retirerVehicule() {
    //       String immatriculation = vue.saisieTexte("Immatriculation du véhicule à retirer").toUpperCase();
    //       String confirmation = vue.saisieTexte("Confirmer le retrait de " + immatriculation + " ? (o/n)");
//
    //       if (confirmation.equalsIgnoreCase("o")) {
    //           Vehicule vehicule = vehiculeService.rechercheParImmatriculation(immatriculation);
    //           if (vehicule == null) {
    //               vue.afficherRefus("véhicule introuvable.");
    //           } else {
    //               vehiculeService.retirerVehicule(immatriculation);
    //               vue.afficherSucces("Véhicule " + immatriculation + " retiré de la flotte.");
    //           }
    //       } else {
    //           vue.afficherSucces("Retrait annulé.");
    //       }
    //   }
//
//
    //   public void rechercherVehicule() {
//
    //       String immatriculation = vue.saisieTexte("Saisissez l'immatriculation :").toUpperCase();
    //       Vehicule vehicule = vehiculeService.rechercheParImmatriculation(immatriculation);
//
    //       if (vehicule == null) {
    //           vue.afficherRefus("Véhicule introuvable");
    //       } else {
    //           // List.of pour un rendu plus propre que le toString
    //           vue.afficherVehicules(List.of(vehicule));
    //       }
    //   }
//
    //   public void trierFlotte() {
    //       vue.afficherSucces("1. Par marque, puis modèle");
    //       vue.afficherSucces("2. Par date de mise en circulation (plus ancien d'abord)");
    //       vue.afficherSucces("3. Par kilométrage décroissant");
    //       int choix = vue.saisieEntier("Tri");
//
    //       switch (choix) {
    //           case 1:
    //               vehiculeService.trierMarque();
    //               break;
    //           case 2:
    //               vehiculeService.trierDate();
    //               break;
    //           case 3:
    //               vehiculeService.trierKilometrageDecroissant();
    //               break;
    //           default:
    //               vue.afficherRefus("choix de tri inconnu.");
    //               return;
    //       }
    //       afficherFlotte();
    //   }
//
//
}//
//