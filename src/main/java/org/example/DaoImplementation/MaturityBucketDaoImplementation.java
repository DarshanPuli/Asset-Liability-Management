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
    private static final String UPDATE_MATURITY_BUCKET = "update MaturityBucket SET total_assets_value = ?, net_gap = ? WHERE bucket_id = ?";
    private static final String GET_MATURITY_BUCKET = "select * from MaturityBucket";
    private static final String ADD_MATURITY_BUCKET = "insert into MaturityBucket (bucket_ID,bucket_name,start_range,end_range,description) values (?,?,?,?,?)";
    private static final String DELETE_MATURITY_BUCKET = "delete from MaturityBucket where bucket_id = ?";

    public MaturityBucketDaoImplementation() throws SQLException {}


    public void addMaturityBucket(MaturityBucket maturityBucket) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_MATURITY_BUCKET);
        preparedStatement.setString(1, maturityBucket.getBucketID().toString());
        preparedStatement.setString(2, maturityBucket.getBucketName());
        preparedStatement.setObject(3, maturityBucket.getStartRange());
        preparedStatement.setObject(4, maturityBucket.getEndRange());
        preparedStatement.setObject(5, maturityBucket.getDescription());
        preparedStatement.execute();
        System.out.println("Maturity bucket added successfully");
    }
    // PENDING (HANDLE LIABILITIES CASE AS WELL)
    @Override
    public void updateMaturityBucket(MaturityBucket maturityBucket) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MATURITY_BUCKET);
        preparedStatement.setDouble(1,maturityBucket.getTotalAssetsValue());
        preparedStatement.setDouble(2,maturityBucket.getNetGap());
        preparedStatement.setString(3,maturityBucket.getBucketID().toString());
        preparedStatement.executeUpdate();
        System.out.println("Maturity bucket updated successfully");
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
                maturityBucket.setBucketID(UUID.fromString(resultSet.getString("BUCKET_ID")));
                maturityBucket.setBucketName(resultSet.getString("BUCKET_NAME"));
                maturityBucket.setStartRange(resultSet.getInt("START_RANGE"));
                maturityBucket.setEndRange(resultSet.getInt("END_RANGE"));
                maturityBucket.setDescription(resultSet.getString("DESCRIPTION"));
                maturityBucket.setTotalAssetsValue(resultSet.getDouble("TOTAL_ASSETS_VALUE"));
                maturityBucket.setTotalLiabilitiesValue(resultSet.getDouble("TOTAL_LIABILITIES_VALUE"));
                maturityBucket.setNetGap(resultSet.getDouble("NET_GAP"));
                maturityBucket.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
                maturityBucket.setUpdatedAt(resultSet.getTimestamp("UPDATED_AT"));
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
                maturityBucket.setBucketID(UUID.fromString(resultSet.getString("bucket_id")));
                maturityBucket.setBucketName(resultSet.getString("bucket_name"));
                maturityBucket.setStartRange(resultSet.getInt("START_RANGE"));
                maturityBucket.setEndRange(resultSet.getInt("END_RANGE"));
                maturityBucket.setDescription(resultSet.getString("DESCRIPTION"));
                maturityBucket.setTotalAssetsValue(resultSet.getDouble("total_assets_value"));
                maturityBucket.setTotalLiabilitiesValue(resultSet.getDouble("total_liabilities_value"));
                maturityBucket.setNetGap(resultSet.getDouble("net_gap"));
                maturityBucket.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
                maturityBucket.setUpdatedAt(resultSet.getTimestamp("UPDATED_AT"));
                System.out.println(maturityBucket);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteMaturityBucket(String uuid){
        try{
            PreparedStatement statement = connection.prepareStatement(DELETE_MATURITY_BUCKET);
            statement.setString(1, uuid);
            statement.execute();
            System.out.println("Maturity bucket deleted successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
