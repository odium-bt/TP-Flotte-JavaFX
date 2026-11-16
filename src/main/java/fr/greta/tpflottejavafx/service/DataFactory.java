package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.*;
import fr.greta.tpflottejavafx.modeles.Flotte;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.util.List;

/**
 * Classe d'instantiation des DAOs, des services et de la flotte.
 */
public class DataFactory {
    private static DataFactory instance;

    private final VehiculeDAO vehiculeDAO;
    private final ReservationDAO reservationDAO;
    private final SalarieDAO salarieDAO;
    private final Flotte flotte;
    private final VehiculeService vehiculeService;
    private final ReservationService reservationService;
    private final SalariesService salariesService;
    private final PdfService pdfService;
    private final MailService mailService;
    private final ExportService exportService;
    private final ExecutorService executor;


    public DataFactory() {
        this.executor = Executors.newFixedThreadPool(2); // PDF, courriel, export
        this.vehiculeDAO = new VehiculeDAOImpl();
        this.reservationDAO = new ReservationDAOImpl();
        this.salarieDAO = new SalarieDAOImpl();
        this.flotte = new Flotte();
        this.vehiculeService = new VehiculeService(vehiculeDAO, reservationDAO, flotte);
        this.reservationService = new ReservationService(vehiculeDAO, reservationDAO, salarieDAO);
        this.salariesService = new SalariesService(salarieDAO, reservationDAO);
        this.pdfService = new PdfService(flotte, reservationDAO, executor);
        this.mailService = new MailService(executor);
        this.exportService = new ExportService(executor, salarieDAO, reservationDAO);
    }

    // Getters des services
    public VehiculeService getVehiculeService() {
        return vehiculeService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }

    public SalariesService getSalariesService() {
        return salariesService;
    }

    public PdfService getPdfService() {
        return pdfService;
    }

    public MailService getMailService() {
        return mailService;
    }

    public ExportService getExportService() {
        return exportService;
    }

    // Instance de DataFactory
    public static DataFactory getInstance() {
        if (instance == null) {
            instance = new DataFactory();
        }
        return instance;
    }


    //Récupération des listes pour toute l'appli

    /**
     * Récupération de la flotte
     *
     * @return la liste des véhicules de la base
     */
    public List<Vehicule> afficherListVehicules() {
        return vehiculeService.afficherFlotte();
    }

    public List<Reservation> afficherListReservations() {
        return reservationService.afficherReservations();
    }

    public List<Salarie> afficherListSalaries() {
        return salariesService.afficherSalaries();
    }


    //Arrête les threads
    public void arreter() {
        executor.shutdown();                                     // n'accepte plus de nouvelles tâches
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();                          // force l'arrêt au bout de 10 s
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Arrête les threads si DataFactory a déjà été créée.
     */
    public static void arreterSiCree() {
        if (instance != null) {
            instance.arreter();
        }
    }
}
