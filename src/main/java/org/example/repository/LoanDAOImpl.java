package org.example.repository;

import org.example.model.Loan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {
    @Override
    public void saveLoan(Loan loan) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "MERGE INTO loans l USING (SELECT ? AS id FROM dual) d ON (l.id = d.id) " +
                    "WHEN MATCHED THEN UPDATE SET borrower=?, credit_rating=?, probability_of_default=?, loss_given_default=? " +
                    "WHEN NOT MATCHED THEN INSERT (id, borrower, credit_rating, probability_of_default, loss_given_default) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, loan.getId());
                ps.setString(2, loan.getBorrower());
                ps.setInt(3, loan.getCreditRating());
                ps.setDouble(4, loan.getProbabilityOfDefault());
                ps.setDouble(5, loan.getLossGivenDefault());
                ps.setString(6, loan.getId());
                ps.setString(7, loan.getBorrower());
                ps.setInt(8, loan.getCreditRating());
                ps.setDouble(9, loan.getProbabilityOfDefault());
                ps.setDouble(10, loan.getLossGivenDefault());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs, Connection conn) throws SQLException {
        String id = rs.getString("id");
        String borrower = rs.getString("borrower");
        int creditRating = rs.getInt("credit_rating");
        double pd = rs.getDouble("probability_of_default");
        double lgd = rs.getDouble("loss_given_default");
        // Query assets table for asset fields
        String assetSql = "SELECT * FROM assets WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(assetSql)) {
            ps.setString(1, id);
            try (ResultSet assetRs = ps.executeQuery()) {
                if (assetRs.next()) {
                    String name = assetRs.getString("name");
                    double principal = assetRs.getDouble("principal_amount");
                    double rate = assetRs.getDouble("interest_rate");
                    java.sql.Date maturity = assetRs.getDate("maturity_date");
                    String currency = assetRs.getString("currency");
                    boolean isFixed = assetRs.getBoolean("is_fixed_rate");
                    return new org.example.model.Loan(id, name, principal, rate, maturity.toLocalDate(), currency, isFixed, borrower, creditRating, pd, lgd);
                }
            }
        }
        return null;
    }

    @Override
    public Loan getLoanById(String id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM loans WHERE id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs, conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM loans")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Loan loan = mapResultSetToLoan(rs, conn);
                    if (loan != null) loans.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    @Override
    public void updateLoan(Loan loan) {
        saveLoan(loan);
    }

    @Override
    public void deleteLoan(String id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM loans WHERE id = ?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
