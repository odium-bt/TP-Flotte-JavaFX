package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.util.Email;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Prépare l'envoie d'un pdf par courriel.
 * <p>
 * L'envoi du pdf est exécuté en arrière-plan sur l'executor fourni à la construction
 * depuis une tâche du pool de 2 threads, créé et arrêté par le main : elle met en place une
 * chaîne PDF puis l'envoi par mail dans l'ordre voulu (l'envoi du mail démarre après la fin
 * de la création du pdf. Elle renvoie un {@link Future} : l'appelant peut attendre la
 * fin avec {@code get()} (facultatif) et récupère alors l'éventuelle erreur sous la forme
 * d'une {@link java.util.concurrent.ExecutionException}.
 * <p>
 */
public class MailService {
    private static final Logger log = LogManager.getLogger(MailService.class);
    private final ExecutorService executor;

    /**
     * Créé le service.
     *
     * @param executor      executor à un seul thread qui envoi le mail
     * */
    public MailService(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Envoi un mail avec le pdf de l'état de la flotte en PJ.
     *
     * <p>
     * L'envoi s'exécute en arrière-plan.
     * @param fichier Path du fichier à joindre au mail
     * @return un {@link Future} : l'envoi de l'email
     */
    public Future<Void> envoyerPdfParMail(Path fichier) {
        return executor.submit(() -> {
            log.info("Début de l'envoi par mail du fichier pdf");
            new Email().sendEmail(fichier, "mail.json");
            log.info("Fichier {} envoyé par courriel", fichier);
            return null;
        });
    }
}
