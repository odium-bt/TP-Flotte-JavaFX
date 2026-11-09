package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.dao.VehiculeDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;

public class EtatService implements Runnable {

    private static final Logger log = LogManager.getLogger(EtatService.class);
    private static final String FICHIER_PDF = "etat_flotte.pdf";
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String[] ENTETES = {
            "Immatriculation", "Marque", "Modèle", "Mise en circulation", "Kilométrage", "Disponibilité"
    };

    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;

    public EtatService(VehiculeDAO vehiculeDAO, ReservationDAO reservationDAO) {
        this.vehiculeDAO = vehiculeDAO;
        this.reservationDAO = reservationDAO;
    }

    @Override
    public void run() {
    }





}

