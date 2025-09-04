package org.example.DaoImplementation;

import org.example.Dao.UserDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    Connection connection = OracleDbConnection.getConnection();

    public MaturityBucketDaoImpl maturityBucketDaoImpl = new MaturityBucketDaoImpl();



    public static final String ADD_USER = "insert into USERS (user_id,name,phone_number,credit_rating) values (?,?,?,?)";
    public static final String PURCHASE_ASSET = "insert into ASSETSHELD (user_id,asset_id,principal_amount,maturity_date,amount_left_to_repay,possibility_of_default,created_at) values (?,?,?,?,?,?,?)";

    public UserDaoImpl() throws SQLException {
    }


    @Override
    public void addUser(User user) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER);
            preparedStatement.setString(1,user.getUserId().toString());
            preparedStatement.setString(2,user.getName());
            preparedStatement.setString(3,user.getNumber());
            preparedStatement.setInt(4,user.getCreditRating());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void purchaseAsset(AssetsHeld assetsHeld) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(PURCHASE_ASSET);
            preparedStatement.setString(1,assetsHeld.getUserId().toString());
            preparedStatement.setString(2,assetsHeld.getAssetId().toString());
            preparedStatement.setLong(3,assetsHeld.getPrincipalAmount());
            preparedStatement.setObject(4,assetsHeld.getMaturityDate());
            preparedStatement.setLong(5,assetsHeld.getAmountLeftToRepay());
            preparedStatement.setInt(6,assetsHeld.getPossibilityOfDefault());
            preparedStatement.setObject(7,assetsHeld.getCreatedAt());
            System.out.println(assetsHeld);
            System.out.println("helloooooooooooooooooooooooooo");
            preparedStatement.executeUpdate();
            System.out.println("Asset purchased successfully");
            maturityBucketDaoImpl.addAssetToMaturityBucket(assetsHeld);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
