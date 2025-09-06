package org.example.model;

import java.time.LocalDate;

/**
 * Represents a financial asset that extends the base FinancialInstrument class.
 * Assets are income-generating instruments owned by the institution (loans, bonds, etc.).
 * This class provides concrete implementations for duration and present value calculations
 * specific to asset instruments.
 *
 * Used throughout the risk management system for:
 * - Portfolio risk analysis in RiskService classes
 * - Asset-liability matching in MatchingService
 * - Scenario analysis in ScenarioAnalysisService
 * - Reporting in ReportingService
 */
public class Asset extends FinancialInstrument {

    /**
     * Constructs a new Asset with the specified parameters.
     *
     * @param id Unique identifier for the asset
     * @name Descriptive name of the asset
     * @param principalAmount The face value or principal amount
     * @param interestRate Annual interest rate (as decimal, e.g., 0.05 for 5%)
     * @param maturityDate Date when the principal is repaid
     * @param currency Currency denomination (USD, EUR, etc.)
     * @param isFixedRate Whether the interest rate is fixed or variable
     * @param type Specific asset type (Loan, Bond, etc.)
     */
    public Asset(String id, String name, double principalAmount, double interestRate,
                 LocalDate maturityDate, String currency, boolean isFixedRate, String type) {
        super(id, name, principalAmount, interestRate, maturityDate, currency, isFixedRate, type);
    }

    /**
     * Calculates the Macaulay duration of the asset.
     * Duration measures interest rate sensitivity - how much the price changes
     * for a 1% change in yield. Higher duration = higher interest rate risk.
     *
     * Simplified approach:
     * - Fixed rate assets: Duration equals years to maturity
     * - Variable rate assets: Assumed duration of 0.5 years (resets frequently)
     *
     * @param yield The current yield to maturity (not used in this simplified model)
     * @return Duration in years
     *
     * Used by: InterestRateRiskService for gap analysis and portfolio duration calculation
     */
    @Override
    public double calculateDuration(double yield) {
        double yearsToMaturity = getYearFractionToMaturity();
        if (yearsToMaturity <= 0) return 0;  // Matured instruments have zero duration

        if (isFixedRate()) {
            return yearsToMaturity;  // Simplified: duration = maturity for fixed rate
        } else {
            return 0.5;  // Simplified: short duration for floating rate instruments
        }
    }

    /**
     * Calculates the present value of future cash flows discounted at the given rate.
     * This represents the current fair value of the asset considering time value of money.
     *
     * Calculation includes:
     * 1. Present value of principal repayment at maturity
     * 2. Present value of all future interest payments
     *
     * @param discountRate The discount rate to apply to future cash flows
     * @return Present value of the asset
     *
     * Used by: Various services for valuation, risk assessment, and scenario analysis
     * Particularly important for interest rate shock scenarios in ScenarioAnalysisService
     */
    @Override
    public double calculatePresentValue(double discountRate) {
        double yearsToMaturity = getYearFractionToMaturity();
        if (yearsToMaturity <= 0) return 0;  // No value for matured instruments

        // Present value of principal repayment
        double principalPV = getPrincipalAmount() / Math.pow(1 + discountRate, yearsToMaturity);

        // Annual interest payment amount
        double annualPayment = getPrincipalAmount() * getInterestRate();
        double interestPV = 0;

        // Sum present value of all interest payments
        for (int i = 1; i <= yearsToMaturity; i++) {
            interestPV += annualPayment / Math.pow(1 + discountRate, i);
        }

        return principalPV + interestPV;  // Total present value
    }
}