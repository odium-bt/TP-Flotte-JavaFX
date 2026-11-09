package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.dao.SalarieDAO;
import fr.greta.tpflottejavafx.dao.VehiculeDAO;
import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.RegleGestionException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.greta.tpflottejavafx.modeles.Reservations;

public class ReservationService extends Service {
    private final Logger log = LogManager.getLogger(String.valueOf(ReservationService.class));

    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;
    private final SalarieDAO salarieDAO;

    public ReservationService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO, SalarieDAO salarieDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
        this.salarieDAO = salarieDAO;
    }

    /**
     * Enregistrement du départ de la réservation
     *
     * @param immatriculation Immatriculation
     * @param matricule       Matricule
     */
    public void enregistrerDepart(String immatriculation, String matricule) {
        // TODO Zadig
    }

    /**
     * Créé une nouvelle réservation.
     */
    public void creerReservation() {
        executor.submit(() -> {// TODO new Reservation(); (attendre classe Reservation)
            log.info("Création d'une nouvelle réservation");
        });
    }

    /**
     * Affiche toutes les réservations.
     *
     * @return {@code List<String>} - Liste des réservations sous forme de string
     */
    public List<Reservation> afficherReservations() {
        // TODO Afficher réservations
        return List.of();
    }


    /**
     * Enregistre le retour d'un véhicule et la clotûre d'une réservation.
     *
     * @param immatriculation Immatriculation du véhicule
     * @param kmRetour        Kilométrage au retour
     */
    public void enregistrerRetour(String immatriculation, int kmRetour) {
        executor.submit(() -> {
            log.info("Enregistrement d'un retour");

            try {
                Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);

                // Règles de gestion
                if (v == null) {
                    throw new RegleGestionException("Véhicule inconnu");
                }
                if (!reservationDAO.estEnDeplacement(v.getId())) {
                    throw new RegleGestionException("Ce véhicule n'est pas en déplacement");
                }
                if (kmRetour < v.getKilometrage()) {
                    throw new RegleGestionException("Kilométrage inférieur au compteur actuel");
                }

                // Persistance : deux DAO pour une seule action
                reservationDAO.cloturer(v.getId(), new Timestamp(System.currentTimeMillis()), kmRetour);
                v.setKilometrage(kmRetour);
                vehiculeDAO.modifier(v);

                log.info("Retour de {} à {} km", immatriculation, kmRetour);
            } catch (DAOException | RegleGestionException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getStackTrace());
            }
        });
    }

    /**
     * Supprime la réservation.
     *
     * @param idReservation Id de la réservation
     */
    public void supprimerReservation(int idReservation) {
        executor.submit(() -> {
            log.info("Tentative de suppression d'une réservation");

            try {
                if (reservationDAO.estEnDeplacement(idReservation)) {
                    throw new RegleGestionException("Cette réservation est en cours");
                }

                reservationDAO.supprimer(idReservation);

                log.info("Réservation supprimée avec succès");
            } catch (DAOException | RegleGestionException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getStackTrace());
            }
        });
    }

    /**
     * Ferme le thread du service.
     */
    public void fermer() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public Reservations<Salarie, Timestamp, Vehicule> historiqueSalarie(String matricule) {
        return null;
    }

    public List<Vehicule> vehiculeEnDeplacement() {
        return new ArrayList<>();
    }
}