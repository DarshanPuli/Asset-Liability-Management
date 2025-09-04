package org.example.DaoImplementation;

import org.example.Dao.MaturityBucketDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.MaturityBucket;

import java.sql.*;

public class MaturityBucketDaoImpl implements MaturityBucketDao {

    private final Connection connection = OracleDbConnection.getConnection();

    private static final String ADD_MATURITY_BUCKET= "insert into MATURITYBUCKET (bucket_id,start_range,end_range,total_assets_value,total_liabilities_value) values (?,?,?,?,?)";

    public MaturityBucketDaoImpl() throws SQLException {}


    @Override
    public void addMaturityBucket(MaturityBucket maturityBucket) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_MATURITY_BUCKET);
            preparedStatement.setString(1,maturityBucket.getBucketID().toString());
            preparedStatement.setInt(2,maturityBucket.getStartRange());
            preparedStatement.setInt(3,maturityBucket.getEndRange());
            preparedStatement.setDouble(4,maturityBucket.getTotalAssetsValue());
            preparedStatement.setDouble(5,maturityBucket.getTotalLiabilitiesValue());
            preparedStatement.executeUpdate();
            System.out.println("Maturity Bucket has been added successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
