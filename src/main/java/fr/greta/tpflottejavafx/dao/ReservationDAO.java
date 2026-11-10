package fr.greta.tpflottejavafx.dao;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.sql.Timestamp;
import java.util.List;

public interface ReservationDAO {
    /**
     * Création d'une nouvelle réservation dans la base de données.
     *
     * @param reservation La réservation à créer
     * @param salarie     Le salarié qui effectue la réservation
     */
    void creer(Reservation reservation, Salarie salarie) throws DAOException;

    /**
     * Cherche les réservations associées au salarié à qui appartient la matricule donnée
     *
     * @param matricule Le matricule du salarié
     * @return Un objet Reservations contenant les réservations associées à un salarié
     */
    List<Reservation> trouverParMatricule(String matricule) throws DAOException;

    /**
     * Récupère toutes les réservations stockées en base.
     *
     * @return Un objet {@code List<Reservations>} contenant chaque réservation stockée en base
     */
    List<Reservation> lister() throws DAOException;

    /**
     * Trouve si une réservation est actuellement en cours.
     *
     * @param idReservation Id de la réservation
     * @return {@code boolean}
     */
    boolean estEnDeplacementParId(int idReservation) throws DAOException;

    /**
     * Trouve si un véhicule est actuellement en cours de déplacement.
     *
     * @param idVehicule Id du véhicule
     * @return {@code boolean}
     */
    boolean estEnDeplacementParIdVehicule(int idVehicule) throws DAOException;

    /**
     * Trouve si un salarié est actuellement en cours de déplacement.
     *
     * @param matricule Matricule du salarié
     * @return {@code boolean}
     */
    boolean estEnDeplacementParMatricule(int matricule) throws DAOException;

    /**
     * Modifie une réservation.
     *
     * @param idReservation Id de la réservation
     * @param dateDepart    Date de départ
     * @param vehicule      Véhicule
     */
    void modifier(int idReservation, Timestamp dateDepart, Vehicule vehicule) throws DAOException;

    /**
     * Clotûre une réservation.
     *
     * @param idReservation Id de la réservation
     * @param timestamp     Date de la fermeture
     * @param kmRetour      Longueur du trajet à la cloturation
     */
    void cloturer(int idReservation, Timestamp timestamp, int kmRetour) throws DAOException;

    /**
     * Supprime une réservation par son id.
     *
     * @param id Id d'une réservation
     */
    void supprimer(int id) throws DAOException;

    /**
     *
     * @return
     * @throws DAOException
     */
    public List<Integer> vehiculesEnDeplacement() throws DAOException;
}
