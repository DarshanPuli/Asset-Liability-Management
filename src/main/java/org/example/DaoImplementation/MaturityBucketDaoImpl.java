package org.example.DaoImplementation;

import org.example.Dao.MaturityBucketDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.LiabilitiesHeld;
import org.example.entity.MaturityBucket;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaturityBucketDaoImpl implements MaturityBucketDao {

    private Connection connection = OracleDbConnection.getConnection();

    private static final String ADD_MATURITY_BUCKET= "insert into MATURITYBUCKET (bucket_id,start_range,end_range,total_assets_value,total_liabilities_value) values (?,?,?,?,?)";
    private static final String GET_MATURITY_BUCKET= "select * from MATURITYBUCKET where start_range<=? and end_range>=?";
    private static final String UPDATE_MATURITY_BUCKET_BY_BUCKET_ID = "update MATURITYBUCKET set total_assets_value = ?,total_liabilities_value = ? where bucket_id = ? ";
    private static final String GET_ALL_MATURITY_BUCKETS = "select * from MATURITYBUCKET";
    public MaturityBucketDaoImpl() throws SQLException {
    }


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

    @Override
    public List<MaturityBucket> getAllMaturityBuckets() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MATURITY_BUCKETS);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<MaturityBucket> maturityBuckets = new ArrayList<>();
        while(resultSet.next()){
            MaturityBucket maturityBucket = new MaturityBucket();
            maturityBucket.setBucketID(UUID.fromString(resultSet.getString("bucket_id")));
            maturityBucket.setStartRange(resultSet.getInt("start_range"));
            maturityBucket.setEndRange(resultSet.getInt("end_range"));
            maturityBucket.setTotalAssetsValue(resultSet.getDouble("total_assets_value"));
            maturityBucket.setTotalLiabilitiesValue(resultSet.getDouble("total_liabilities_value"));
            maturityBuckets.add(maturityBucket);
        }
        return maturityBuckets;
    }

    public void addAssetToMaturityBucket(AssetsHeld assetsHeld) throws SQLException {
        try{
            LocalDate maturityDate = assetsHeld.getMaturityDate();
            Period timeLeft = Period.between(LocalDate.now(),maturityDate);
            int timeLeftInMonths = timeLeft.getYears()*12+timeLeft.getMonths();



            PreparedStatement preparedStatement = connection.prepareStatement(GET_MATURITY_BUCKET);
            preparedStatement.setInt(1,timeLeftInMonths);
            preparedStatement.setInt(2,timeLeftInMonths);
            ResultSet rs = preparedStatement.executeQuery();


            MaturityBucket maturityBucket = new MaturityBucket();

            while(rs.next()){
                maturityBucket.setBucketID(UUID.fromString(rs.getString("bucket_id")));
                maturityBucket.setStartRange(rs.getInt("start_range"));
                maturityBucket.setEndRange(rs.getInt("end_range"));
                maturityBucket.setTotalAssetsValue(rs.getDouble("total_assets_value"));
                maturityBucket.setTotalLiabilitiesValue(rs.getDouble("total_liabilities_value"));
            }


            double newTotalAssetsValue = maturityBucket.getTotalAssetsValue()+assetsHeld.getPrincipalAmount();
            maturityBucket.setTotalAssetsValue(newTotalAssetsValue);
            PreparedStatement preparedStatement1 = connection.prepareStatement(UPDATE_MATURITY_BUCKET_BY_BUCKET_ID);
            preparedStatement1.setDouble(1,newTotalAssetsValue);
            preparedStatement1.setDouble(2,maturityBucket.getTotalLiabilitiesValue());
            preparedStatement1.setString(3,maturityBucket.getBucketID().toString());
            preparedStatement1.executeUpdate();

            System.out.println("Maturity Bucket has been updated successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    public void addLiabilityToMaturityBucket(LiabilitiesHeld liabilitiesHeld) throws SQLException {
        try{
            LocalDate maturityDate = liabilitiesHeld.getMaturityDate();
            Period timeLeft = Period.between(LocalDate.now(),maturityDate);
            int timeLeftInMonths = timeLeft.getYears()*12+timeLeft.getMonths();



            PreparedStatement preparedStatement = connection.prepareStatement(GET_MATURITY_BUCKET);
            preparedStatement.setInt(1,timeLeftInMonths);
            preparedStatement.setInt(2,timeLeftInMonths);
            ResultSet rs = preparedStatement.executeQuery();

            MaturityBucket maturityBucket = new MaturityBucket();

            while(rs.next()){
                maturityBucket.setBucketID(UUID.fromString(rs.getString("bucket_id")));
                maturityBucket.setStartRange(rs.getInt("start_range"));
                maturityBucket.setEndRange(rs.getInt("end_range"));
                maturityBucket.setTotalAssetsValue(rs.getDouble("total_assets_value"));
                maturityBucket.setTotalLiabilitiesValue(rs.getDouble("total_liabilities_value"));
            }


            double newTotalLiabilitiesValue = maturityBucket.getTotalLiabilitiesValue()+liabilitiesHeld.getPrincipalAmount();
            maturityBucket.setTotalLiabilitiesValue(newTotalLiabilitiesValue);
            PreparedStatement preparedStatement1 = connection.prepareStatement(UPDATE_MATURITY_BUCKET_BY_BUCKET_ID);
            preparedStatement1.setDouble(1,maturityBucket.getTotalAssetsValue());
            preparedStatement1.setDouble(2,newTotalLiabilitiesValue);
            preparedStatement1.setString(3,maturityBucket.getBucketID().toString());
            preparedStatement1.executeUpdate();

            System.out.println("Maturity Bucket has been updated successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
