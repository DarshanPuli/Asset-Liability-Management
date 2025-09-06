package org.example.service;

// src/com/alm/service/ScenarioAnalysisService.java

import org.example.model.Asset;
import org.example.model.FinancialInstrument;
import org.example.model.Liability;
import org.example.model.Scenario;

import java.util.List;

public class ScenarioAnalysisService {
    private final InterestRateRiskService interestRateRiskService;

    public ScenarioAnalysisService() {
        this.interestRateRiskService = new InterestRateRiskService();
    }

    public ScenarioResult analyzeScenario(Scenario scenario, List<Asset> assets, List<Liability> liabilities) {
        ScenarioResult result = new ScenarioResult(scenario);

        // Apply rate changes
        List<Asset> adjustedAssets = applyRateChanges(assets, scenario);
        List<Liability> adjustedLiabilities = applyRateChanges(liabilities, scenario);

        // Calculate metrics under scenario
        result.setBaseNii(interestRateRiskService.calculateNetInterestIncome(assets, liabilities));
        result.setScenarioNii(interestRateRiskService.calculateNetInterestIncome(adjustedAssets, adjustedLiabilities));

        result.setBaseDuration(interestRateRiskService.calculatePortfolioDuration(assets, liabilities, 0.05));
        result.setScenarioDuration(interestRateRiskService.calculatePortfolioDuration(adjustedAssets, adjustedLiabilities, 0.05));

        return result;
    }

    private <T extends FinancialInstrument> List<T> applyRateChanges(List<T> instruments, Scenario scenario) {
        return instruments.stream()
                .map(instrument -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T copy = (T) instrument.getClass()
                                .getConstructor(instrument.getClass())
                                .newInstance(instrument);

                        double rateChange = scenario.getRateChangeForCurrency(instrument.getCurrency());
                        if (!copy.isFixedRate()) {
                            copy.setInterestRate(copy.getInterestRate() + rateChange);
                        }

                        return copy;
                    } catch (Exception e) {
                        // If we can't clone, return original
                        return instrument;
                    }
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public static class ScenarioResult {
        private final Scenario scenario;
        private double baseNii;
        private double scenarioNii;
        private double baseDuration;
        private double scenarioDuration;

        public ScenarioResult(Scenario scenario) {
            this.scenario = scenario;
        }

        public double getNiiChange() { return scenarioNii - baseNii; }
        public double getDurationChange() { return scenarioDuration - baseDuration; }

        // Getters and setters
        public Scenario getScenario() { return scenario; }
        public double getBaseNii() { return baseNii; }
        public void setBaseNii(double baseNii) { this.baseNii = baseNii; }
        public double getScenarioNii() { return scenarioNii; }
        public void setScenarioNii(double scenarioNii) { this.scenarioNii = scenarioNii; }
        public double getBaseDuration() { return baseDuration; }
        public void setBaseDuration(double baseDuration) { this.baseDuration = baseDuration; }
        public double getScenarioDuration() { return scenarioDuration; }
        public void setScenarioDuration(double scenarioDuration) { this.scenarioDuration = scenarioDuration; }

        @Override
        public String toString() {
            return String.format("Scenario: %s\nNII Change: %.2f\nDuration Change: %.2f",
                    scenario.getName(), getNiiChange(), getDurationChange());
        }
    }
}