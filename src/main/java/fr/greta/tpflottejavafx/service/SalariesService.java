package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.dao.SalarieDAO;
import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.DomainException;
import fr.greta.tpflottejavafx.modeles.Salarie;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalariesService extends Service {
    private final Logger log = LogManager.getLogger(String.valueOf(SalariesService.class));

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final SalarieDAO salarieDAO;
    private final ReservationDAO reservationDAO;

    public SalariesService(SalarieDAO salarieDAO, ReservationDAO reservationDAO) {
        this.salarieDAO = salarieDAO;
        this.reservationDAO = reservationDAO;
    }

    /**
     * Ajout d'un salarié.
     *
     * @param salarie Le salarié
     */
    public void ajouterSalarie(Salarie salarie) throws DAOException {
        executor.submit(() -> {
            log.info("Création d'un salarié");

            try {
                salarieDAO.creer(salarie);

                log.info("Salarié créé");
            } catch (DAOException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getStackTrace());
                throw e;
            }
        });
    }

    /**
     * Récupère chaque salarié sous forme de string.
     *
     * @return Une {@code List<String>} contenant chaque salarié sous forme de string
     */
    public List<Salarie> afficherSalaries() {
        log.info("Affichage des salariés");

        List<Salarie> res = new ArrayList<>();

        try {
            salarieDAO.listerSalaries();
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
            throw e;
        }

        return res;
    }

    /**
     * Récupère un salarié par son matricule.
     *
     * @param matricule Le matricule du salarié
     * @return {@code Salarie}
     */
    public Salarie rechercheParMatricule(String matricule) {
        log.info("Affichage du salarié {}", matricule);

        try {
            return salarieDAO.trouverParMatricule(matricule);
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            log.trace(e.getStackTrace());
        }

        return null;
    }

    /**
     * Modifie les données d'un salarié.
     *
     * @param salarie Le salarié modifié.
     */
    public void modifierSalarie(Salarie salarie) throws DAOException {
        executor.submit(() -> {
            log.info("Modification d'un salarié");

            try {
                salarieDAO.modifier(salarie);

                log.info("Salarié mis à jour");
            } catch (DAOException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getStackTrace());
                throw e;
            }
        });
    }

    public void modifierSalarie(Salarie salarie, String nom, String prenom) throws DAOException {
        executor.submit(() -> {
            log.info("Modification d'un salarié");
            Salarie nouveauSalarie = new Salarie(salarie.getMatricule(), prenom, nom);
            try {
                salarieDAO.modifier(nouveauSalarie);

                log.info("Salarié mis à jour");
            } catch (DAOException e) {
                log.error(e.getMessage(), e);
                log.trace(e.getStackTrace());
                throw e;
            }
        });
    }

    /**
     * Supprime le compte du salarié.
     *
     * @param matricule Son matricule
     */
    public void supprimerParMatricule(String matricule) throws DomainException {
        executor.submit(() -> {
            log.info("Suppression du salarié {}", matricule);

            if (reservationDAO.estEnDeplacementParMatricule(Integer.parseInt(matricule))) {
                salarieDAO.supprimer(matricule);
            } else {
                throw new DomainException("Erreur : Supprimer un salarié en déplacement n'est pas authorisé");
            }
        });
    }
}