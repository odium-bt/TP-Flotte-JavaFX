package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.service.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/// Contrôleur gérant les réservations
public class ReservationController {
    private final Logger log = LogManager.getLogger(String.valueOf(ReservationController.class));

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // TODO CRUD Réservations (créer, lister, modifier (inclure clôturation avec l'ajout du retour), supprimer (vérifier si le véhicule n'est pas en déplacement))
}

/*


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

    }*/
