package org.example.DaoImplementation;

import org.example.Dao.MaturityBucketDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.MaturityBucket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaturityBucketDaoImplementation implements MaturityBucketDao {

    private final Connection connection = OracleDbConnection.getConnection();
    private static final String FIND_MATURITY_BUCKET_BY_RANGE = "select * from MaturityBucket where ?>=start_range and ?<=end_range";
    private static final String UPDATE_MATURITY_BUCKET = "UPDATE MaturityBucket SET total_assets_value = ?, net_gap = ? WHERE bucket_id = ?";
    private static final String GET_MATURITY_BUCKET = "select * from MaturityBucket";
    private static final String ADD_MATURITY_BUCKET = "insert into MaturityBucket (bucket_ID,bucket_name,start_range,end_range,description) values (?,?,?,?,?)";
    public MaturityBucketDaoImplementation() throws SQLException {}


    public void addMaturityBucket(MaturityBucket maturityBucket) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_MATURITY_BUCKET);
        preparedStatement.setString(1, maturityBucket.getBucketID().toString());
        preparedStatement.setString(2, maturityBucket.getBucketName());
        preparedStatement.setObject(3, maturityBucket.getStartRange());
        preparedStatement.setObject(4, maturityBucket.getEndRange());
        preparedStatement.setObject(5, maturityBucket.getDescription());
        preparedStatement.execute();
    }
    // PENDING (HANDLE LIABILITIES CASE AS WELL)
    @Override
    public void updateMaturityBucket(MaturityBucket maturityBucket) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MATURITY_BUCKET);
        preparedStatement.setDouble(1,maturityBucket.getTotalAssetsValue());
        preparedStatement.setDouble(2,maturityBucket.getNetGap());
        preparedStatement.setObject(3,maturityBucket.getBucketID());
        preparedStatement.executeUpdate();
    }

    @Override
    public MaturityBucket findMaturityBucketByRange(int monthsLeftToMaturity) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_MATURITY_BUCKET_BY_RANGE);
            preparedStatement.setInt(1, monthsLeftToMaturity);
            preparedStatement.setInt(2, monthsLeftToMaturity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MaturityBucket maturityBucket = new MaturityBucket();
                maturityBucket.setBucketID(resultSet.getObject("BUCKET_ID", UUID.class));
                maturityBucket.setBucketName(resultSet.getString("BUCKET_NAME"));
                maturityBucket.setTotalAssetsValue(resultSet.getInt("TOTAL_ASSETS_VALUE"));
                maturityBucket.setTotalLiabilitiesValue(resultSet.getInt("TOTAL_LIABILITIES_VALUE"));
                maturityBucket.setNetGap(resultSet.getInt("NET_GAP"));
                return maturityBucket;
            }
            return null;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void getMaturityBuckets(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_MATURITY_BUCKET);
            while (resultSet.next()) {
                MaturityBucket maturityBucket = new MaturityBucket();
                maturityBucket.setBucketID(resultSet.getObject("bucket_id", UUID.class));
                maturityBucket.setBucketName(resultSet.getString("bucket_name"));
                maturityBucket.setTotalAssetsValue(resultSet.getInt("total_assets_value"));
                maturityBucket.setTotalLiabilitiesValue(resultSet.getInt("total_liabilities_value"));
                maturityBucket.setNetGap(resultSet.getInt("net_gap"));
                System.out.println(maturityBucket);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
