package org.example.service;

// src/com/alm/service/CreditRiskService.java

import org.example.model.Asset;
import org.example.model.Loan;

import java.util.List;

/**
 * Service for calculating credit risk metrics including expected loss,
 * risk-weighted assets, and capital adequacy.
 * Used by risk management teams for regulatory reporting and capital allocation decisions.
 */
import java.util.*;
import java.util.stream.Collectors;

public class CreditRiskService {

    // Basel III risk weights (simplified)
    private static final Map<Integer, Double> CREDIT_RATING_RISK_WEIGHTS = Map.of(
            1, 1.50, // Default/Very Poor: 150%
            2, 1.25, // Poor: 125%
            3, 1.00, // Below Average: 100%
            4, 0.85, // Average: 85%
            5, 0.75, // Above Average: 75%
            6, 0.50, // Good: 50%
            7, 0.35, // Very Good: 35%
            8, 0.20, // Excellent: 20%
            9, 0.15, // Superior: 15%
            10, 0.10 // Prime: 10%
    );

    private static final Map<String, Double> ASSET_TYPE_RISK_WEIGHTS = Map.of(
            "cash", 0.00,
            "government_bond", 0.05,
            "municipal_bond", 0.20,
            "corporate_bond", 0.50,
            "loan", 1.00, // Default for loans, overridden by credit rating
            "other", 1.00
    );

    /**
     * Enhanced expected loss calculation with regulatory adjustments
     */
    public double calculateTotalExpectedLoss(List<Loan> loans) {
        return loans.stream()
                .mapToDouble(this::calculateAdjustedExpectedLoss)
                .sum();
    }

    private double calculateAdjustedExpectedLoss(Loan loan) {
        double baseEL = loan.calculateExpectedLoss();
        // Apply maturity adjustment (Basel II/III)
        double maturityAdjustment = 1 + (0.04 * Math.min(loan.getDaysToMaturity() / 365.0, 5));
        return baseEL * maturityAdjustment;
    }

    /**
     * Basel III compliant risk-weighted assets calculation
     */
    public double calculateRiskWeightedAssets(List<Asset> assets) {
        return assets.stream()
                .mapToDouble(this::calculateBaselRiskWeight)
                .sum();
    }

    private double calculateBaselRiskWeight(Asset asset) {
        double riskWeight = getBaselRiskWeight(asset);
        return asset.getPrincipalAmount() * riskWeight;
    }

    private double getBaselRiskWeight(Asset asset) {
        if (asset instanceof Loan) {
            Loan loan = (Loan) asset;
            return CREDIT_RATING_RISK_WEIGHTS.getOrDefault(
                    Math.min(Math.max(loan.getCreditRating(), 1), 10), 1.00);
        }

        return ASSET_TYPE_RISK_WEIGHTS.getOrDefault(
                asset.getType().toLowerCase(), 1.00);
    }

    /**
     * Enhanced CAR calculation with regulatory thresholds
     */
    public CapitalAdequacyResult calculateCapitalAdequacy(double tier1Capital, double tier2Capital,
                                                          double riskWeightedAssets) {
        double totalCapital = tier1Capital + tier2Capital;
        double car = riskWeightedAssets > 0 ? totalCapital / riskWeightedAssets : Double.POSITIVE_INFINITY;

        return new CapitalAdequacyResult(
                car,
                tier1Capital / riskWeightedAssets,
                totalCapital,
                riskWeightedAssets,
                getCARStatus(car)
        );
    }

    private String getCARStatus(double car) {
        if (car >= 0.105) return "WELL_CAPITALIZED"; // Basel III minimum + conservation buffer
        if (car >= 0.08) return "ADEQUATELY_CAPITALIZED";
        if (car >= 0.06) return "UNDERCAPITALIZED";
        return "CRITICALLY_UNDERCAPITALIZED";
    }

    /**
     * Portfolio credit risk metrics
     */
    public PortfolioCreditMetrics calculatePortfolioMetrics(List<Loan> loans) {
        double totalExposure = loans.stream()
                .mapToDouble(Loan::getPrincipalAmount)
                .sum();

        double totalExpectedLoss = calculateTotalExpectedLoss(loans);
        double averagePD = loans.stream()
                .mapToDouble(Loan::getProbabilityOfDefault)
                .average()
                .orElse(0);

        double averageLGD = loans.stream()
                .mapToDouble(Loan::getLossGivenDefault)
                .average()
                .orElse(0);

        Map<Integer, Double> exposureByRating = loans.stream()
                .collect(Collectors.groupingBy(
                        Loan::getCreditRating,
                        Collectors.summingDouble(Loan::getPrincipalAmount)
                ));

        return new PortfolioCreditMetrics(
                totalExposure,
                totalExpectedLoss,
                averagePD,
                averageLGD,
                exposureByRating
        );
    }

