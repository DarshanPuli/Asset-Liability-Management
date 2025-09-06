package org.example.service;


import org.example.model.Asset;
import org.example.model.Liability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.model.FinancialInstrument;
import java.util.*;

public class MatchingService {
    private final InterestRateRiskService interestRateRiskService;

    public MatchingService() {
        this.interestRateRiskService = new InterestRateRiskService();
    }

    /**
     * Checks if there's a significant duration mismatch
     */
    public boolean hasDurationMismatch(List<Asset> assets, List<Liability> liabilities, double tolerance) {
        double durationGap = calculateDurationGap(assets, liabilities);
        return Math.abs(durationGap) > tolerance;
    }

    /**
     * Calculates the duration gap between assets and liabilities
     */
    public double calculateDurationGap(List<Asset> assets, List<Liability> liabilities) {
        double assetDuration = calculateWeightedDuration(assets, 0.05); // Using 5% yield
        double liabilityDuration = calculateWeightedDuration(liabilities, 0.05);

        double totalAssets = calculateTotalValue(assets);
        double totalLiabilities = calculateTotalValue(liabilities);

        return assetDuration - (liabilityDuration * (totalLiabilities / totalAssets));
    }

    /**
     * Calculates weighted average duration
     */
    private double calculateWeightedDuration(List<? extends FinancialInstrument> instruments, double yield) {
        double totalValue = calculateTotalValue(instruments);
        if (totalValue == 0) return 0;

        double weightedDuration = 0;
        for (FinancialInstrument instrument : instruments) {
            double weight = instrument.getPrincipalAmount() / totalValue;
            weightedDuration += weight * instrument.calculateDuration(yield);
        }
        return weightedDuration;
    }

    /**
     * Calculates total value of instruments
     */
    private double calculateTotalValue(List<? extends FinancialInstrument> instruments) {
        return instruments.stream()
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }

    /**
     * Calculates maturity matching score (0-100%)
     */
    public double calculateMaturityMatch(List<Asset> assets, List<Liability> liabilities) {
        // Group by maturity buckets and calculate matching
        Map<String, Double> assetMaturity = groupByMaturityBuckets(assets);
        Map<String, Double> liabilityMaturity = groupByMaturityBuckets(liabilities);

        double totalMatched = 0;
        double totalValue = 0;

        // Check all maturity buckets
        Set<String> allBuckets = new HashSet<>();
        allBuckets.addAll(assetMaturity.keySet());
        allBuckets.addAll(liabilityMaturity.keySet());

        for (String bucket : allBuckets) {
            double assetAmount = assetMaturity.getOrDefault(bucket, 0.0);
            double liabilityAmount = liabilityMaturity.getOrDefault(bucket, 0.0);

            totalMatched += Math.min(assetAmount, liabilityAmount);
            totalValue += Math.max(assetAmount, liabilityAmount);
        }

        return totalValue > 0 ? (totalMatched / totalValue) * 100 : 100;
    }

    /**
     * Groups instruments by maturity buckets
     */
    private Map<String, Double> groupByMaturityBuckets(List<? extends FinancialInstrument> instruments) {
        Map<String, Double> buckets = new HashMap<>();

        for (FinancialInstrument instrument : instruments) {
            String bucket = getMaturityBucket(instrument.getDaysToMaturity());
            double current = buckets.getOrDefault(bucket, 0.0);
            buckets.put(bucket, current + instrument.getPrincipalAmount());
        }

        return buckets;
    }

    /**
     * Categorizes instruments into maturity buckets
     */
    private String getMaturityBucket(long daysToMaturity) {
        if (daysToMaturity <= 30) return "0-30 days";
        else if (daysToMaturity <= 90) return "31-90 days";
        else if (daysToMaturity <= 365) return "91-365 days";
        else if (daysToMaturity <= 1095) return "1-3 years";
        else return "3+ years";
    }

    /**
     * Checks for currency mismatch
     */
    public boolean hasCurrencyMismatch(List<Asset> assets, List<Liability> liabilities) {
        Set<String> assetCurrencies = assets.stream()
                .map(Asset::getCurrency)
                .collect(Collectors.toSet());

        Set<String> liabilityCurrencies = liabilities.stream()
                .map(Liability::getCurrency)
                .collect(Collectors.toSet());

        return !assetCurrencies.equals(liabilityCurrencies);
    }

