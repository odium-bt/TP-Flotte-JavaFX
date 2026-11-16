package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Salarie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseInitializer {
    private final Logger log = LogManager.getLogger(String.valueOf(DatabaseInitializer.class));

    private final SalariesService salariesService;

    public DatabaseInitializer(SalariesService salariesService) {
        this.salariesService = salariesService;
    }

    public void initialize() {
        Salarie[] salaries = {
                new Salarie("111111", "Valérie", "Dupond"),
                new Salarie("123533", "Clark", "Higgins"),
                new Salarie("253644", "Maurice", "Pantou"),
                new Salarie("867452", "Yvette", "Parmentier")
        };

        for (Salarie salarie : salaries) {
            if (salariesService.rechercheParMatricule(salarie.getMatricule()) == null) {
                try {
                    salariesService.ajouterSalarie(salarie);
                } catch (DAOException e) {
                    log.error("Erreur : Impossible d'initialiser le salarié {}.", salarie);
                }
            }
        }
    }
}