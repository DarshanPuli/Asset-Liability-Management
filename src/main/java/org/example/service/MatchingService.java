package org.example.service;


import org.example.model.Asset;
import org.example.model.Liability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.model.FinancialInstrument;
import java.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class MatchingService {
    private final InterestRateRiskService interestRateRiskService;

    public MatchingService() {
        this.interestRateRiskService = new InterestRateRiskService();
    }

    // Regulatory thresholds
    private static final double DURATION_MISMATCH_THRESHOLD = 0.5; // years
    private static final double CURRENCY_MISMATCH_THRESHOLD = 80.0; // percentage
    private static final double MATURITY_MISMATCH_THRESHOLD = 80.0; // percentage
    private static final double RATE_TYPE_MISMATCH_THRESHOLD = 0.3; // 30% difference

    /**
     * Comprehensive ALM analysis with regulatory compliance
     */
    public ALMAnalysis analyzeALM(List<Asset> assets, List<Liability> liabilities, double yield) {
        DurationMatchResult durationMatch = analyzeDurationMatch(assets, liabilities, yield);
        CurrencyMatchResult currencyMatch = analyzeCurrencyMatch(assets, liabilities);
        MaturityMatchResult maturityMatch = analyzeMaturityMatch(assets, liabilities);
        RateTypeMatchResult rateTypeMatch = analyzeRateTypeMatch(assets, liabilities);

        double overallScore = calculateOverallScore(durationMatch, currencyMatch, maturityMatch, rateTypeMatch);
        String overallStatus = determineOverallStatus(overallScore);

        return new ALMAnalysis(durationMatch, currencyMatch, maturityMatch, rateTypeMatch,
                overallScore, overallStatus, generateRecommendations(durationMatch, currencyMatch, maturityMatch, rateTypeMatch));
    }

    /**
     * Enhanced duration gap analysis
     */
    public DurationMatchResult analyzeDurationMatch(List<Asset> assets, List<Liability> liabilities, double yield) {
        double assetDuration = calculateWeightedDuration(assets, yield);
        double liabilityDuration = calculateWeightedDuration(liabilities, yield);

        double totalAssets = calculateTotalMarketValue(assets, yield);
        double totalLiabilities = calculateTotalMarketValue(liabilities, yield);

        double durationGap = assetDuration - (liabilityDuration * (totalLiabilities / totalAssets));
        double leverageAdjustedGap = durationGap * (totalAssets / (totalAssets - totalLiabilities));

        String status = Math.abs(durationGap) > DURATION_MISMATCH_THRESHOLD ? "MISMATCHED" : "MATCHED";
        double matchScore = calculateDurationMatchScore(durationGap);

        return new DurationMatchResult(durationGap, leverageAdjustedGap, assetDuration,
                liabilityDuration, totalAssets, totalLiabilities, status, matchScore);
    }

    private double calculateWeightedDuration(List<? extends FinancialInstrument> instruments, double yield) {
        double totalMarketValue = calculateTotalMarketValue(instruments, yield);
        if (totalMarketValue == 0) return 0;

        return instruments.stream()
                .mapToDouble(instrument -> {
                    double marketValue = calculateMarketValue(instrument, yield);
                    double duration = instrument.calculateDuration(yield);
                    return (marketValue / totalMarketValue) * duration;
                })
                .sum();
    }

    private double calculateTotalMarketValue(List<? extends FinancialInstrument> instruments, double yield) {
        return instruments.stream()
                .mapToDouble(instrument -> calculateMarketValue(instrument, yield))
                .sum();
    }

    private double calculateMarketValue(FinancialInstrument instrument, double yield) {
        return instrument.calculatePresentValue(yield);
    }

    private double calculateDurationMatchScore(double durationGap) {
        return Math.max(0, 100 - (Math.abs(durationGap) * 20)); // 1 year gap = 80% score
    }

    /**
     * Enhanced currency matching analysis
     */
    public CurrencyMatchResult analyzeCurrencyMatch(List<Asset> assets, List<Liability> liabilities) {
        Map<String, CurrencyExposure> exposures = new HashMap<>();

        // Calculate exposures by currency
        calculateCurrencyExposures(assets, liabilities, exposures);

        double matchPercentage = calculateCurrencyMatchPercentage(exposures);
        String status = matchPercentage < CURRENCY_MISMATCH_THRESHOLD ? "MISMATCHED" : "MATCHED";

        return new CurrencyMatchResult(exposures, matchPercentage, status);
    }

    private void calculateCurrencyExposures(List<Asset> assets, List<Liability> liabilities,
                                            Map<String, CurrencyExposure> exposures) {
        // Asset exposures
        for (Asset asset : assets) {
            String currency = asset.getCurrency();
            CurrencyExposure exposure = exposures.getOrDefault(currency, new CurrencyExposure());
            exposure.addAssets(asset.getPrincipalAmount());
            exposures.put(currency, exposure);
        }

        // Liability exposures
        for (Liability liability : liabilities) {
            String currency = liability.getCurrency();
            CurrencyExposure exposure = exposures.getOrDefault(currency, new CurrencyExposure());
            exposure.addLiabilities(liability.getPrincipalAmount());
            exposures.put(currency, exposure);
        }

        // Calculate net exposures
        exposures.values().forEach(CurrencyExposure::calculateNetExposure);
    }

    private double calculateCurrencyMatchPercentage(Map<String, CurrencyExposure> exposures) {
        double totalMatched = 0;
        double totalExposure = 0;

        for (CurrencyExposure exposure : exposures.values()) {
            totalMatched += Math.min(exposure.getAssets(), exposure.getLiabilities());
            totalExposure += Math.max(exposure.getAssets(), exposure.getLiabilities());
        }

        return totalExposure > 0 ? (totalMatched / totalExposure) * 100 : 100;
    }

    /**
     * Enhanced maturity matching analysis
     */
    public MaturityMatchResult analyzeMaturityMatch(List<Asset> assets, List<Liability> liabilities) {
        Map<String, MaturityBucket> assetBuckets = groupByMaturityBuckets(assets);
        Map<String, MaturityBucket> liabilityBuckets = groupByMaturityBuckets(liabilities);

        double matchPercentage = calculateMaturityMatchPercentage(assetBuckets, liabilityBuckets);
        String status = matchPercentage < MATURITY_MISMATCH_THRESHOLD ? "MISMATCHED" : "MATCHED";

        return new MaturityMatchResult(assetBuckets, liabilityBuckets, matchPercentage, status);
    }

    private Map<String, MaturityBucket> groupByMaturityBuckets(List<? extends FinancialInstrument> instruments) {
        Map<String, MaturityBucket> buckets = new LinkedHashMap<>();
        String[] bucketNames = {"0-30 days", "31-90 days", "91-365 days", "1-3 years", "3+ years"};
        int[][] dayRanges = {{0, 30}, {31, 90}, {91, 365}, {366, 1095}, {1096, Integer.MAX_VALUE}};

        for (int i = 0; i < bucketNames.length; i++) {
            buckets.put(bucketNames[i], new MaturityBucket(bucketNames[i]));
        }

        for (FinancialInstrument instrument : instruments) {
            long days = instrument.getDaysToMaturity();
            String bucket = getMaturityBucket(days);
            buckets.get(bucket).addAmount(instrument.getPrincipalAmount());
        }

        return buckets;
    }

    private double calculateMaturityMatchPercentage(Map<String, MaturityBucket> assetBuckets,
                                                    Map<String, MaturityBucket> liabilityBuckets) {
        double totalMatched = 0;
        double totalExposure = 0;

        for (String bucket : assetBuckets.keySet()) {
            double assetAmount = assetBuckets.get(bucket).getAmount();
            double liabilityAmount = liabilityBuckets.getOrDefault(bucket, new MaturityBucket(bucket)).getAmount();

            totalMatched += Math.min(assetAmount, liabilityAmount);
            totalExposure += Math.max(assetAmount, liabilityAmount);
        }

        return totalExposure > 0 ? (totalMatched / totalExposure) * 100 : 100;
    }

    /**
     * Rate type matching analysis
     */
    public RateTypeMatchResult analyzeRateTypeMatch(List<Asset> assets, List<Liability> liabilities) {
        double fixedAssets = calculateFixedAmount(assets);
        double variableAssets = calculateTotalValue(assets) - fixedAssets;
        double fixedLiabilities = calculateFixedAmount(liabilities);
        double variableLiabilities = calculateTotalValue(liabilities) - fixedLiabilities;

        double fixedRatioAssets = calculateTotalValue(assets) > 0 ? fixedAssets / calculateTotalValue(assets) : 0;
        double fixedRatioLiabilities = calculateTotalValue(liabilities) > 0 ? fixedLiabilities / calculateTotalValue(liabilities) : 0;

        double mismatch = Math.abs(fixedRatioAssets - fixedRatioLiabilities);
        String status = mismatch > RATE_TYPE_MISMATCH_THRESHOLD ? "MISMATCHED" : "MATCHED";
        double matchScore = 100 - (mismatch * 100);

        return new RateTypeMatchResult(fixedAssets, variableAssets, fixedLiabilities, variableLiabilities,
                fixedRatioAssets, fixedRatioLiabilities, mismatch, status, matchScore);
    }

    private double calculateFixedAmount(List<? extends FinancialInstrument> instruments) {
        return instruments.stream()
                .filter(FinancialInstrument::isFixedRate)
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }

    private double calculateTotalValue(List<? extends FinancialInstrument> instruments) {
        return instruments.stream()
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }

    private double calculateOverallScore(DurationMatchResult duration, CurrencyMatchResult currency,
                                         MaturityMatchResult maturity, RateTypeMatchResult rateType) {
        return (duration.getMatchScore() + currency.getMatchPercentage() +
                maturity.getMatchPercentage() + rateType.getMatchScore()) / 4;
    }

    private String determineOverallStatus(double overallScore) {
        if (overallScore >= 85) return "EXCELLENT";
        if (overallScore >= 70) return "GOOD";
        if (overallScore >= 50) return "FAIR";
        return "POOR";
    }

    private List<String> generateRecommendations(DurationMatchResult duration, CurrencyMatchResult currency,
                                                 MaturityMatchResult maturity, RateTypeMatchResult rateType) {
        List<String> recommendations = new ArrayList<>();

        if (duration.getStatus().equals("MISMATCHED")) {
            if (duration.getDurationGap() > 0) {
                recommendations.add("Reduce asset duration or increase liability duration. Gap: " +
                        String.format("%.2f years", duration.getDurationGap()));
            } else {
                recommendations.add("Increase asset duration or reduce liability duration. Gap: " +
                        String.format("%.2f years", duration.getDurationGap()));
            }
        }

        if (currency.getMatchPercentage() < CURRENCY_MISMATCH_THRESHOLD) {
            recommendations.add("Hedge currency exposure. Current match: " +
                    String.format("%.1f%%", currency.getMatchPercentage()));
        }

        if (maturity.getMatchPercentage() < MATURITY_MISMATCH_THRESHOLD) {
            recommendations.add("Improve maturity matching. Current match: " +
                    String.format("%.1f%%", maturity.getMatchPercentage()));
        }

        if (rateType.getStatus().equals("MISMATCHED")) {
            recommendations.add("Balance fixed/variable rate mix. Mismatch: " +
                    String.format("%.1f%%", rateType.getMismatch() * 100));
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Portfolio is well-matched. Maintain current strategy.");
        }

        return recommendations;
    }

    // Data classes for comprehensive results
    public static class ALMAnalysis {
        private final DurationMatchResult durationMatch;
        private final CurrencyMatchResult currencyMatch;
        private final MaturityMatchResult maturityMatch;
        private final RateTypeMatchResult rateTypeMatch;
        private final double overallScore;
        private final String overallStatus;
        private final List<String> recommendations;

        public ALMAnalysis(DurationMatchResult durationMatch, CurrencyMatchResult currencyMatch,
                           MaturityMatchResult maturityMatch, RateTypeMatchResult rateTypeMatch,
                           double overallScore, String overallStatus, List<String> recommendations) {
            this.durationMatch = durationMatch;
            this.currencyMatch = currencyMatch;
            this.maturityMatch = maturityMatch;
            this.rateTypeMatch = rateTypeMatch;
            this.overallScore = overallScore;
            this.overallStatus = overallStatus;
            this.recommendations = recommendations;
        }
        // Getters

        public DurationMatchResult getDurationMatch() {
            return durationMatch;
        }

        public CurrencyMatchResult getCurrencyMatch() {
            return currencyMatch;
        }

        public MaturityMatchResult getMaturityMatch() {
            return maturityMatch;
        }

        public RateTypeMatchResult getRateTypeMatch() {
            return rateTypeMatch;
        }

        public double getOverallScore() {
            return overallScore;
        }

        public String getOverallStatus() {
            return overallStatus;
        }

        public List<String> getRecommendations() {
            return recommendations;
        }
    }

    public static class DurationMatchResult {
        private final double durationGap;
        private final double leverageAdjustedGap;
        private final double assetDuration;
        private final double liabilityDuration;
        private final double totalAssets;
        private final double totalLiabilities;
        private final String status;
        private final double matchScore;

        public DurationMatchResult(double durationGap, double leverageAdjustedGap, double assetDuration,
                                   double liabilityDuration, double totalAssets, double totalLiabilities,
                                   String status, double matchScore) {
            this.durationGap = durationGap;
            this.leverageAdjustedGap = leverageAdjustedGap;
            this.assetDuration = assetDuration;
            this.liabilityDuration = liabilityDuration;
            this.totalAssets = totalAssets;
            this.totalLiabilities = totalLiabilities;
            this.status = status;
            this.matchScore = matchScore;
        }

        public double getDurationGap() {
            return durationGap;
        }

        public double getLeverageAdjustedGap() {
            return leverageAdjustedGap;
        }

        public double getAssetDuration() {
            return assetDuration;
        }

        public double getLiabilityDuration() {
            return liabilityDuration;
        }

        public double getTotalAssets() {
            return totalAssets;
        }

        public double getTotalLiabilities() {
            return totalLiabilities;
        }

        public String getStatus() {
            return status;
        }

        public double getMatchScore() {
            return matchScore;
        }
        // Getters
    }

    public static class CurrencyMatchResult {
        private final Map<String, CurrencyExposure> exposures;
        private final double matchPercentage;
        private final String status;

        public CurrencyMatchResult(Map<String, CurrencyExposure> exposures, double matchPercentage, String status) {
            this.exposures = exposures;
            this.matchPercentage = matchPercentage;
            this.status = status;
        }

        public Map<String, CurrencyExposure> getExposures() {
            return exposures;
        }

        public double getMatchPercentage() {
            return matchPercentage;
        }

        public String getStatus() {
            return status;
        }
        // Getters
    }

    public static class MaturityMatchResult {
        private final Map<String, MaturityBucket> assetBuckets;
        private final Map<String, MaturityBucket> liabilityBuckets;
        private final double matchPercentage;
        private final String status;

        public MaturityMatchResult(Map<String, MaturityBucket> assetBuckets,
                                   Map<String, MaturityBucket> liabilityBuckets,
                                   double matchPercentage, String status) {
            this.assetBuckets = assetBuckets;
            this.liabilityBuckets = liabilityBuckets;
            this.matchPercentage = matchPercentage;
            this.status = status;
        }

        // Getters
        public Map<String, MaturityBucket> getAssetBuckets() { return assetBuckets; }
        public Map<String, MaturityBucket> getLiabilityBuckets() { return liabilityBuckets; }
        public double getMatchPercentage() { return matchPercentage; }
        public String getStatus() { return status; }
    }


    public static class CurrencyExposure {
        private double assets;
        private double liabilities;
        private double netExposure;

        public void addAssets(double amount) { this.assets += amount; }
        public void addLiabilities(double amount) { this.liabilities += amount; }
        public void calculateNetExposure() { this.netExposure = assets - liabilities; }

        public double getAssets() {
            return assets;
        }

        public double getLiabilities() {
            return liabilities;
        }

        public double getNetExposure() {
            return netExposure;
        }
        // Getters
    }

    public static class MaturityBucketResult {
        private final Map<String, MaturityBucket> assetBuckets;
        private final Map<String, MaturityBucket> liabilityBuckets;
        private final double matchPercentage;
        private final String status;

        public MaturityBucketResult(Map<String, MaturityBucket> assetBuckets,
                                    Map<String, MaturityBucket> liabilityBuckets,
                                    double matchPercentage, String status) {
            this.assetBuckets = assetBuckets;
            this.liabilityBuckets = liabilityBuckets;
            this.matchPercentage = matchPercentage;
            this.status = status;
        }

        public Map<String, MaturityBucket> getAssetBuckets() {
            return assetBuckets;
        }

        public Map<String, MaturityBucket> getLiabilityBuckets() {
            return liabilityBuckets;
        }

        public double getMatchPercentage() {
            return matchPercentage;
        }

        public String getStatus() {
            return status;
        }
        // Getters

    }

    public static class MaturityBucket {
        private final String bucketName;
        private double amount;

        public MaturityBucket(String bucketName) {
            this.bucketName = bucketName;
            this.amount = 0;
        }

        public void addAmount(double amount) { this.amount += amount; }

        public String getBucketName() {
            return bucketName;
        }

        public double getAmount() {
            return amount;
        }
        // Getters
    }

    public static class RateTypeMatchResult {
        private final double fixedAssets;
        private final double variableAssets;
        private final double fixedLiabilities;
        private final double variableLiabilities;
        private final double fixedRatioAssets;
        private final double fixedRatioLiabilities;
        private final double mismatch;
        private final String status;
        private final double matchScore;

        public RateTypeMatchResult(double fixedAssets, double variableAssets, double fixedLiabilities,
                                   double variableLiabilities, double fixedRatioAssets, double fixedRatioLiabilities,
                                   double mismatch, String status, double matchScore) {
            this.fixedAssets = fixedAssets;
            this.variableAssets = variableAssets;
            this.fixedLiabilities = fixedLiabilities;
            this.variableLiabilities = variableLiabilities;
            this.fixedRatioAssets = fixedRatioAssets;
            this.fixedRatioLiabilities = fixedRatioLiabilities;
            this.mismatch = mismatch;
            this.status = status;
            this.matchScore = matchScore;
        }
        // Getters

        public double getFixedAssets() {
            return fixedAssets;
        }

        public double getVariableAssets() {
            return variableAssets;
        }

        public double getFixedLiabilities() {
            return fixedLiabilities;
        }

        public double getVariableLiabilities() {
            return variableLiabilities;
        }

        public double getFixedRatioAssets() {
            return fixedRatioAssets;
        }

        public double getFixedRatioLiabilities() {
            return fixedRatioLiabilities;
        }

        public double getMismatch() {
            return mismatch;
        }

        public String getStatus() {
            return status;
        }

        public double getMatchScore() {
            return matchScore;
        }
    }

    private String getMaturityBucket(long daysToMaturity) {
        if (daysToMaturity <= 30) return "0-30 days";
        else if (daysToMaturity <= 90) return "31-90 days";
        else if (daysToMaturity <= 365) return "91-365 days";
        else if (daysToMaturity <= 1095) return "1-3 years";
        else return "3+ years";
    }
}