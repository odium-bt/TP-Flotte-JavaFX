package fr.greta.tpflottejavafx.modeles;

import java.sql.Timestamp;

/**
 * Classe représentant une réservation.
 */
public class Reservation {
    private final int idReservation;
    private Salarie salarie;
    private Vehicule vehicule;
    private Timestamp dateDepart;
    private Timestamp dateRetour;
    private int kilometrage;

    /// Constructeur avec retour
    public Reservation(int idReservation, Salarie salarie, Vehicule vehicule, Timestamp dateDepart, Timestamp dateRetour, int kilometrage) {
        this.idReservation = idReservation;
        this.salarie = salarie;
        this.vehicule = vehicule;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.kilometrage = kilometrage;
    }

    /// Constructeur
    public Reservation(int idReservation, Salarie salarie, Vehicule vehicule, Timestamp dateDepart, int kilometrage) {
        this.idReservation = idReservation;
        this.salarie = salarie;
        this.vehicule = vehicule;
        this.dateDepart = dateDepart;
        this.kilometrage = kilometrage;
    }

    public int getIdReservation() {
        return idReservation;
    }


    public Salarie getSalarie() {
        return salarie;
    }

    public void setSalarie(Salarie salarie) {
        this.salarie = salarie;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Timestamp getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Timestamp dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Timestamp getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Timestamp dateRetour) {
        this.dateRetour = dateRetour;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", salarie=" + salarie +
                ", vehicule=" + vehicule +
                ", dateDepart=" + dateDepart +
                ", dateRetour=" + dateRetour +
                ", kilometrage=" + kilometrage +
                '}';
    }
}
