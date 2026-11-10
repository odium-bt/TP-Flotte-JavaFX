package fr.greta.tpflottejavafx.controllers;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.exceptions.DomainException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Vehicule;
import fr.greta.tpflottejavafx.service.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.greta.tpflottejavafx.modeles.Salarie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.lang.Thread.sleep;

/// Contrôleur gérant les réservations
public class ReservationController {
    private final Logger log = LogManager.getLogger(String.valueOf(ReservationController.class));

    private final ReservationService reservationService = DataFactory.getInstance().getReservationService();
    private final SalariesService salarieService = DataFactory.getInstance().getSalariesService();
    private final VehiculeService vehiculeService = DataFactory.getInstance().getVehiculeService();

    /**
     * Création de la scène.
     *
     * @param w Fenêtre
     * @throws IOException Jette une exception si le chargement de la vue échoue
     */
    public void ReservationScene(Window w) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AccueilController.class.getResource("/fr/greta/tpflottejavafx/views/reservation-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) w;
        stage.setTitle("Gestion des réservations");
        stage.setScene(scene);
        stage.show();
    }

    // Salariés
    @FXML
    TextField id;
    @FXML
    private TextField matricule;
    @FXML
    private TextField id_vehicule;
    @FXML
    private DatePicker date_depart;
    @FXML
    private DatePicker date_retour;
    @FXML
    private TextField km;
    @FXML
    private TextFlow message;
    @FXML
    private TableView<Reservation> list;
    @FXML
    private TableColumn<Reservation, Integer> idColumn;
    @FXML
    private TableColumn<Reservation, String> matriculeColumn;
    @FXML
    private TableColumn<Reservation, Integer> idVehiculeColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> dateDepartColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> dateRetourColumn;
    @FXML
    private TableColumn<Reservation, Integer> kmColumn;
    @FXML
    private ListView<Reservation> list2;
    @FXML
    private Button menuPrincipal;

    // Source de données unique, partagée par la table et la ListView
    private final ObservableList<Reservation> reservationsList = FXCollections.observableArrayList(
            DataFactory.getInstance().afficherListReservations()
    );

    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    @FXML
    private void initialize() {
        log("Gestion des réservations");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        StringConverter<LocalDate> converter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : date.format(formatter);
            }

