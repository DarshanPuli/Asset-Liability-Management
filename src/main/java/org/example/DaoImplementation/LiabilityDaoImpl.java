package org.example.DaoImplementation;

import org.example.Dao.LiabilityDao;
import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.LiabilitiesHeld;
import org.example.entity.Liability;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LiabilityDaoImpl implements LiabilityDao {

    Connection connection = OracleDbConnection.getConnection();
    private static final String ADD_LIABILITY = "insert into LIABILITY (liability_id,liability_name,interest_rate,rate_type,repricing_Date) values (?,?,?,?,?)";
    private static final String GET_ALL_LIABILITIES_HELD = "select * from LIABILITIESHELD";
    private static final String GET_LIABILITIES_BY_LIABILITY_ID = "select * from LIABILITIESHELD where liability_id=?";


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

    public List<LiabilitiesHeld> getAllLiabilitiesByLiabilityId(String liabilityId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_LIABILITIES_BY_LIABILITY_ID);
        preparedStatement.setString(1, liabilityId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<LiabilitiesHeld> liabilitiesHeld = new ArrayList<>();
        while(resultSet.next()){
            LiabilitiesHeld liabilitiesHeldTemp = new LiabilitiesHeld();
            liabilitiesHeldTemp.setLiabilityId(UUID.fromString(resultSet.getString("liability_id")));
            liabilitiesHeldTemp.setUserId(UUID.fromString(resultSet.getString("user_id")));
            liabilitiesHeldTemp.setMaturityDate(resultSet.getObject("maturity_date", LocalDate.class));
            liabilitiesHeldTemp.setPrincipalAmount(resultSet.getLong(("principal_amount")));
            liabilitiesHeldTemp.setAmountLeftToRepay(resultSet.getLong(("amount_left_to_repay")));
            liabilitiesHeldTemp.setCreatedAt(resultSet.getObject("created_at", LocalDate.class));
            liabilitiesHeld.add(liabilitiesHeldTemp);
        }
        return liabilitiesHeld;
    }

    public long getTotalLiabilitiesValue() throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_LIABILITIES_HELD);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();
        long totalLiabilitiesValue = 0;
        while(resultSet.next()){
            long amountLeftToRepay = resultSet.getLong("amount_left_to_repay");
            totalLiabilitiesValue+=amountLeftToRepay;
        }
        return totalLiabilitiesValue;
    }
}
