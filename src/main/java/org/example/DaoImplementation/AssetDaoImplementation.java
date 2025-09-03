package org.example.DaoImplementation;

import org.example.Dao.AssetDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.Asset;
import org.example.entity.MaturityBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.UUID;

public class AssetDaoImplementation implements AssetDao {

    private final Connection connection = OracleDbConnection.getConnection();
    private final String ADD_ASSET = "INSERT INTO asset (asset_id, asset_type, interest_rate, rate_type, months_to_expiry, repricing_date) VALUES (?, ?, ?, ?, ?, ?)";
    private final String GET_MONTHS_TO_EXPIRY = "SELECT months_to_expiry FROM assets WHERE asset_id = ?";
    private final String GET_ASSET = "SELECT * FROM assets WHERE asset_id = ?";

    public AssetDaoImplementation() throws SQLException {
    }

    @Override
    public void addAsset(Asset asset) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD_ASSET);
        statement.setString(1, asset.getAssetId().toString());
        statement.setString(2, asset.getAssetType());
        statement.setDouble(3, asset.getInterestRate());
        statement.setString(4, asset.getRateType());
        statement.setInt(5, asset.getMonthsToExpiry());
        statement.setDate(6, java.sql.Date.valueOf(asset.getRepricingDate()));
        statement.execute();
    }

    public int getMonthsToExpiry(UUID assetId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_MONTHS_TO_EXPIRY);
        statement.setObject(1, assetId);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getInt("months_to_expiry");
        }
        return 0;
    }

    @Override
    public void deleteAsset(Asset asset) {

    }

    @Override
    public void updateAsset(Asset asset) {

    }

    @Override
    public Asset getAsset(UUID assetId) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(GET_ASSET);
        preparedStatement.setObject(1, assetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            Asset asset = new Asset();
            asset.setInterestRate(resultSet.getDouble("interest_rate"));
            asset.setRateType(resultSet.getString("rate_type"));
            asset.setMonthsToExpiry(resultSet.getInt("months_to_expiry"));
            return asset;
        }
        return null;
    }

    @Override
    public int updateCount(UUID assetId) {
        return 0;
    }

    @Override
    public Asset addAssetToMaturityBucket(UUID assetId) {
        return null;
    }
}
