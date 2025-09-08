package org.example.service;

// src/com/alm/service/ScenarioAnalysisService.java

import org.example.model.*;

import java.util.List;

import java.util.*;
import java.util.stream.Collectors;

public class ScenarioAnalysisService {
    private final InterestRateRiskService interestRateRiskService;
    private final CreditRiskService creditRiskService;
    private final LiquidityRiskService liquidityRiskService;

    public ScenarioAnalysisService() {
        this.interestRateRiskService = new InterestRateRiskService();
        this.creditRiskService = new CreditRiskService();
        this.liquidityRiskService = new LiquidityRiskService();
    }

    /**
     * Comprehensive scenario analysis with multiple risk dimensions
     */
    public ScenarioAnalysisResult analyzeScenario(Scenario scenario, List<Asset> assets,
                                                  List<Liability> liabilities, List<Loan> loans) {
        ScenarioAnalysisResult result = new ScenarioAnalysisResult(scenario);

        // Apply scenario shocks
        List<Asset> shockedAssets = applyScenarioShocks(assets, scenario);
        List<Liability> shockedLiabilities = applyScenarioShocks(liabilities, scenario);
        List<Loan> shockedLoans = applyCreditShocks(loans, scenario);

        // Calculate baseline metrics
        calculateBaselineMetrics(result, assets, liabilities, loans);

        // Calculate shocked metrics
        calculateShockedMetrics(result, shockedAssets, shockedLiabilities, shockedLoans, scenario);

        // Calculate impacts and sensitivities
        calculateImpacts(result);

        return result;
    }