    /**
     * Stress testing under adverse scenarios
     */
    public StressTestResult performStressTest(List<Loan> loans, double pdShock, double lgdShock) {
        double baseEL = calculateTotalExpectedLoss(loans);

        double stressedEL = loans.stream()
                .mapToDouble(loan -> {
                    double stressedPD = loan.getProbabilityOfDefault() * pdShock;
                    double stressedLGD = loan.getLossGivenDefault() * lgdShock;
                    return loan.getPrincipalAmount() * stressedPD * stressedLGD;
                })
                .sum();

        return new StressTestResult(baseEL, stressedEL, pdShock, lgdShock);
    }

    /**
     * Credit concentration analysis
     */
    public ConcentrationAnalysis analyzeConcentration(List<Loan> loans) {
        // Single borrower concentration
        Map<String, Double> borrowerExposure = loans.stream()
                .collect(Collectors.groupingBy(
                        Loan::getBorrower,
                        Collectors.summingDouble(Loan::getPrincipalAmount)
                ));

        // Industry/sector concentration (simplified)
        Map<String, Double> sectorExposure = loans.stream()
                .collect(Collectors.groupingBy(
                        loan -> getLoanSector(loan.getBorrower()),
                        Collectors.summingDouble(Loan::getPrincipalAmount)
                ));

        double largestExposure = borrowerExposure.values().stream()
                .max(Double::compare)
                .orElse(0.0);

        double totalExposure = borrowerExposure.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        return new ConcentrationAnalysis(borrowerExposure, sectorExposure,
                largestExposure, totalExposure);
    }

    private String getLoanSector(String borrower) {
        // Simplified sector mapping - in practice would use proper classification
        if (borrower.toLowerCase().contains("inc") || borrower.toLowerCase().contains("corp"))
            return "CORPORATE";
        if (borrower.toLowerCase().contains("home") || borrower.toLowerCase().contains("loan"))
            return "RETAIL_MORTGAGE";
        return "RETAIL_OTHER";
    }

    // Data classes for comprehensive results
    public static class CapitalAdequacyResult {
        private final double car;
        private final double tier1Ratio;
        private final double totalCapital;
        private final double rwa;
        private final String status;

        public CapitalAdequacyResult(double car, double tier1Ratio, double totalCapital,
                                     double rwa, String status) {
            this.car = car;
            this.tier1Ratio = tier1Ratio;
            this.totalCapital = totalCapital;
            this.rwa = rwa;
            this.status = status;
        }
        // Getters

        public double getCar() {
            return car;
        }

        public double getTier1Ratio() {
            return tier1Ratio;
        }

        public double getTotalCapital() {
            return totalCapital;
        }

        public double getRwa() {
            return rwa;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class PortfolioCreditMetrics {
        private final double totalExposure;
        private final double totalExpectedLoss;
        private final double averagePD;
        private final double averageLGD;
        private final Map<Integer, Double> exposureByRating;

        public PortfolioCreditMetrics(double totalExposure, double totalExpectedLoss,
                                      double averagePD, double averageLGD,
                                      Map<Integer, Double> exposureByRating) {
            this.totalExposure = totalExposure;
            this.totalExpectedLoss = totalExpectedLoss;
            this.averagePD = averagePD;
            this.averageLGD = averageLGD;
            this.exposureByRating = exposureByRating;
        }
        // Getters

        public double getTotalExposure() {
            return totalExposure;
        }

        public double getTotalExpectedLoss() {
            return totalExpectedLoss;
        }

        public double getAveragePD() {
            return averagePD;
        }

        public double getAverageLGD() {
            return averageLGD;
        }

        public Map<Integer, Double> getExposureByRating() {
            return exposureByRating;
        }
    }

    public static class StressTestResult {
        private final double baseExpectedLoss;
        private final double stressedExpectedLoss;
        private final double pdShock;
        private final double lgdShock;

        public StressTestResult(double baseExpectedLoss, double stressedExpectedLoss,
                                double pdShock, double lgdShock) {
            this.baseExpectedLoss = baseExpectedLoss;
            this.stressedExpectedLoss = stressedExpectedLoss;
            this.pdShock = pdShock;
            this.lgdShock = lgdShock;
        }
        // Getters

        public double getBaseExpectedLoss() {
            return baseExpectedLoss;
        }

        public double getStressedExpectedLoss() {
            return stressedExpectedLoss;
        }

        public double getPdShock() {
            return pdShock;
        }

        public double getLgdShock() {
            return lgdShock;
        }
    }

    public static class ConcentrationAnalysis {
        private final Map<String, Double> borrowerExposure;
        private final Map<String, Double> sectorExposure;
        private final double largestExposure;
        private final double totalExposure;

        public ConcentrationAnalysis(Map<String, Double> borrowerExposure,
                                     Map<String, Double> sectorExposure,
                                     double largestExposure, double totalExposure) {
            this.borrowerExposure = borrowerExposure;
            this.sectorExposure = sectorExposure;
            this.largestExposure = largestExposure;
            this.totalExposure = totalExposure;
        }
        // Getters

        public Map<String, Double> getBorrowerExposure() {
            return borrowerExposure;
        }

        public Map<String, Double> getSectorExposure() {
            return sectorExposure;
        }

        public double getLargestExposure() {
            return largestExposure;
        }

        public double getTotalExposure() {
            return totalExposure;
        }
    }
}