    /**
     * Calculates currency matching score
     */
    public double calculateCurrencyMatch(List<Asset> assets, List<Liability> liabilities) {
        Map<String, Double> assetCurrencies = new HashMap<>();
        Map<String, Double> liabilityCurrencies = new HashMap<>();

        // Sum amounts by currency for assets
        for (Asset asset : assets) {
            String currency = asset.getCurrency();
            assetCurrencies.put(currency, assetCurrencies.getOrDefault(currency, 0.0) + asset.getPrincipalAmount());
        }

        // Sum amounts by currency for liabilities
        for (Liability liability : liabilities) {
            String currency = liability.getCurrency();
            liabilityCurrencies.put(currency, liabilityCurrencies.getOrDefault(currency, 0.0) + liability.getPrincipalAmount());
        }

        double totalMatched = 0;
        double totalValue = 0;

        // Check all currencies
        Set<String> allCurrencies = new HashSet<>();
        allCurrencies.addAll(assetCurrencies.keySet());
        allCurrencies.addAll(liabilityCurrencies.keySet());

        for (String currency : allCurrencies) {
            double assetAmount = assetCurrencies.getOrDefault(currency, 0.0);
            double liabilityAmount = liabilityCurrencies.getOrDefault(currency, 0.0);

            totalMatched += Math.min(assetAmount, liabilityAmount);
            totalValue += Math.max(assetAmount, liabilityAmount);
        }

        return totalValue > 0 ? (totalMatched / totalValue) * 100 : 100;
    }

    /**
     * Gets matching suggestions based on analysis
     */
    public List<String> getMatchingSuggestions(List<Asset> assets, List<Liability> liabilities) {
        List<String> suggestions = new ArrayList<>();

        // Check duration mismatch
        if (hasDurationMismatch(assets, liabilities, 0.5)) { // 0.5 year tolerance
            double gap = calculateDurationGap(assets, liabilities);
            if (gap > 0) {
                suggestions.add("Duration gap: " + String.format("%.2f", gap) +
                        " years. Consider adding shorter-term assets or longer-term liabilities.");
            } else {
                suggestions.add("Duration gap: " + String.format("%.2f", gap) +
                        " years. Consider adding longer-term assets or shorter-term liabilities.");
            }
        }

        // Check currency mismatch
        if (hasCurrencyMismatch(assets, liabilities)) {
            double currencyMatch = calculateCurrencyMatch(assets, liabilities);
            suggestions.add("Currency matching at " + String.format("%.1f", currencyMatch) +
                    "%. Consider hedging foreign exchange exposure.");
        }

        // Check maturity mismatch
        double maturityMatch = calculateMaturityMatch(assets, liabilities);
        if (maturityMatch < 80) { // Less than 80% matched
            suggestions.add("Maturity matching at " + String.format("%.1f", maturityMatch) +
                    "%. Review cash flow timing to avoid liquidity gaps.");
        }

        // Check interest rate type mismatch
        if (hasRateTypeMismatch(assets, liabilities)) {
            suggestions.add("Fixed/variable rate mismatch. Consider interest rate swaps.");
        }

        if (suggestions.isEmpty()) {
            suggestions.add("Portfolio is well-matched. No immediate actions needed.");
        }

        return suggestions;
    }

    /**
     * Checks for fixed/variable rate mismatch
     */
    public boolean hasRateTypeMismatch(List<Asset> assets, List<Liability> liabilities) {
        long fixedAssets = assets.stream().filter(Asset::isFixedRate).count();
        long variableAssets = assets.stream().filter(a -> !a.isFixedRate()).count();

        long fixedLiabilities = liabilities.stream().filter(Liability::isFixedRate).count();
        long variableLiabilities = liabilities.stream().filter(l -> !l.isFixedRate()).count();

        // If we have significant amounts of both fixed and variable on both sides,
        // but they're not proportionally matched
        return (fixedAssets > 0 && variableAssets > 0 &&
                fixedLiabilities > 0 && variableLiabilities > 0 &&
                Math.abs((double)fixedAssets/assets.size() - (double)fixedLiabilities/liabilities.size()) > 0.3);
    }

    /**
     * Checks if assets and liabilities are duration-matched within tolerance
     */
    public boolean isDurationMatched(List<Asset> assets, List<Liability> liabilities, double yield, double tolerance) {
        double durationGap = calculateDurationGap(assets, liabilities);
        return Math.abs(durationGap) <= tolerance;
    }