    /**
     * Apply comprehensive scenario shocks to instruments
     */
    private <T extends FinancialInstrument> List<T> applyScenarioShocks(List<T> instruments, Scenario scenario) {
        return instruments.stream()
                .map(instrument -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T shocked = (T) instrument.getClass()
                                .getConstructor(instrument.getClass())
                                .newInstance(instrument);

                        // Apply interest rate shocks for variable rate instruments
                        if (!shocked.isFixedRate()) {
                            double rateChange = scenario.getRateChangeForCurrency(shocked.getCurrency());
                            shocked.setInterestRate(shocked.getInterestRate() + rateChange);
                        }

                        // Apply liquidity shocks (reduce principal for stressed scenarios)
                        double liquidityFactor = scenario.getLiquidityShockFactor();
                        if (liquidityFactor != 1.0) {
                            double shockedPrincipal = shocked.getPrincipalAmount() * liquidityFactor;
                            shocked.setPrincipalAmount(shockedPrincipal);
                        }

                        return shocked;
                    } catch (Exception e) {
                        return instrument; // Return original if cloning fails
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Apply credit shocks to loans (PD and LGD increases)
     */
    private List<Loan> applyCreditShocks(List<Loan> loans, Scenario scenario) {
        return loans.stream()
                .map(loan -> {
                    try {
                        Loan shocked = new Loan(
                                loan.getId(), loan.getName(),
                                loan.getPrincipalAmount() * scenario.getLiquidityShockFactor(),
                                loan.getInterestRate(),
                                loan.getMaturityDate(),
                                loan.getCurrency(),
                                loan.isFixedRate(),
                                loan.getBorrower(),
                                loan.getCreditRating(),
                                Math.min(1.0, loan.getProbabilityOfDefault() + scenario.getDefaultRateIncrease()),
                                Math.min(1.0, loan.getLossGivenDefault() * 1.2) // LGD increases 20% in stress
                        );
                        return shocked;
                    } catch (Exception e) {
                        return loan;
                    }
                })
                .collect(Collectors.toList());
    }

    private void calculateBaselineMetrics(ScenarioAnalysisResult result, List<Asset> assets,
                                          List<Liability> liabilities, List<Loan> loans) {
        // Interest rate risk metrics
        double yield = 0.05;
        result.setBaseNii(interestRateRiskService.calculateNetInterestIncome(assets, liabilities, 4).getNetInterestIncome());
        result.setBaseDuration(interestRateRiskService.calculatePortfolioDurationGap(assets, liabilities, yield).getDurationGap());
        result.setBaseEve(calculateEconomicValueOfEquity(assets, liabilities, yield));

        // Credit risk metrics
        result.setBaseEl(creditRiskService.calculateTotalExpectedLoss(loans));
        result.setBaseRwa(creditRiskService.calculateRiskWeightedAssets(assets));

        // Liquidity risk metrics
        result.setBaseLcr(calculateLCR(assets, liabilities));
        result.setBaseNsfr(calculateNSFR(assets, liabilities));
    }

    private void calculateShockedMetrics(ScenarioAnalysisResult result, List<Asset> shockedAssets,
                                         List<Liability> shockedLiabilities, List<Loan> shockedLoans,
                                         Scenario scenario) {
        double yield = 0.05 + scenario.getAverageRateChange(); // Adjust yield for scenario

        // Interest rate risk metrics
        result.setScenarioNii(interestRateRiskService.calculateNetInterestIncome(shockedAssets, shockedLiabilities, 4).getNetInterestIncome());
        result.setScenarioDuration(interestRateRiskService.calculatePortfolioDurationGap(shockedAssets, shockedLiabilities, yield).getDurationGap());
        result.setScenarioEve(calculateEconomicValueOfEquity(shockedAssets, shockedLiabilities, yield));

        // Credit risk metrics
        result.setScenarioEl(creditRiskService.calculateTotalExpectedLoss(shockedLoans));
        result.setScenarioRwa(creditRiskService.calculateRiskWeightedAssets(shockedAssets));

        // Liquidity risk metrics
        result.setScenarioLcr(calculateLCR(shockedAssets, shockedLiabilities));
        result.setScenarioNsfr(calculateNSFR(shockedAssets, shockedLiabilities));
    }

    private void calculateImpacts(ScenarioAnalysisResult result) {
        // Calculate percentage changes
        result.setNiiImpact((result.getScenarioNii() - result.getBaseNii()) / Math.abs(result.getBaseNii()));
        result.setDurationImpact(result.getScenarioDuration() - result.getBaseDuration());
        result.setEveImpact((result.getScenarioEve() - result.getBaseEve()) / Math.abs(result.getBaseEve()));
        result.setElImpact((result.getScenarioEl() - result.getBaseEl()) / Math.abs(result.getBaseEl()));
        result.setRwaImpact((result.getScenarioRwa() - result.getBaseRwa()) / Math.abs(result.getBaseRwa()));
        result.setLcrImpact(result.getScenarioLcr() - result.getBaseLcr());
        result.setNsfrImpact(result.getScenarioNsfr() - result.getBaseNsfr());

        // Determine overall severity
        result.setOverallSeverity(determineOverallSeverity(result));
    }

    private double calculateEconomicValueOfEquity(List<Asset> assets, List<Liability> liabilities, double yield) {
        double assetValue = assets.stream()
                .mapToDouble(asset -> asset.calculatePresentValue(yield))
                .sum();

        double liabilityValue = liabilities.stream()
                .mapToDouble(liability -> liability.calculatePresentValue(yield))
                .sum();

        return assetValue - liabilityValue;
    }

    private double calculateLCR(List<Asset> assets, List<Liability> liabilities) {
        // Simplified LCR calculation
        double hqla = assets.stream()
                .filter(asset -> asset.getType().equals("cash") || asset.getType().equals("government_bond"))
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        double netOutflows = liabilities.stream()
                .filter(liability -> liability.getDaysToMaturity() <= 30)
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        return netOutflows > 0 ? hqla / netOutflows : Double.POSITIVE_INFINITY;
    }

    private double calculateNSFR(List<Asset> assets, List<Liability> liabilities) {
        // Simplified NSFR calculation
        double asf = liabilities.stream()
                .filter(liability -> liability.getDaysToMaturity() > 365)
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        double rsf = assets.stream()
                .filter(asset -> asset.getDaysToMaturity() > 365)
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        return rsf > 0 ? asf / rsf : Double.POSITIVE_INFINITY;
    }

    private double getAverageRateChange(Scenario scenario) {
        // Calculate average rate change across all currencies
        Collection<Double> rateChanges = scenario.getRateChanges().values();
        return rateChanges.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private String determineOverallSeverity(ScenarioAnalysisResult result) {
        // Comprehensive severity assessment
        int severityScore = 0;

        if (result.getNiiImpact() < -0.20) severityScore += 3;
        else if (result.getNiiImpact() < -0.10) severityScore += 2;
        else if (result.getNiiImpact() < -0.05) severityScore += 1;

        if (result.getEveImpact() < -0.15) severityScore += 3;
        else if (result.getEveImpact() < -0.08) severityScore += 2;
        else if (result.getEveImpact() < -0.04) severityScore += 1;

        if (result.getElImpact() > 0.50) severityScore += 3;
        else if (result.getElImpact() > 0.25) severityScore += 2;
        else if (result.getElImpact() > 0.10) severityScore += 1;

        if (result.getLcrImpact() < -0.20) severityScore += 2;
        if (result.getNsfrImpact() < -0.15) severityScore += 2;

        if (severityScore >= 8) return "SEVERE";
        if (severityScore >= 5) return "HIGH";
        if (severityScore >= 3) return "MODERATE";
        return "LOW";
    }

    /**
     * Run multiple scenarios for comparative analysis
     */
    public Map<String, ScenarioAnalysisResult> runStressTesting(List<Scenario> scenarios,
                                                                List<Asset> assets,
                                                                List<Liability> liabilities,
                                                                List<Loan> loans) {
        return scenarios.stream()
                .collect(Collectors.toMap(
                        Scenario::getName,
                        scenario -> analyzeScenario(scenario, assets, liabilities, loans)
                ));
    }

    /**
     * Identify most vulnerable scenarios
     */
    public List<ScenarioAnalysisResult> getMostSevereScenarios(Map<String, ScenarioAnalysisResult> results, int limit) {
        return results.values().stream()
                .sorted((r1, r2) -> compareSeverity(r2.getOverallSeverity(), r1.getOverallSeverity()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private int compareSeverity(String severity1, String severity2) {
        Map<String, Integer> severityRank = Map.of(
                "SEVERE", 4, "HIGH", 3, "MODERATE", 2, "LOW", 1
        );
        return Integer.compare(severityRank.get(severity1), severityRank.get(severity2));
    }

    // Data classes for comprehensive results
    public static class ScenarioAnalysisResult {
        private final Scenario scenario;

        // Baseline metrics
        private double baseNii;
        private double baseDuration;
        private double baseEve;
        private double baseEl;
        private double baseRwa;
        private double baseLcr;
        private double baseNsfr;

        // Shocked metrics
        private double scenarioNii;
        private double scenarioDuration;
        private double scenarioEve;
        private double scenarioEl;
        private double scenarioRwa;
        private double scenarioLcr;
        private double scenarioNsfr;

        // Impacts
        private double niiImpact;
        private double durationImpact;
        private double eveImpact;
        private double elImpact;
        private double rwaImpact;
        private double lcrImpact;
        private double nsfrImpact;

        private String overallSeverity;

        public ScenarioAnalysisResult(Scenario scenario) {
            this.scenario = scenario;
        }

        public double getBaseDuration() {
            return baseDuration;
        }

        public void setBaseDuration(double baseDuration) {
            this.baseDuration = baseDuration;
        }

        public double getBaseEve() {
            return baseEve;
        }

        public void setBaseEve(double baseEve) {
            this.baseEve = baseEve;
        }

        public double getBaseEl() {
            return baseEl;
        }

        public void setBaseEl(double baseEl) {
            this.baseEl = baseEl;
        }

        public double getBaseRwa() {
            return baseRwa;
        }

        public void setBaseRwa(double baseRwa) {
            this.baseRwa = baseRwa;
        }

        public double getBaseLcr() {
            return baseLcr;
        }

        public void setBaseLcr(double baseLcr) {
            this.baseLcr = baseLcr;
        }

        public double getBaseNsfr() {
            return baseNsfr;
        }

        public void setBaseNsfr(double baseNsfr) {
            this.baseNsfr = baseNsfr;
        }

        public double getScenarioDuration() {
            return scenarioDuration;
        }

        public void setScenarioDuration(double scenarioDuration) {
            this.scenarioDuration = scenarioDuration;
        }

        public double getScenarioEve() {
            return scenarioEve;
        }

        public void setScenarioEve(double scenarioEve) {
            this.scenarioEve = scenarioEve;
        }

        public double getScenarioEl() {
            return scenarioEl;
        }

        public void setScenarioEl(double scenarioEl) {
            this.scenarioEl = scenarioEl;
        }

        public double getScenarioRwa() {
            return scenarioRwa;
        }

        public void setScenarioRwa(double scenarioRwa) {
            this.scenarioRwa = scenarioRwa;
        }

        public double getScenarioLcr() {
            return scenarioLcr;
        }

        public void setScenarioLcr(double scenarioLcr) {
            this.scenarioLcr = scenarioLcr;
        }

        public double getScenarioNsfr() {
            return scenarioNsfr;
        }

        public void setScenarioNsfr(double scenarioNsfr) {
            this.scenarioNsfr = scenarioNsfr;
        }

        public double getDurationImpact() {
            return durationImpact;
        }

        public void setDurationImpact(double durationImpact) {
            this.durationImpact = durationImpact;
        }

        public double getEveImpact() {
            return eveImpact;
        }

        public void setEveImpact(double eveImpact) {
            this.eveImpact = eveImpact;
        }

        public double getElImpact() {
            return elImpact;
        }

        public void setElImpact(double elImpact) {
            this.elImpact = elImpact;
        }

        public double getRwaImpact() {
            return rwaImpact;
        }

        public void setRwaImpact(double rwaImpact) {
            this.rwaImpact = rwaImpact;
        }

        public double getLcrImpact() {
            return lcrImpact;
        }

        public void setLcrImpact(double lcrImpact) {
            this.lcrImpact = lcrImpact;
        }

        public double getNsfrImpact() {
            return nsfrImpact;
        }

        public void setNsfrImpact(double nsfrImpact) {
            this.nsfrImpact = nsfrImpact;
        }

        public String getOverallSeverity() {
            return overallSeverity;
        }

        public void setOverallSeverity(String overallSeverity) {
            this.overallSeverity = overallSeverity;
        }

        // Getters and setters
        public Scenario getScenario() { return scenario; }
        public double getBaseNii() { return baseNii; } public void setBaseNii(double baseNii) { this.baseNii = baseNii; }
        public double getScenarioNii() { return scenarioNii; } public void setScenarioNii(double scenarioNii) { this.scenarioNii = scenarioNii; }
        public double getNiiImpact() { return niiImpact; } public void setNiiImpact(double niiImpact) { this.niiImpact = niiImpact; }
        // ... similar for all other fields
    }
}