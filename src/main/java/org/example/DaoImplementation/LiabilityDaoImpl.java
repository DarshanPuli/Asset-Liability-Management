package org.example.DaoImplementation;

import org.example.Dao.LiabilityDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.Liability;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LiabilityDaoImpl implements LiabilityDao {

    Connection connection = OracleDbConnection.getConnection();
    private static final String ADD_LIABILITY = "insert into LIABILITY (liability_id,liability_name,interest_rate,rate_type,repricing_Date) values (?,?,?,?,?)";

    public LiabilityDaoImpl() throws SQLException {
    }

    @Override
    public void addLiability(Liability liability) throws SQLException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_LIABILITY);
            preparedStatement.setString(1,liability.getLiabilityId().toString());
            preparedStatement.setString(2,liability.getLiabilityName());
            preparedStatement.setDouble(3,liability.getInterestRate());
            preparedStatement.setString(4,liability.getRateType());
            preparedStatement.setObject(5,liability.getRepricingDate());
            preparedStatement.executeUpdate();
            System.out.println("Liability added successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
