package org.example.repository;

import org.example.model.Liability;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LiabilityDAOImpl implements LiabilityDAO {
    @Override
    public void saveLiability(Liability liability) {
        String sql = "INSERT INTO liabilities (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, liability.getId());
            ps.setString(2, liability.getName());
            ps.setDouble(3, liability.getPrincipalAmount());
            ps.setDouble(4, liability.getInterestRate());
            ps.setDate(5, Date.valueOf(liability.getMaturityDate()));
            ps.setString(6, liability.getCurrency());
            ps.setBoolean(7, liability.isFixedRate());
            ps.setString(8, liability.getType());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLiability(Liability liability) {
        String sql = "UPDATE liabilities SET name=?, principal_amount=?, interest_rate=?, maturity_date=?, currency=?, is_fixed_rate=?, type=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, liability.getName());
            ps.setDouble(2, liability.getPrincipalAmount());
            ps.setDouble(3, liability.getInterestRate());
            ps.setDate(4, Date.valueOf(liability.getMaturityDate()));
            ps.setString(5, liability.getCurrency());
            ps.setBoolean(6, liability.isFixedRate());
            ps.setString(7, liability.getType());
            ps.setString(8, liability.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLiability(String liabilityId) {
        String sql = "DELETE FROM liabilities WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, liabilityId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Liability getLiabilityById(String liabilityId) {
        String sql = "SELECT * FROM liabilities WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, liabilityId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLiability(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Liability> getAllLiabilities() {
        List<Liability> liabilities = new ArrayList<>();
        String sql = "SELECT * FROM liabilities";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liabilities.add(mapResultSetToLiability(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liabilities;
    }

    private Liability mapResultSetToLiability(ResultSet rs) throws SQLException {
        return new Liability(
            rs.getString("id"),
            rs.getString("name"),
            rs.getDouble("principal_amount"),
            rs.getDouble("interest_rate"),
            rs.getDate("maturity_date").toLocalDate(),
            rs.getString("currency"),
            rs.getBoolean("is_fixed_rate"),
            rs.getString("type")
        );
    }
}
