package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.dao.VehiculeDAO;
import fr.greta.tpflottejavafx.exceptions.RegleGestionException;
import fr.greta.tpflottejavafx.modeles.Flotte;
import fr.greta.tpflottejavafx.modeles.Vehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class VehiculeService extends Service {
    private final Logger log = LogManager.getLogger(String.valueOf(VehiculeService.class));

    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;
    private final Flotte flotte;

    public VehiculeService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO, Flotte flotte) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
        this.flotte = flotte;

        this.initialiserFlotte();
    }

    public int ajouterVehicule(String immatriculation, String marque, String modele, LocalDate dateMiseEnCirculation, int kilometrage) throws DAOException {
        log.info("Début de l'ajout d'un véhicule...");


        try {
            if (vehiculeDAO.trouverParImmatriculation(immatriculation) != null) {
                throw new RegleGestionException("Un véhicule avec cette immatriculation existe déjà !");
            }

            int id = vehiculeDAO.creer(immatriculation, marque, modele, dateMiseEnCirculation, kilometrage);

            log.info("Le véhicule immatriculé {} a été ajouté !", immatriculation);

            return id;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Récupère la flotte de véhicule depuis la base de donnée
     * et les stocke dans un objet {@code Flotte} propriété de {@code VehiculeService}.
     */
    public void initialiserFlotte() {
        log.info("Début de la récupération de la flotte depuis la base de données");
        try {
            this.flotte.setListeVehicules(vehiculeDAO.lister());
            log.info("Flotte initialisée avec succès");
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Récupère chaque véhicule de la flotte.
     *
     * @return Retourne une {@code List<String>} contenant chaque véhicule
     */
    public List<Vehicule> afficherFlotte() {
        log.info("Récupération de la flotte...");

        try {
            List<Vehicule> flotte = new ArrayList<>(this.flotte.getListeVehicules());
            if (flotte.isEmpty()) {
                log.info("La flotte est actuellement vide, aucun véhicule à afficher.");
            }
            return flotte;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cherche un véhicule par son id.
     *
     * @param id L'id du véhicule
     * @return {@code Vehicule}
     */
    public Vehicule rechercheParId(int id) {
        log.info("Recherche du véhicule {}", id);
        try {
            Vehicule v = vehiculeDAO.trouverParId(id);
            if (v == null) {
                log.info("Aucun véhicule trouvé pour cet id.");
            } else {
                log.info("Véhicule {} trouvé", id);
            }
            return v;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Cherche un véhicule par son immatriculation.
     *
     * @param immatriculation L'immatriculation du véhicule
     * @return {@code Vehicule}
     */
    public Vehicule rechercheParImmatriculation(String immatriculation) {
        log.info("Recherche du véhicule immatriculé {}", immatriculation);
        try {
            Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);
            if (v == null) {
                log.info("Aucun véhicule trouvé pour l'immatriculation {}'.", immatriculation);
            } else {
                log.info("Véhicule immatriculé {} trouvé", immatriculation);
            }
            return v;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Recherche dans la liste de Véhicule à partir d'un mot
     *
     * @param mot String
     * @return
     */
    public List<Vehicule> rechercherParString(String mot) {
        log.info("Recherche de véhicules avec le mot clé {}", mot);
        try {
            List<Vehicule> listVehiculeRecherche = new ArrayList<>(vehiculeDAO.trouver(mot));
            return listVehiculeRecherche;
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Modifie un véhicule en base
     *
     * @param vehicule Vehicule
     */
    public void modifierVehicule(Vehicule vehicule) {
        log.info("Modification d'un véhicule");

        if (vehiculeDAO.trouverParId(vehicule.getId()) == null) {
            throw new RegleGestionException("Ce véhicule n'existe pas");
        }

        // Si l'immatriculation change, elle ne doit pas appartenir à un autre véhicule
        Vehicule existant = vehiculeDAO.trouverParImmatriculation(vehicule.getImmatriculation());
        if (existant != null && !Objects.equals(existant.getId(), vehicule.getId())) {
            throw new RegleGestionException("Un véhicule avec cette immatriculation existe déjà !");
        }

        vehiculeDAO.modifier(vehicule);
        log.info("Véhicule modifié");
    }


    /**
     * Tente de supprimer un véhicule par son immatriculation.
     *
     * @param immatriculation Immatriculation
     */
    public void retirerVehicule(String immatriculation) {
        executor.submit(() -> {
            log.info("Tentative de suppression du véhicule {}", immatriculation);

            try {
                Vehicule v = vehiculeDAO.trouverParImmatriculation(immatriculation);

                if (v == null) {
                    throw new RegleGestionException("Ce véhicule n'existe pas");
                }

                if (reservationDAO.estEnDeplacementParIdVehicule(v.getId())) {
                    throw new RegleGestionException("Ce véhicule est actuellement en déplacement");
                }

                vehiculeDAO.supprimer(v.getId());

                log.info("Véhicule {} supprimé avec succès", immatriculation);
            } catch (DAOException | RegleGestionException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Tente de supprimer un véhicule par son ID.
     *
     * @param id Id du véhicule
     */
    public void retirerVehiculeParId(int id) {
        executor.submit(() -> {
            log.info("Tentative de suppression du véhicule {}", id);

            try {
                if (vehiculeDAO.trouverParId(id) == null) {
                    throw new RegleGestionException("Ce véhicule n'existe pas");
                }

                if (reservationDAO.estEnDeplacementParIdVehicule(id)) {
                    throw new RegleGestionException("Ce véhicule est actuellement en déplacement");
                }

                vehiculeDAO.supprimer(id);

                log.info("Véhicule {} supprimé avec succès", id);
            } catch (DAOException | RegleGestionException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
