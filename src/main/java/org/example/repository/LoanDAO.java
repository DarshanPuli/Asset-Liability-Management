package org.example.repository;

import org.example.model.Loan;
import java.util.List;

public interface LoanDAO {
    void saveLoan(Loan loan);
    Loan getLoanById(String id);
    List<Loan> getAllLoans();
    void updateLoan(Loan loan);
    void deleteLoan(String id);
}

