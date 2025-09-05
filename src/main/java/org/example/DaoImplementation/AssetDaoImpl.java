package org.example.DaoImplementation;

import org.example.Dao.AssetDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.Asset;
import org.example.entity.AssetsHeld;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssetDaoImpl implements AssetDao {

    private final Connection connection = OracleDbConnection.getConnection();

    private final String ADD_ASSET = "insert into ASSET (asset_id,asset_name,interest_rate,rate_type,repricing_date,quality) values (?,?,?,?,?,?)";
    private final String GET_ASSETS_BY_ASSET_ID = "select * from ASSETSHELD where asset_id = ?";
    private final String GET_ALL_ASSETS = "select * from ASSETSHELD";

    public AssetDaoImpl() throws SQLException {
    }


    @Override
    public void addAsset(Asset asset) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_ASSET);

            preparedStatement.setString(1, asset.getAssetId().toString());
            preparedStatement.setString(2, asset.getAssetName());
            preparedStatement.setDouble(3, asset.getInterestRate());
            preparedStatement.setString(4, asset.getRateType());
            preparedStatement.setObject(5, asset.getRepricingDate());
            preparedStatement.setObject(6, asset.getQuality());

            preparedStatement.execute();
            System.out.println("Asset has been added successfully");
        }catch(Exception e){
            System.out.println("Error adding asset");
        }

    }

    @Override
    public List<AssetsHeld> getAllAssetsByAssetId(String assetId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ASSETS_BY_ASSET_ID);
        preparedStatement.setString(1, assetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<AssetsHeld> assetsHeld = new ArrayList<>();
        while(resultSet.next()){
            AssetsHeld assetsHeldTemp = new AssetsHeld();
            assetsHeldTemp.setAssetId(UUID.fromString(resultSet.getString("asset_id")));
            assetsHeldTemp.setUserId(UUID.fromString(resultSet.getString("user_id")));
            assetsHeldTemp.setMaturityDate(resultSet.getObject("maturity_date", LocalDate.class));
            assetsHeldTemp.setPrincipalAmount(resultSet.getLong(("principal_amount")));
            assetsHeldTemp.setAmountLeftToRepay(resultSet.getLong(("amount_left_to_repay")));
            assetsHeldTemp.setPossibilityOfDefault(resultSet.getInt("possibility_of_default"));
            assetsHeldTemp.setCreatedAt(resultSet.getObject("created_at", LocalDate.class));
            assetsHeld.add(assetsHeldTemp);
        }
        return assetsHeld;
    }

    @Override
    public long getTotalAssetsValue() throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ASSETS);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();
        long totalAssetsValue = 0;
        while(resultSet.next()){
            long amountLeftToRepay = resultSet.getLong("amount_left_to_repay");
            totalAssetsValue+=amountLeftToRepay;
        }
        return totalAssetsValue;
    }
}
