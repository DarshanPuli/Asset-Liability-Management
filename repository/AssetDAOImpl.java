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
        return new Asset(
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
