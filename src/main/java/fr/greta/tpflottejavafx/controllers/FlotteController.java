package fr.greta.tpflottejavafx.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import fr.greta.tpflottejavafx.service.VehiculeService;

import java.io.IOException;

public class FlotteController {
    private final VehiculeService vehiculeService;

    public FlotteController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    public void FlotteScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(AccueilController.class.getResource("flotte-view.fxml"));

        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = (Stage) w;
        stage.setTitle("Flotte de véhicule");
        stage.setScene(scene);
        stage.show();
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