package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.SalarieDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExportService extends Service {
    private final Logger log = LogManager.getLogger(String.valueOf(ExportService.class));

    private final SalarieDAO salarieDAO;

    public ExportService(SalarieDAO salarieDAO) {
        this.salarieDAO = salarieDAO;
    }

    public void exportSalaries() {

    }

    /**
     * Ferme le thread du service.
     */
    public void fermer() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
