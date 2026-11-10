package fr.greta.tpflottejavafx.dao;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private final String table = "reservation";
    private final String id = "id";
    private final String matricule = "matricule";
    private final String idVehicule = "id_vehicule";
    private final String dateDepart = "date_depart";
    private final String dateRetour = "date_retour";
    private final String kmRetour = "km_retour";

    /**
     * {@inheritDoc}
     */
    @Override
    public int creer(String matricule, int idVehicule, LocalDate dateDepart) throws DAOException {
        String query = "INSERT INTO " + table + " ("
                + this.matricule + ", "
                + this.idVehicule + ", "
                + this.dateDepart + ") "
                + "VALUES( ?, ?, ?)";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, matricule);
            ps.setInt(2, idVehicule);
            ps.setTimestamp(3, Timestamp.valueOf(dateDepart.atStartOfDay()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la création de la réservation : " + e.getMessage());
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation trouverParId(int id) throws DAOException {
        String query = "select * from " + table + " where " + this.id + "=?";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapperReservation(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche de la réservation");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> trouverParMatricule(String matricule) throws DAOException {
        String query = "SELECT * FROM " + table
                + " WHERE " + this.matricule + " = ?";

        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {

            ps.setString(1, matricule);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    reservations.add(mapperReservation(rs));
                }

            }

            return reservations;

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche de la réservation : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> lister() throws DAOException {
        List<Reservation> liste = new ArrayList<>();

        // Récupère uniquement les matricules qui ont des réservations, sans doublons
        String query = "SELECT DISTINCT " + this.matricule + " FROM " + table;

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(mapperReservation(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche des réservations : " + e.getMessage());
        }

        return liste;
    }

    /**
     * Transforme une ligne SQL en objet Reservation.
     */
    private Reservation mapperReservation(ResultSet rs) throws SQLException {
        int idReservation = rs.getInt(id);
        String matriculeSalarie = rs.getString(matricule);
        int vehiculeId = rs.getInt(idVehicule);
        Salarie salarie = DAOFactory.getInstance().getSalarieDAO().trouverParMatricule(matriculeSalarie);
        Vehicule vehicule = DAOFactory.getInstance().getVehiculeDAO().trouverParId(vehiculeId);
        Timestamp depart = rs.getTimestamp(dateDepart);
        Timestamp retour = rs.getTimestamp(dateRetour);
        int kilometrage = rs.getInt(kmRetour);

        return new Reservation(idReservation, salarie, vehicule, depart, retour, kilometrage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estEnDeplacementParId(int idReservation) throws DAOException {
        String query = "SELECT COUNT(*) FROM " + table
                + " WHERE " + this.id + " = ?"
                + " AND " + this.dateDepart + " <= NOW()"
                + " AND " + this.dateRetour + " IS NULL";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, idReservation);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la vérification de l'état de la réservation " + idReservation + " : " + e.getMessage());
        }
    }

    @Override
    public List<Integer> vehiculesEnDeplacement() throws DAOException {
        List<Integer> list = new ArrayList<>();

        String query = "SELECT id_vehicule FROM " + table
                + " WHERE " + dateRetour + " IS NULL";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    list.add(rs.getInt("id_vehicule"));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la vérification de l'état de la réservation : " + e.getMessage());
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estEnDeplacementParIdVehicule(int idVehicule) throws DAOException {
        String query = "SELECT COUNT(*) FROM " + table
                + " WHERE " + this.idVehicule + " = ?"
                + " AND " + this.dateDepart + " <= NOW()"
                + " AND " + this.dateRetour + " IS NULL";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, idVehicule);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la vérification du déplacement du véhicule" + idVehicule + " : " + e.getMessage());

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean estEnDeplacementParMatricule(int matricule) throws DAOException {
        String query = "SELECT COUNT(*) FROM " + table
                + " WHERE " + this.matricule + " = ?"
                + " AND " + this.dateDepart + " <= NOW()"
                + " AND " + this.dateRetour + " IS NULL";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, matricule);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la vérification du déplacement du salarié " + matricule + " : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifier(int idReservation, String matricule, int idVehicule, Timestamp dateDepart) throws DAOException {
        String query = "UPDATE " + table + " SET "
                + this.matricule + " = ?, "
                + this.idVehicule + " = ?, "
                + this.dateDepart + " = ? "
                + "WHERE " + id + " = ?";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setString(1, matricule);
            ps.setInt(2, idVehicule);
            ps.setTimestamp(3, dateDepart);
            ps.setInt(4, idReservation);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la modification de la réservation : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cloturer(int idReservation, Timestamp dateRetour, int kmRetour) throws DAOException {
        String query = "UPDATE " + table + " SET "
                + this.dateRetour + " = ?, "
                + this.kmRetour + " = ? "
                + "WHERE " + id + " = ? "
                + "AND " + this.dateRetour + " IS NULL";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setTimestamp(1, dateRetour);
            ps.setInt(2, kmRetour);
            ps.setInt(3, idReservation);

            int lignesModifiees = ps.executeUpdate();

            if (lignesModifiees == 0) {
                throw new DAOException("Réservation introuvable ou déjà clôturée");
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la clôture de la réservation : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimer(int id) throws DAOException {
        String query = "DELETE FROM " + table
                + " WHERE " + this.id + " = ?";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }
}
