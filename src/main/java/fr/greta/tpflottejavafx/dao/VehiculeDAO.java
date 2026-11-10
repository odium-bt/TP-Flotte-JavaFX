package fr.greta.tpflottejavafx.dao;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.time.LocalDate;
import java.util.List;

public interface VehiculeDAO {

    /**
     * Création d'un nouveau véhicule dans la base de données.
     *
     * @param vehiculeImmat  Immatriculation
     * @param vehiculeMarque Marque
     * @param vehiculeModele Modèle
     * @param vehiculeDate   Date de mise en circulation
     * @param vehiculeKilo   Kilométrage
     * @return L'id du véhicule
     */
    int creer(String vehiculeImmat, String vehiculeMarque, String vehiculeModele, LocalDate vehiculeDate, int vehiculeKilo) throws DAOException; // renseigne l'id généré

    /**
     * Cherche un véhicule dans la base de données selon son id.
     *
     * @param id L'id du véhicule
     * @return Un objet {@code Vehicule}, ou {@code null} si le véhicule n'a pas été trouvé
     */
    Vehicule trouverParId(int id) throws DAOException;

    /**
     * Cherche un véhicule dans la base de données selon son immatriculation.
     *
     * @param immat L'immatriculation du véhicule
     * @return Un objet {@code Vehicule}, ou {@code null} si le véhicule n'a pas été trouvé
     */
    Vehicule trouverParImmatriculation(String immat) throws DAOException;

    /**
     * Cherche un véhicule dans la base de données selon un mot clé.
     *
     * @param mot String
     * @return Un objet {@code Vehicule}, ou {@code null} aucun véhicule n'a pas été trouvé
     */
    public List<Vehicule> trouver(String mot) throws DAOException;

    /**
     * Récupère tous les véhicules stockés en base.
     *
     * @return Un objet {@code List<Vehicule>} contenant chaque véhicule stocké en base
     */
    List<Vehicule> lister() throws DAOException;

    /**
     * Modifie le véhicule dans la base de donnée possédant le même id que l'objet fourni.
     *
     * @param vehicule L'objet véhicule modifié
     */
    void modifier(Vehicule vehicule) throws DAOException; // ex. : mise à jour du kilométrage

    /**
     * Supprime le véhicule selon l'id fourni.
     *
     * @param id Id du véhicule
     */
    void supprimer(int id) throws DAOException;
}