package fr.greta.tpflottejavafx.service;

import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.dao.SalarieDAO;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.util.ChiffrementAES;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Service d'enregistrement des données des salariés et des réservations chiffrées avec AES.
 */
public class ExportService {
    private static final Logger log = LogManager.getLogger(ExportService.class);
    private final ExecutorService executor;
    private final ReservationDAO reservationDAO;
    private final SalarieDAO salarieDAO;
    private static final String SALARIES_ENC = "salaries.enc";
    private static final String RESERVATIONS_ENC = "reservations.enc";
    private static final String AES_KEY = "aes.key";

    public ExportService(ExecutorService executor, SalarieDAO salarieDAO, ReservationDAO reservationDAO) {
        this.executor = executor;
        this.salarieDAO = salarieDAO;
        this.reservationDAO = reservationDAO;
    }

    public enum TypeExport {
        SALARIES("salariés", SALARIES_ENC),
        RESERVATIONS("reservations", RESERVATIONS_ENC);

        private final String libelle;
        private final String fichier;

        TypeExport(String libelle, String fichier) {
            this.libelle = libelle;
            this.fichier = fichier;
        }

        public String getLibelle() {
            return libelle;
        }
        public String getFichier() {
            return fichier;
        }
    }

    /**
     * Exporte les salariés ou les réservations dans des fichiers chiffrés avec AES.
     * La clé de chiffrement est créée au premier lancement si elle n'existe pas.
     *
     * <p>
     * L'export s'exécute en arrière-plan. Les erreurs sont transmises par le {@link Future}
     * (via {@code get()}) :
     * <ul>
     *   <li>{@link IOException} si un fichier ne peut pas être lu ou écrit ;</li>
     *   <li>{@link GeneralSecurityException} si le chiffrement échoue ;</li>
     * </ul>
     * @param type le type de données à chiffrer
     * @return un {@link Future} qui se termine quand le fichier choisi est écrit
     */
    public Future<Void> chiffrerEnregistrerSalariesReservations(TypeExport type) {
        return executor.submit(() -> {
            String contenu = switch (type) {
                    case SALARIES -> construireBackupSalaries();
                    case RESERVATIONS -> construireBackupReservations();
                };

            //Chiffrement des données avec AES
            SecretKey cle = aesKey();
            ChiffrementAES.chiffrer(contenu, cle, Path.of(type.getFichier()));

            log.info("Export chiffré des {} terminé", type.getLibelle());
            return null;
        });
    }

    /**
     * Charge la clé AES du fichier, ou la génère et la sauvegarde si le fichier n'existe pas
     * encore (premier lancement).
     *
     * @return la clé AES à utiliser pour chiffrer et déchiffrer les exports
     * @throws IOException              si le fichier de clé ne peut pas être lu ou écrit.
     * @throws GeneralSecurityException si la clé est invalide ou ne peut pas être générée.
     * @implNote Méthode {@code synchronized} : deux tâches lancées en même temps au premier
     * lancement pourraient sinon générer chacune une clé, la seconde écrasant la
     * première et rendant illisibles les fichiers chiffrés avec elle.
     */
    private synchronized SecretKey aesKey() throws IOException, GeneralSecurityException {
        Path fichierCle = Path.of(AES_KEY);
        SecretKey cle;
        if (Files.exists(fichierCle)) {
            cle = ChiffrementAES.chargerCle(fichierCle);
            // lancements suivants
        } else {
            cle = ChiffrementAES.genererCle();
            // premier lancement
            ChiffrementAES.sauvegarderCle(cle, fichierCle);
        }
        return cle;
    }

    /**
     * Construit la liste des salariés
     * dans une chaîne de caractères
     *
     * @return la liste des salariés
     */
    private String construireBackupSalaries() {
        StringBuilder resSalaries = new StringBuilder("Liste des salariés : \n");
        for (Salarie s : salarieDAO.lister()) {
            resSalaries.append(s.getMatricule()).append(";")
                    .append(s.getNom()).append(";")
                    .append(s.getPrenom()).append("\n");
        }
        resSalaries.append("--- Fin de liste --- ").append("\n");
        return resSalaries.toString();
    }

    /**
     * Construit la liste des réservations
     * dans une chaîne de caractères
     *
     * @return la liste des réservations
     */
    private String construireBackupReservations() {
        StringBuilder resResa = new StringBuilder("Liste des réservations : \n");
        for (Reservation r : reservationDAO.lister()) {
            resResa.append(r.getIdReservation()).append(";")
                    .append(r.getSalarie()).append(";")
                    .append(r.getVehicule()).append(";")
                    .append(r.getDateDepart()).append(";")
                    .append(r.getDateRetour()).append(";")
                    .append(r.getKilometrage()).append("\n");
        }
        resResa.append("--- Fin de liste --- ").append("\n");
        return resResa.toString();
    }

    /**
     * Déchiffre un fichier chiffré avec AES
     *
     * <p>
     * Les erreurs sont transmises par le {@link Future} (via {@code get()}) :
     * <ul>
     *   <li>{@link GeneralSecurityException} si le déchiffrement échoue ;</li>
     *   <li>{@link IOException} un fichier ne peut pas être lu.</li>
     * </ul>
     *
     * @param type le type du fichier à déchiffrer
     * @return un {@link Future} : les données du fichier déchiffré
     */
    public Future<String> dechifrerEtAfficherFichierExport(TypeExport type) {
        return executor.submit(() -> {
            String fichierDechiffre;
            //Chemin des fichiers
            Path pathBackupFichier = Path.of(type.getFichier());
            SecretKey cle = aesKey();
            //Lecture des fichiers chiffrés
            fichierDechiffre = ChiffrementAES.dechiffrer(cle, pathBackupFichier);
            return fichierDechiffre;
        });
    }
}