    /**
     * Finds the optimal way to match assets and liabilities based on multiple criteria
     */
    public MatchingResult findOptimalMatching(List<Asset> assets, List<Liability> liabilities) {
        MatchingResult result = new MatchingResult();

        // Calculate currency match score (0-1)
        Set<String> assetCurrencies = assets.stream().map(Asset::getCurrency).collect(Collectors.toSet());
        Set<String> liabilityCurrencies = liabilities.stream().map(Liability::getCurrency).collect(Collectors.toSet());
        double currencyMatchScore = calculateCurrencyMatchScore(assetCurrencies, liabilityCurrencies);
        result.setCurrencyMatchScore(currencyMatchScore);

        // Calculate duration match score (0-1)
        double durationGap = Math.abs(calculateDurationGap(assets, liabilities));
        double durationMatchScore = Math.max(0, 1 - durationGap/2); // Gap of 2 or more gives score of 0
        result.setDurationMatchScore(durationMatchScore);

        // Calculate maturity match score (0-1)
        double maturityMatchScore = calculateMaturityMatch(assets, liabilities) / 100.0;
        result.setMaturityMatchScore(maturityMatchScore);

        // Set overall assessment
        String assessment = generateOverallAssessment(currencyMatchScore, durationMatchScore, maturityMatchScore);
        result.setOverallAssessment(assessment);

        return result;
    }

    private double calculateCurrencyMatchScore(Set<String> assetCurrencies, Set<String> liabilityCurrencies) {
        if (assetCurrencies.isEmpty() || liabilityCurrencies.isEmpty()) return 0;

        Set<String> commonCurrencies = new HashSet<>(assetCurrencies);
        commonCurrencies.retainAll(liabilityCurrencies);

        int totalUniqueCurrencies = new HashSet<String>() {{
            addAll(assetCurrencies);
            addAll(liabilityCurrencies);
        }}.size();

        return (double) commonCurrencies.size() / totalUniqueCurrencies;
    }

    private String generateOverallAssessment(double currencyScore, double durationScore, double maturityScore) {
        double overallScore = (currencyScore + durationScore + maturityScore) / 3;

        if (overallScore >= 0.8) return "Excellent match";
        else if (overallScore >= 0.6) return "Good match";
        else if (overallScore >= 0.4) return "Fair match";
        else return "Poor match - significant mismatches exist";
    }

    /**
     * Comprehensive matching analysis result
     */
    public static class MatchingResult {
        private double currencyMatchScore;
        private double durationMatchScore;
        private double maturityMatchScore;
        private double rateTypeMatchScore;
        private String overallAssessment;

        // Getters and setters
        public double getCurrencyMatchScore() { return currencyMatchScore; }
        public void setCurrencyMatchScore(double score) { this.currencyMatchScore = score; }

        public double getDurationMatchScore() { return durationMatchScore; }
        public void setDurationMatchScore(double score) { this.durationMatchScore = score; }

        public double getMaturityMatchScore() { return maturityMatchScore; }
        public void setMaturityMatchScore(double score) { this.maturityMatchScore = score; }

        public double getRateTypeMatchScore() { return rateTypeMatchScore; }
        public void setRateTypeMatchScore(double score) { this.rateTypeMatchScore = score; }

        public String getOverallAssessment() { return overallAssessment; }
        public void setOverallAssessment(String assessment) { this.overallAssessment = assessment; }
    }

    /**
     * Comprehensive matching analysis
     */
    public MatchingResult analyzeMatching(List<Asset> assets, List<Liability> liabilities) {
        MatchingResult result = new MatchingResult();

        result.setCurrencyMatchScore(calculateCurrencyMatch(assets, liabilities));
        result.setMaturityMatchScore(calculateMaturityMatch(assets, liabilities));

        // For duration, we use a score based on the gap
        double durationGap = Math.abs(calculateDurationGap(assets, liabilities));
        result.setDurationMatchScore(Math.max(0, 100 - (durationGap * 20))); // Convert gap to score

        // Rate type matching (simplified)
        double fixedAssetRatio = (double) assets.stream().filter(Asset::isFixedRate).count() / assets.size();
        double fixedLiabilityRatio = (double) liabilities.stream().filter(Liability::isFixedRate).count() / liabilities.size();
        result.setRateTypeMatchScore(100 - (Math.abs(fixedAssetRatio - fixedLiabilityRatio) * 100));

        // Overall assessment
        double overallScore = (result.getCurrencyMatchScore() + result.getDurationMatchScore() +
                result.getMaturityMatchScore() + result.getRateTypeMatchScore()) / 4;

        if (overallScore >= 85) {
            result.setOverallAssessment("EXCELLENT - Well matched portfolio");
        } else if (overallScore >= 70) {
            result.setOverallAssessment("GOOD - Minor mismatches detected");
        } else if (overallScore >= 50) {
            result.setOverallAssessment("FAIR - Some significant mismatches");
        } else {
            result.setOverallAssessment("POOR - Major mismatches require attention");
        }

        return result;
    }
}