package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.dao.SalarieDAO;
import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.DomainException;
import fr.greta.tpflottejavafx.exceptions.RegleGestionException;
import fr.greta.tpflottejavafx.modeles.Salarie;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

public class SalariesService extends Service {
    private final Logger log = LogManager.getLogger(String.valueOf(SalariesService.class));

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
                throw e;
            }
        });
    }

    /**
     * Récupère chaque salarié.
     *
     * @return Une {@code List<Salarie>} contenant chaque salarié
     */
    public List<Salarie> afficherSalaries() {
        log.info("Affichage des salariés");

        List<Salarie> res = new ArrayList<>();

        try {
            salarieDAO.listerSalaries();
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
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
        }

        return null;
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
                throw new RegleGestionException("Erreur : Supprimer un salarié en déplacement n'est pas authorisé");
            }
        });
    }
}