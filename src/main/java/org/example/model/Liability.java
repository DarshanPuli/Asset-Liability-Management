package org.example.model;

import java.time.LocalDate;

public class Liability extends FinancialInstrument {
    public Liability(String id, String name, double principalAmount, double interestRate,
                     LocalDate maturityDate, String currency, boolean isFixedRate, String type) {
        super(id, name, principalAmount, interestRate, maturityDate, currency, isFixedRate, type);
    }

    @Override
    public double calculateDuration(double yield) {
        double yearsToMaturity = getYearFractionToMaturity();
        if (yearsToMaturity <= 0) return 0;

        if (isFixedRate()) {
            return yearsToMaturity;
        } else {
            return 0.5;
        }
    }

    @Override
    public double calculatePresentValue(double discountRate) {
        double yearsToMaturity = getYearFractionToMaturity();
        if (yearsToMaturity <= 0) return 0;

        double principalPV = getPrincipalAmount() / Math.pow(1 + discountRate, yearsToMaturity);
        double annualPayment = getPrincipalAmount() * getInterestRate();
        double interestPV = 0;

        for (int i = 1; i <= yearsToMaturity; i++) {
            interestPV += annualPayment / Math.pow(1 + discountRate, i);
        }

        return principalPV + interestPV;
    }
}
