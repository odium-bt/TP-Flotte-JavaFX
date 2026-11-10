package fr.greta.tpflottejavafx.dao;

import fr.greta.tpflottejavafx.exceptions.DAOException;
import fr.greta.tpflottejavafx.modeles.Vehicule;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour les véhicules
 */
public class VehiculeDAOImpl implements VehiculeDAO {
    private final String table = "vehicule";
    private final String id = "id";
    private final String immatriculation = "immatriculation";
    private final String marque = "marque";
    private final String modele = "modele";
    private final String date_mise_circulation = "date_mise_circulation";
    private final String kilometrage = "kilometrage";

    /**
     * {@inheritDoc}
     */
    @Override
    public int creer(String vehiculeImmat, String vehiculeMarque, String vehiculeModele, LocalDate vehiculeDate, int vehiculeKilo) throws DAOException {
        String query = "insert into " + table + " (" +
                immatriculation + ", " +
                marque + ", " +
                modele + ", " +
                date_mise_circulation + ", " +
                kilometrage + ") values (?,?,?,?,?)";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehiculeImmat);
            ps.setString(2, vehiculeMarque);
            ps.setString(3, vehiculeModele);
            ps.setDate(4, Date.valueOf(vehiculeDate));
            ps.setInt(5, vehiculeKilo);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new DAOException("Impossible de récupérer l'identifiant du véhicule.");
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la création du véhicule : " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicule trouverParId(int id) throws DAOException {
        String query = "select * from " + table + " where " + this.id + "=?";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireVehicule(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du véhicule");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicule trouverParImmatriculation(String immat) throws DAOException {
        String query = "select * from " + table + " where " + immatriculation + "=?";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setString(1, immat);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construireVehicule(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du véhicule");
        }
    }

    public List<Vehicule> trouver(String mot) throws DAOException {
        List<Vehicule> listRecherche = new ArrayList<>();
        String query = "select * from " + table + " where " + immatriculation + "=? OR " + marque + "=? OR " + modele + "=?";

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setString(1, mot);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listRecherche.add(construireVehicule(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche du véhicule");
        }
        return listRecherche;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicule> lister() throws DAOException {
        List<Vehicule> vehicules = new ArrayList<>();
        String query = "SELECT * FROM " + table;

        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehicules.add(construireVehicule(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la recherche des véhicules");
        }

        return vehicules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifier(Vehicule vehicule) throws DAOException {
        String query = "update " + table + " set "
                + immatriculation + "=?,"
                + marque + "=?,"
                + modele + "=?,"
                + date_mise_circulation + "=?,"
                + kilometrage + "=?"
                + " WHERE " + id + "=?";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setString(1, vehicule.getImmatriculation());
            ps.setString(2, vehicule.getMarque());
            ps.setString(3, vehicule.getModele());
            ps.setDate(4, Date.valueOf(vehicule.getDateMiseEnCirculation()));
            ps.setInt(5, vehicule.getKilometrage());
            ps.setInt(6, vehicule.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la modification du véhicule");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void supprimer(int id) throws DAOException {
        String query = "delete from " + table + " where " + this.id + " =?";
        try (PreparedStatement ps = DAOFactory.getInstance().getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erreur lors de la suppression du véhicule");
        }
    }

    private Vehicule construireVehicule(ResultSet rs) throws SQLException {
        return new Vehicule(
                rs.getInt(id),
                rs.getString(immatriculation),
                rs.getString(marque),
                rs.getString(modele),
                rs.getDate(date_mise_circulation).toLocalDate(),
                rs.getInt(kilometrage)
        );
    }
}
