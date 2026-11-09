package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.vue.MenuConsole;

public class ApplicationController {

    //
    private final MenuConsole vue;
    private final FlotteController flotteController;
    private final ReservationController reservationController;
    private final EtatController etatController;

    // Constructeur qui reçoit la vue et les trois contrôleurs
    public ApplicationController(MenuConsole vue, FlotteController flotteController, ReservationController reservationController, EtatController etatController) {
        this.vue = vue;
        this.flotteController = flotteController;
        this.reservationController = reservationController;
        this.etatController = etatController;
    }
}
