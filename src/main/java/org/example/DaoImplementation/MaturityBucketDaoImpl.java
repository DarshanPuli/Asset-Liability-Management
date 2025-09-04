package org.example.DaoImplementation;

import org.example.Dao.MaturityBucketDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.MaturityBucket;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class MaturityBucketDaoImpl implements MaturityBucketDao {

    private Connection connection = OracleDbConnection.getConnection();

    private static final String ADD_MATURITY_BUCKET= "insert into MATURITYBUCKET (bucket_id,start_range,end_range,total_assets_value,total_liabilities_value) values (?,?,?,?,?)";
    private static final String GET_MATURITY_BUCKET= "select * from MATURITYBUCKET where start_range<=? and end_range>=?";
    private static final String UPDATE_MATURITY_BUCKET_BY_BUCKET_ID = "update MATURITYBUCKET set total_assets_value = ? where bucket_id = ? ";

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

    public void addAssetToMaturityBucket(AssetsHeld assetsHeld) throws SQLException {
        try{
            LocalDate maturityDate = assetsHeld.getMaturityDate();
            Period timeLeft = Period.between(LocalDate.now(),maturityDate);
            int timeLeftInMonths = timeLeft.getYears()*12+timeLeft.getMonths();


            System.out.println(timeLeftInMonths);


            PreparedStatement preparedStatement = connection.prepareStatement(GET_MATURITY_BUCKET);
            preparedStatement.setInt(1,timeLeftInMonths);
            preparedStatement.setInt(2,timeLeftInMonths);
            System.out.println("------------------------------------");
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("Maturity bucket fetched successfully");

            MaturityBucket maturityBucket = new MaturityBucket();

            while(rs.next()){
                maturityBucket.setBucketID(UUID.fromString(rs.getString("bucket_id")));
                maturityBucket.setStartRange(rs.getInt("start_range"));
                maturityBucket.setEndRange(rs.getInt("end_range"));
                maturityBucket.setTotalAssetsValue(rs.getDouble("total_assets_value"));
                maturityBucket.setTotalLiabilitiesValue(rs.getDouble("total_liabilities_value"));
            }

            System.out.println(maturityBucket);

            System.out.println(1);
            double newTotalAssetsValue = maturityBucket.getTotalAssetsValue()+assetsHeld.getPrincipalAmount();
            maturityBucket.setTotalAssetsValue(newTotalAssetsValue);
            System.out.println(2);
            PreparedStatement preparedStatement1 = connection.prepareStatement(UPDATE_MATURITY_BUCKET_BY_BUCKET_ID);
            System.out.println(3);
            preparedStatement1.setDouble(1,newTotalAssetsValue);
            System.out.println(4);
            preparedStatement1.setString(2,maturityBucket.getBucketID().toString());
            System.out.println(5);
            preparedStatement1.executeUpdate();

            System.out.println("Maturity Bucket has been updated successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