            @Override
            public LocalDate fromString(String string) {
                return string == null || string.isBlank()
                        ? null
                        : LocalDate.parse(string, formatter);
            }
        };

        date_depart.setConverter(converter);
        date_retour.setConverter(converter);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        matriculeColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        idVehiculeColumn.setCellValueFactory(new PropertyValueFactory<>("id_vehicule"));
        dateDepartColumn.setCellValueFactory(new PropertyValueFactory<>("date_depart"));
        dateRetourColumn.setCellValueFactory(new PropertyValueFactory<>("date_retour"));
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("kmRetour"));
        // Formatage de l'affichage de la date de mise en circulation
        dateDepartColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(FORMAT_DATE));
            }
        });
        kmColumn.setCellValueFactory(new PropertyValueFactory<>("kilometrage"));

        // à la place de list.setItems(listReservation) :
        FilteredList<Reservation> reservationsFiltres = new FilteredList<>(reservationsList, v -> true);
        SortedList<Reservation> reservationTries = new SortedList<>(reservationsFiltres);
        reservationTries.comparatorProperty().bind(list.comparatorProperty());  // garde le tri par clic sur les colonnes
        list.setItems(reservationTries);

        loadTableAsync();

        // Remplis ou vide les champs selon la ligne sélectionnée
        list.getSelectionModel().selectedItemProperty().addListener((obs, oldP, newP) -> {
            id.setText(newP == null ? "" : String.valueOf(newP.getIdReservation()));
            matricule.setText(newP == null ? "" : newP.getSalarie().getMatricule());
            id_vehicule.setText(newP == null ? "" : String.valueOf(newP.getVehicule().getId()));
            date_depart.setValue(newP == null ? null : newP.getDateDepart().toLocalDateTime().toLocalDate());
            date_retour.setValue(newP == null || newP.getDateRetour() == null ? null : newP.getDateRetour().toLocalDateTime().toLocalDate());
            km.setText(newP == null ? "" : String.valueOf(newP.getKilometrage()));
        });
    }

    private void loadTableAsync() {
        // Lancement d'un Task : enregistrement en base hors du thread JavaFX
        Task<List<Reservation>> task = new Task<>() {
            @Override
            protected List<Reservation> call() throws Exception {
                sleep(500);
                return DataFactory.getInstance().afficherListReservations();
            }
        };

        // Succès : on revient dans le thread JavaFX, on met à jour l'interface
        task.setOnSucceeded(e -> {
            reservationsList.setAll(task.getValue());
            list.setPlaceholder(new Label("Aucune réservation"));
        });

        // Échec : message d'erreur (immatriculation déjà existante, base inaccessible...)
        task.setOnFailed(e -> {
            log.trace(task.getException());
            list.setPlaceholder(new Label("Erreur de chargement : "
                    + task.getException().getMessage()));
        });

        reservationsList.clear();                          // vide la table
        list.setPlaceholder(new ProgressIndicator());   // affiche l'indicateur de chargement

        Thread thread = new Thread(task, "load-flotte");
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void onAdd() {
        String m = matricule.getText().trim();
        String idV = id_vehicule.getText().trim();
        LocalDate dateD = date_depart.getValue();
        String km = this.km.getText().trim();

        int idVehicule;
        LocalDate dateDepart;
        int kilometrage = Integer.parseInt(km);

        if (m.isEmpty() || idV.isEmpty() || dateD == null) {
            log("Erreur : Le matricule, l'id véhicule et la date de départ sont obligatoires" + "\n");
            return;
        } else {
            idVehicule = Integer.parseInt(idV);
        }

        try {
            Salarie s = salarieService.rechercheParMatricule(m);
            if (s == null) {
                log("Erreur : Aucun salarié ne possède ce matricule." + "\n");
                return;
            }
            Vehicule v = vehiculeService.rechercheParImmatriculation(idV);
            if (v == null) {
                log("Erreur : Aucun véhicule ne possède cet id." + "\n");
                return;
            }

            int id = reservationService.creerReservation(m, idVehicule, dateD);
            Reservation r = new Reservation(id, s, v, Timestamp.valueOf(dateD.atStartOfDay()), kilometrage);

            reservationsList.add(r);
            list.getSelectionModel().select(r);
            list.scrollTo(r);
            log("Ajout " + s + "\n");
        } catch (DAOException e) {
            log("Erreur : Impossible d'enregistrer la réservation. Veuillez ré-essayer plus tard" + "\n");
        }
    }

    @FXML
    private void onUpdate() {
        Reservation r = selected();
        if (r == null) return;

        int id = r.getIdReservation();
        String m = matricule.getText().trim();
        String idV = id_vehicule.getText().trim();
        LocalDate dateD = date_depart.getValue();
        LocalDate dateR = date_retour.getValue();
        String kmR = this.km.getText().trim();

        int idVehicule;
        int kmRetour;

        if (m.isEmpty() || idV.isEmpty() || dateD == null || kmR.isEmpty()) {
            log("Erreur : Le matricule, l'id véhicule, la date de départ et le kilométrage sont obligatoires" + "\n");
            return;
        } else {
            kmRetour = Integer.parseInt(kmR);
            idVehicule = Integer.parseInt(idV);
        }

        try {
            reservationService.modifierReservation(id, m, idVehicule, dateD);

            // Enregistrement du retour si entré
            if (dateR != null) {
                reservationService.enregistrerRetour(id, dateR, kmRetour);
                r.setDateRetour(Timestamp.valueOf(dateR.atStartOfDay()));
                r.setKilometrage(kmRetour);
            }

            Salarie s = salarieService.rechercheParMatricule(m);
            Vehicule v = vehiculeService.rechercheParId(idVehicule);
            r.setSalarie(s);
            r.setVehicule(v);
            r.setDateDepart(Timestamp.valueOf(dateD.atStartOfDay()));

            list.refresh();
            list2.refresh();

            log("Modification effectuée : " + r + "\n");
        } catch (DAOException e) {
            log("Erreur : Impossible de modifier le salarié. Veuillez réessayer plus tard" + "\n");
        }
    }

    @FXML
    private void onDelete() {
        Reservation r = selected();
        if (r == null) return;
        try {
            reservationService.supprimerReservation(r.getIdReservation());
            reservationsList.remove(r);
            log("Suppression " + r + "\n");
        } catch (DAOException | DomainException e) {
            log(e.getMessage() + "\n");
        }
    }

    /// Bouton retour à l'accueil
    @FXML
    private void onMenuClick() {
        try {
            new AccueilController().AccueilScene(menuPrincipal.getScene().getWindow());
        } catch (IOException e) {
            log("Erreur : Impossible d'ouvrir l'accueil.");
            log.error(e.getMessage());
            log.trace(e);
        }
    }

    /**
     * Personne sélectionnée, ou null avec un message si aucune.
     */
    private Reservation selected() {
        Reservation r = list.getSelectionModel().getSelectedItem();
        if (r == null) log("Erreur : sélectionnez d'abord une réservation");
        return r;
    }

    private void log(String text) {
        var messages = message.getChildren();
        messages.add(new Text((messages.isEmpty() ? "" : "\n") + text));
    }
}

/*


    public void departReservation() {

        String matricule = vue.saisieTexte("Matricule du salarié : ");
        String immatricule = vue.saisieTexte("Immatriculation : ").toUpperCase();

        try {
            reservationService.enregistrerDepart(immatricule, matricule);
            vue.afficherSucces("La réservation a été validée avec succès");
        } catch (RegleGestionException e) {
            vue.afficherRefus(e.getMessage());
        }
    }

    public void retourReservation() {

        String immatriculation = vue.saisieTexte("Immatriculation : ").toUpperCase();
        int kilometrage = vue.saisieEntier("Kilométrage au compteur : ");

        try {
            reservationService.enregistrerRetour(immatriculation, kilometrage);
            vue.afficherSucces("Le retour a bien été enregistré.");
        } catch (RegleGestionException e) {
            vue.afficherRefus(e.getMessage());
        }
    }

    public void vehiculeEnDeplacement() {

        vue.afficherVehicules(reservationService.vehiculeEnDeplacement());
    }

    }*/
