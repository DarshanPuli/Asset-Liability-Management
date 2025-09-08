package org.example.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Abstract base class representing a financial instrument.
 * Serves as the foundation for all financial products (Assets, Liabilities, Loans, Deposits)
 * in the risk management system. Provides common properties and behaviors that all
 * financial instruments share, while enforcing implementation of key financial calculations.
 *
 * This class is the core data model used throughout the entire application by all services.
 */
public abstract class FinancialInstrument {
    // Core instrument identification and characteristics
    private final String id;
    private final String name;
    private double principalAmount;
    private double interestRate;
    private final LocalDate maturityDate;
    private final String currency;
    private final boolean isFixedRate;
    private final String type;

    // Matching and hedging attributes for ALM (Asset-Liability Management)
    private String matchingId; // Links matched assets/liabilities for gap analysis
    private boolean isHedgingInstrument; // For derivatives used in matching strategies
    private String matchingStrategy; // "Duration-match", "Currency-hedge", etc.

    /**
     * Constructs a FinancialInstrument with basic characteristics.
     *
     * @param id Unique identifier for tracking and reference
     * @param name Descriptive name of the instrument
     * @param principalAmount Face value or notional amount
     * @param interestRate Annual interest rate (decimal form)
     * @param maturityDate Date when instrument matures/expires
     * @param currency Currency denomination for FX risk management
     * @param isFixedRate Whether rate is fixed or floating
     * @param type Instrument category (Loan, Deposit, Bond, etc.)
     */
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

    /**
     * Abstract method to calculate duration - must be implemented by subclasses.
     * Duration measures interest rate sensitivity of the instrument.
     *
     * @param yield The yield to maturity for calculation
     * @return Duration in years
     */
    public abstract double calculateDuration(double yield);

    /**
     * Abstract method to calculate present value - must be implemented by subclasses.
     * Represents current fair value discounted at given rate.
     *
     * @param discountRate The rate to discount future cash flows
     * @return Present value of the instrument
     */
    public abstract double calculatePresentValue(double discountRate);

    // Standard getters for instrument properties
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(double amount) {
        principalAmount = amount;
    }
    public double getInterestRate() { return interestRate; }
    public LocalDate getMaturityDate() { return maturityDate; }
    public String getCurrency() { return currency; }
    public boolean isFixedRate() { return isFixedRate; }
    public String getType() { return type; }

    /**
     * Sets interest rate only for variable rate instruments.
     * Protects fixed rate instruments from rate changes.
     *
     * @param rate New interest rate to set
     * Used by ScenarioAnalysisService when applying rate shocks
     */
    public void setInterestRate(double rate) {
        if (!isFixedRate) this.interestRate = rate;
    }

    /**
     * Calculates days remaining until maturity.
     *
     * @return Number of days to maturity
     * Used by LiquidityRiskService for cash flow timing and maturity ladder construction
     */
    public long getDaysToMaturity() {
        return ChronoUnit.DAYS.between(LocalDate.now(), maturityDate);
    }

    /**
     * Converts days to maturity to year fraction for financial calculations.
     * Uses 365-day year convention.
     *
     * @return Years to maturity as decimal
     * Used by duration and PV calculations in subclasses
     */
    public double getYearFractionToMaturity() {
        return getDaysToMaturity() / 365.0;
    }

    /**
     * Checks if this instrument is matched to another instrument for ALM purposes.
     *
     * @param other The other FinancialInstrument to check against
     * @return True if instruments share the same matching ID
     * Used by MatchingService to verify and analyze matched pairs
     */
    public boolean isMatchedTo(FinancialInstrument other) {
        return this.matchingId != null && this.matchingId.equals(other.matchingId);
    }

    /**
     * Provides formatted string representation of the instrument.
     *
     * @return Descriptive string with key instrument details
     * Used throughout system for logging, reporting, and debugging
     */
    @Override
    public String toString() {
        return String.format("%s: %s (Principal: %.2f, Rate: %.2f%%, Maturity: %s)",
                type, name, principalAmount, interestRate * 100, maturityDate);
    }
}