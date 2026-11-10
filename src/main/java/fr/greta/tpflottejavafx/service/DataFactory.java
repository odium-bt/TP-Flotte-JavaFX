package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.modeles.Flotte;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataFactory {
    private static DataFactory instance;

    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;
    private final SalarieDAO salarieDAO;
    private final Flotte flotte;
    private final VehiculeService vehiculeService;
    private final ReservationService reservationService;
    private final SalariesService salariesService;
    private final EtatService etatService;


    public DataFactory() {
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.reservationDAO = new ReservationDAOImpl();
        this.salarieDAO = new SalarieDAOImpl();
        this.flotte = new Flotte();
        this.vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        this.reservationService = new ReservationService(vehiculeDAO, reservationDAO, salarieDAO);
        this.salariesService = new SalariesService(salarieDAO, reservationDAO);
        this.etatService = new EtatService(vehiculeDAO, reservationDAO);
    }

    //Getteurs des services
    public VehiculeService getVehiculeService() {
        return vehiculeService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public SalariesService getSalariesService() {
        return salariesService;
    }

    public EtatService getEtatService() {
        return etatService;
    }


    //Instance de DataFactory
    public static DataFactory getInstance() {
        if (instance == null) {
            instance = new DataFactory();
        }
        return instance;
    }


    //Récupération des listes pour toute l'appli
    public List<Vehicule> afficherListVehicules() {
        return vehiculeService.afficherFlotte();
    }



    public List<Reservation> afficherListReservations() {
        return reservationService.afficherReservations();
    }

    public List<Salarie> afficherListSalaries() {
        return salariesService.afficherSalaries();
    }
}
