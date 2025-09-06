package org.example.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class FinancialInstrument implements Serializable {
    private String id;
    private String name;
    private double principalAmount;
    private double interestRate;
    private LocalDate maturityDate;
    private String currency;
    private boolean isFixedRate;
    private String type;
    private String matchingId; // Links matched assets/liabilities
    private boolean isHedgingInstrument; // For derivatives used in matching
    private String matchingStrategy; // "Duration-match", "Currency-hedge", etc.

    public FinancialInstrument(String id, String name, double principalAmount, double interestRate,
                               LocalDate maturityDate, String currency, boolean isFixedRate, String type) {
        this.id = id;
        this.name = name;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.maturityDate = maturityDate;
        this.currency = currency;
        this.isFixedRate = isFixedRate;
        this.type = type;
    }

    public abstract double calculateDuration(double yield);
    public abstract double calculatePresentValue(double discountRate);

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public String getCurrency() { return currency; }
    public boolean isFixedRate() { return isFixedRate; }
    public String getType() { return type; }

    public void setInterestRate(double rate) {
        if (!isFixedRate) this.interestRate = rate;
    }

    public long getDaysToMaturity() {
        return ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
    }

    public double getYearFractionToMaturity() {
        return getDaysToMaturity() / 365.0;
    }
    public boolean isMatchedTo(FinancialInstrument other) {
        return this.matchingId != null && this.matchingId.equals(other.matchingId);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (Principal: %.2f, Rate: %.2f%%, Maturity: %s)",
                type, name, principalAmount, interestRate * 100, maturityDate);
    }
}
