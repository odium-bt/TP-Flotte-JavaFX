package fr.greta.tpflottejavafx.service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import fr.greta.tpflottejavafx.dao.ReservationDAO;
import fr.greta.tpflottejavafx.modeles.Flotte;
import fr.greta.tpflottejavafx.modeles.Vehicule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Service de préparation de l'état de la flotte en PDF.
 * <p>
 * La création du pdf est exécutée en arrière-plan sur l'executor fourni à la construction
 * depuis une tâche du pool de 2 threads, créé et arrêté par le main : elle met en place une
 * chaîne PDF puis l'envoi par mail dans l'ordre voulu (l'envoi du mail démarre après la fin
 * de la création du pdf. Elles renvoient un {@link Future} : l'appelant peut attendre la
 * fin avec {@code get()} (facultatif) et récupère alors l'éventuelle erreur sous la forme
 * d'une {@link java.util.concurrent.ExecutionException}.
 * <p>
 */
public class PdfService {
    private static final Logger log = LogManager.getLogger(PdfService.class);
    private static final String FICHIER_PDF = "etat_flotte.pdf";
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");;
    private final ExecutorService executor;
    private final Flotte flotte;
    private final ReservationDAO reservationDAO;

    // Constructeur
    public PdfService(Flotte flotte, ReservationDAO reservationDAO, ExecutorService executor) {
        this.flotte = flotte;
        this.reservationDAO = reservationDAO;
        this.executor = executor;
    }


    /**
     * Génère un pdf de l'état de la flotte
     * avec submit -> renvoie un Future : son get() attend la fin de la têche et renvoie son résultat
     * Plus de try/catch : si quelque chose échoue, l’exception arrive à l’appelant via get() et log
     *
     * @return un Future : le chemin du fichier pdf généré
     */
    public Future<Path> creerPdfEtatFlotte() {
        return executor.submit(() -> {
            log.info("Début de la génération du fichier pdf de l'état de la flotte");

            // lister() renvoie aussi une liste vide en cas d'erreur SQL : on n'envoie pas un état vide
            if (flotte.getListeVehicules().isEmpty()) {
                throw new IllegalArgumentException("Aucun véhicule trouvé : état non généré");
            }

            ecrireFlotteStatut();
            return Path.of(FICHIER_PDF);
        });
    }



    public void ecrireFlotteStatut() throws IOException {
        // Gestionnaire de l'écriture dans le fichier pdf
        com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(FICHIER_PDF);
        // Création d'un document avec plusieurs pages
        PdfDocument pdfDoc = new PdfDocument(writer);
        // Création du contenu de la page du document
        Document doc = new Document(pdfDoc);

        // Couleurs
        DeviceRgb bleu = new DeviceRgb(35, 60, 89);
        DeviceRgb gris = new DeviceRgb(100, 116, 139);
        DeviceRgb vert = new DeviceRgb(22, 163, 74);
        DeviceRgb rouge = new DeviceRgb(220, 38, 38);

        // Titre principal
        Paragraph titre = new Paragraph("ÉTAT DE LA FLOTTE")
                .setFontSize(22)
                .setFontColor(bleu)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);

        doc.add(titre);

        // Sous-titre
        doc.add(new Paragraph("RAPPORT DE DISPONIBILITÉ DES VÉHICULES")
                .setFontSize(10)
                .setFontColor(gris)
                .setTextAlignment(TextAlignment.CENTER));

        // Date de génération
        doc.add(new Paragraph("Généré le " + LocalDate.now().format(FORMAT_DATE))
                .setFontSize(9)
                .setFontColor(gris)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(25));

        int compteur = 1;

        for (Vehicule vehicule : flotte.getListeVehicules()) {

            // En-tête du véhicule
            doc.add(new Paragraph("Véhicule n°" + compteur++)
                    .setFontSize(15)
                    .setFontColor(bleu)
                    .setMarginBottom(8));

            // Tableau des informations
            Table table = new Table(UnitValue.createPercentArray(new float[]{35, 65})).useAllAvailableWidth();
            ajouterLigne(table, "Immatriculation", vehicule.getImmatriculation());
            ajouterLigne(table, "Marque", vehicule.getMarque());
            ajouterLigne(table, "Modèle", vehicule.getModele());
            ajouterLigne(table, "Mise en circulation", vehicule.getDateMiseEnCirculation().toString());
            ajouterLigne(table, "Kilométrage", vehicule.getKilometrage() + " km");
            doc.add(table);

            // Statut du véhicule
            boolean enDeplacement = reservationDAO.estEnDeplacementParIdVehicule(vehicule.getId());
            Paragraph statut = new Paragraph("Disponibilité : "
                    + (enDeplacement ? "En déplacement" : "Disponible"))
                    .setFontSize(11)
                    .setFontColor(enDeplacement ? rouge : vert)
                    .setMarginTop(10)
                    .setMarginBottom(20);
            doc.add(statut);

            // Séparateur
            doc.add(new Paragraph("────────────────────────────────────────")
                    .setFontColor(gris)
                    .setMarginBottom(15));
        }

        doc.close();
    }

    /**
     * Construit le contenu d'un tableau avec les données de la flotte
     *
     * @param table
     * @param libelle
     * @param valeur
     */
    private void ajouterLigne(Table table, String libelle, String valeur) {
        DeviceRgb grisClair = new DeviceRgb(241, 245, 249);

        Cell celluleLibelle = new Cell()
                .add(new Paragraph(libelle).setFontSize(10))
                .setBackgroundColor(grisClair);

        Cell celluleValeur = new Cell()
                .add(new Paragraph(valeur).setFontSize(10));

        table.addCell(celluleLibelle);
        table.addCell(celluleValeur);
    }

}

