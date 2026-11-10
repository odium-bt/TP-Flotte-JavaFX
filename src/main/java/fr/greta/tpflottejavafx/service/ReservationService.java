package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.dao.SalarieDAO;
import fr.greta.tpflottejavafx.dao.VehiculeDAO;
import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.RegleGestionException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
     * Créé une nouvelle réservation.
     */
    public int creerReservation(String matricule, int idVehicule, LocalDate dateDepart) throws DAOException {
        log.info("Création d'une réservation");

        try {
            int id = reservationDAO.creer(matricule, idVehicule, dateDepart);
            log.info("Salarié créé");
            return id;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }
    }

    /**
     * Affiche toutes les réservations.
     *
     * @return {@code List<String>} - Liste des réservations sous forme de string
     */
    public List<Reservation> afficherReservations() {
        log.info("Affichage des réservations");

        List<Reservation> res = new ArrayList<>();

        try {
            reservationDAO.lister();
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }

        return res;
    }

    public void modifierReservation(int id, String matricule, int idVehicule, LocalDate dateDepart) {
        log.info("Modification de la réservation {}", id);

        try {
            reservationDAO.modifier(id, matricule, idVehicule, Timestamp.valueOf(dateDepart.atStartOfDay()));

            log.info("Réservation mise à jour");
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }
    }

    public List<Integer> afficherVehiculeNonDisponible() {
        return reservationDAO.vehiculesEnDeplacement();
    }

    /**
     * Enregistre le retour d'un véhicule et la clotûre d'une réservation.
     *
     * @param id         Id du véhicule
     * @param dateRetour Immatriculation du véhicule
     * @param kmRetour   Kilométrage au retour
     */
    public void enregistrerRetour(int id, LocalDate dateRetour, int kmRetour) throws DAOException, RegleGestionException {
        log.info("Enregistrement d'un retour");
        try {
            Reservation r = reservationDAO.trouverParId(id);
            Vehicule v = r.getVehicule();

            // Règles de gestion
            if (v == null) {
                throw new RegleGestionException("Véhicule inconnu");
            }
            if (!reservationDAO.estEnDeplacementParIdVehicule(v.getId())) {
                throw new RegleGestionException("Ce véhicule n'est pas en déplacement");
            }
            if (kmRetour < v.getKilometrage()) {
                throw new RegleGestionException("Kilométrage inférieur au compteur actuel");
            }

            reservationDAO.cloturer(v.getId(), Timestamp.valueOf(dateRetour.atStartOfDay()), kmRetour);
            v.setKilometrage(kmRetour);
            vehiculeDAO.modifier(v);

            log.info("Retour de {} à {} km", v.getImmatriculation(), kmRetour);
        } catch (DAOException | RegleGestionException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }
    }

    /**
     * Supprime la réservation.
     *
     * @param idReservation Id de la réservation
     */
    public void supprimerReservation(int idReservation) throws DAOException, RegleGestionException {
        log.info("Tentative de suppression d'une réservation");

        try {
            if (reservationDAO.estEnDeplacementParId(idReservation)) {
                throw new RegleGestionException("Cette réservation est en cours");
            }

            reservationDAO.supprimer(idReservation);

            log.info("Réservation supprimée avec succès");
        } catch (DAOException | RegleGestionException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }
    }

    public List<Reservation> historiqueSalarie(String matricule) {
        return reservationDAO.trouverParMatricule(matricule);
    }
}