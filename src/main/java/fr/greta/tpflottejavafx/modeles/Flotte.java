package fr.greta.tpflottejavafx.modeles;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la flotte de véhicules.
 */
public class Flotte {

    private List<Vehicule> listeVehicules;

    public Flotte() {
        this.listeVehicules = new ArrayList<>();
    }

    public List<Vehicule> getListeVehicules() {
        return listeVehicules;
    }

    public void setListeVehicules(List<Vehicule> listeVehicules) {
        this.listeVehicules = listeVehicules;
    }

}
