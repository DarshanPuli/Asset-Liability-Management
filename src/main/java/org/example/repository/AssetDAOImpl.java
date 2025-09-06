package org.example.repository;

import org.example.model.Asset;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AssetDAOImpl implements AssetDAO {

    @Override
    public void saveAsset(Asset asset) {
        String sql = "INSERT INTO assets (id, name, principal_amount, interest_rate, maturity_date, currency, is_fixed_rate, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            System.out.println(conn);
            System.out.println("Connected");
            ps.setString(1, asset.getId());
            ps.setString(2, asset.getName());
            ps.setDouble(3, asset.getPrincipalAmount());
            ps.setDouble(4, asset.getInterestRate());
            ps.setDate(5, Date.valueOf(asset.getMaturityDate()));
            ps.setString(6, asset.getCurrency());
            ps.setBoolean(7, asset.isFixedRate());
            ps.setString(8, asset.getType());
            ps.executeUpdate();
            System.out.println("Asset saved: " + asset.getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAsset(Asset asset) {
        String sql = "UPDATE assets SET name=?, principal_amount=?, interest_rate=?, maturity_date=?, currency=?, is_fixed_rate=?, type=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, asset.getName());
            ps.setDouble(2, asset.getPrincipalAmount());
            ps.setDouble(3, asset.getInterestRate());
            ps.setDate(4, Date.valueOf(asset.getMaturityDate()));
            ps.setString(5, asset.getCurrency());
            ps.setBoolean(6, asset.isFixedRate());
            ps.setString(7, asset.getType());
            ps.setString(8, asset.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAsset(String assetId) {
        String sql = "DELETE FROM assets WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assetId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Asset getAssetById(String assetId) {
        String sql = "SELECT * FROM assets WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAsset(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM assets";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assets.add(mapResultSetToAsset(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }

    private Asset mapResultSetToAsset(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String type = rs.getString("type");
        if ("Loan".equalsIgnoreCase(type)) {
            // Fetch loan-specific fields
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM loans WHERE id = ?")) {
                ps.setString(1, id);
                try (ResultSet loanRs = ps.executeQuery()) {
                    if (loanRs.next()) {
                        String name = rs.getString("name");
                        double principal = rs.getDouble("principal_amount");
                        double rate = rs.getDouble("interest_rate");
                        java.sql.Date maturity = rs.getDate("maturity_date");
                        String currency = rs.getString("currency");
                        boolean isFixed = rs.getBoolean("is_fixed_rate");
                        String borrower = loanRs.getString("borrower");
                        int creditRating = loanRs.getInt("credit_rating");
                        double pd = loanRs.getDouble("probability_of_default");
                        double lgd = loanRs.getDouble("loss_given_default");
                        return new org.example.model.Loan(id, name, principal, rate, maturity.toLocalDate(), currency, isFixed, borrower, creditRating, pd, lgd);
                    }
                }
            }
            // If not found in loans table, fallback to Asset
        }
        // Not a loan, or loan not found in loans table
        return new org.example.model.Asset(
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
