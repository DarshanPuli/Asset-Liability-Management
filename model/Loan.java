package org.example.model;

import java.time.LocalDate;

public class Loan extends Asset {
    private String borrower;
    private int creditRating;
    private double probabilityOfDefault;
    private double lossGivenDefault;

    public Loan(String id, String name, double principalAmount, double interestRate,
                LocalDate maturityDate, String currency, boolean isFixedRate,
                String borrower, int creditRating, double probabilityOfDefault, double lossGivenDefault) {
        super(id, name, principalAmount, interestRate, maturityDate, currency, isFixedRate, "Loan");
        this.borrower = borrower;
        this.creditRating = creditRating;
        this.probabilityOfDefault = probabilityOfDefault;
        this.lossGivenDefault = lossGivenDefault;
    }

    public double calculateExpectedLoss() {
        return getPrincipalAmount() * probabilityOfDefault * lossGivenDefault;
    }

    public String getBorrower() { return borrower; }
    public int getCreditRating() { return creditRating; }
    public double getProbabilityOfDefault() { return probabilityOfDefault; }
    public double getLossGivenDefault() { return lossGivenDefault; }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Borrower: %s, Credit Rating: %d]", borrower, creditRating);
    }
}