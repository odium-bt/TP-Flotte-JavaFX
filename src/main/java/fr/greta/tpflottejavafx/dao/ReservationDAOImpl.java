package fr.greta.tpflottejavafx.dao;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Reservation;
import fr.greta.tpflottejavafx.modeles.Salarie;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    public void creer(Reservation reservation, Salarie salarie) throws DAOException {
        String query = "INSERT INTO " + table + " ("
                + matricule + ", "
                + idVehicule + ", "
                + dateDepart + ") "
                + "VALUES( ?, ?, ?)";

        try (PreparedStatement ps = DAOFactory.getInstance()
                .getConnection().prepareStatement(query)) {

            ps.setString(1, salarie.getMatricule());
            ps.setInt(2, reservation.getVehicule().getId());
            ps.setTimestamp(3, reservation.getDateDepart());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(
                    "Erreur lors de la création de la réservation : " + e.getMessage()
            );
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
        String query = "SELECT DISTINCT " + matricule + " FROM " + table;

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
                + " WHERE " + id + " = ?"
                + " AND " + dateDepart + " <= NOW()"
                + " AND " + dateRetour + " IS NULL";
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
                + " WHERE " + idVehicule + " = ?"
                + " AND " + dateDepart + " <= NOW()"
                + " AND " + dateRetour + " IS NULL";
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
                + " WHERE " + matricule + " = ?"
                + " AND " + dateDepart + " <= NOW()"
                + " AND " + dateRetour + " IS NULL";
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
    public void modifier(int idReservation, Timestamp dateDepart, Vehicule vehicule) throws DAOException {
        String query = "UPDATE " + table + " SET "
                + idVehicule + " = ?, "
                + dateDepart + " = ? "
                + "WHERE " + id + " = ?";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, vehicule.getId());
            ps.setTimestamp(2, dateDepart);
            ps.setInt(3, idReservation);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la modification de la réservation : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cloturer(int idReservation, Timestamp timestamp, int kmRetour) throws DAOException {
        String query = "UPDATE " + table + " SET "
                + dateRetour + " = ?, "
                + this.kmRetour + " = ? "
                + "WHERE " + id + " = ? "
                + "AND " + dateRetour + " IS NULL";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setTimestamp(1, timestamp);
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
