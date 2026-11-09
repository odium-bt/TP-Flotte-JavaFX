package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.modeles.Reservations;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;
import fr.greta.tpflottejavafx.exceptions.RegleGestionException;
import fr.greta.tpflottejavafx.service.ReservationService;
import fr.greta.tpflottejavafx.vue.MenuConsole;

import java.sql.Timestamp;

public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;

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
}
