package fr.greta.tpflottejavafx.controllers;


public class ApplicationController {

    private final FlotteController flotteController;
    private final ReservationController reservationController;
    private final EtatController etatController;

    // Constructeur qui reçoit la vue et les trois contrôleurs
    public ApplicationController(FlotteController flotteController, ReservationController reservationController, EtatController etatController) {
        this.flotteController = flotteController;
        this.reservationController = reservationController;
        this.etatController = etatController;
    }
}
