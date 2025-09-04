package org.example.DaoImplementation;

import org.example.Dao.AssetDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.Asset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AssetDaoImpl implements AssetDao {

    private final Connection connection = OracleDbConnection.getConnection();

    private final String ADD_ASSET = "insert into ASSET (asset_id,asset_name,interest_rate,rate_type,repricing_date,quality) values (?,?,?,?,?,?)";

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
}
