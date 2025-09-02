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
    private final String ADD_ASSET = "INSERT INTO assets (asset_id, asset_type, principal_amount, interest_rate, rate_type, maturity_date, repricing_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String GET_MATURTY_DATE = "SELECT maturity_date FROM assets WHERE asset_id = ?";
    private final String GET_ASSET = "SELECT * FROM assets WHERE asset_id = ?";

    public AssetDaoImplementation() throws SQLException {
    }

    @Override
    public void addAsset(Asset asset) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ADD_ASSET);
        statement.setObject(1, asset.getAssetId());
        statement.setString(2, asset.getAssetType());
        statement.setDouble(3, asset.getPrincipalAmount());
        statement.setDouble(4, asset.getInterestRate());
        statement.setString(5, asset.getRateType());
        statement.setDate(6, java.sql.Date.valueOf(asset.getMaturityDate()));
        statement.setDate(7, java.sql.Date.valueOf(asset.getRepricingDate()));
        statement.execute();
    }

    public LocalDate getMaturityDate(UUID assetId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_MATURTY_DATE);
        statement.setObject(1, assetId);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            return resultSet.getObject("maturity_date", LocalDate.class);
        }
        return null;
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
            asset.setPrincipalAmount(resultSet.getDouble("principal_amount"));
            asset.setInterestRate(resultSet.getDouble("interest_rate"));
            asset.setRateType(resultSet.getString("rate_type"));
            asset.setMaturityDate(resultSet.getObject("maturity_date", LocalDate.class));
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
