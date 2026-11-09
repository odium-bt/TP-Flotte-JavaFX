package fr.greta.tpflottejavafx.modeles;

/**
 * Classe représentant un salarié.
 */
public class Salarie {

    private String matricule;
    private String nom;
    private String prenom;

    public Salarie(String matricule, String prenom, String nom) {

        validation(matricule);
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
    }

    private void validation(String matricule) {
        if (!matricule.matches("\\d{6}")) {
            throw new IllegalArgumentException("Le matricule est invalide!");
        }
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Salarie{" +
                "matricule='" + matricule + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }
}
