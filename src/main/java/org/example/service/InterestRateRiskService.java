package org.example.service;

// src/com/alm/service/InterestRateRiskService.java

import org.example.model.Asset;
import org.example.model.FinancialInstrument;
import org.example.model.Liability;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for measuring and analyzing interest rate risk exposure.
 * Provides key metrics for assessing how changes in interest rates affect
 * the institution's portfolio value and income.
 *
 * Used by: Risk management dashboard, regulatory reporting, ALM decision-making
 */
public class InterestRateRiskService {

    // Regulatory thresholds and parameters
    private static final double DURATION_GAP_WARNING_THRESHOLD = 0.5; // years
    private static final double NII_VOLATILITY_THRESHOLD = 0.20; // 20% change
    private static final double[] STANDARD_GAP_BUCKETS = {1, 7, 30, 90, 180, 365, 1095};

    /**
     * Enhanced portfolio duration gap calculation with regulatory compliance
     */
    public DurationGapResult calculatePortfolioDurationGap(List<Asset> assets, List<Liability> liabilities, double yield) {
        double assetDuration = calculateWeightedDuration(assets, yield);
        double liabilityDuration = calculateWeightedDuration(liabilities, yield);

        double assetValue = calculateTotalMarketValue(assets, yield);
        double liabilityValue = calculateTotalMarketValue(liabilities, yield);

        double durationGap = assetDuration - (liabilityValue / assetValue) * liabilityDuration;
        double leverageAdjustedGap = durationGap * (assetValue / (assetValue - liabilityValue));

        return new DurationGapResult(
                durationGap,
                leverageAdjustedGap,
                assetDuration,
                liabilityDuration,
                assetValue,
                liabilityValue,
                getGapStatus(durationGap)
        );
    }

    /**
     * Market value weighted duration calculation
     */
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

    /**
     * Enhanced market value calculation
     */
    private double calculateMarketValue(FinancialInstrument instrument, double yield) {
        if (instrument.getDaysToMaturity() <= 0) {
            return 0; // Matured instruments
        }
        return instrument.calculatePresentValue(yield);
    }

    private double calculateTotalMarketValue(List<? extends FinancialInstrument> instruments, double yield) {
        return instruments.stream()
                .mapToDouble(instrument -> calculateMarketValue(instrument, yield))
                .sum();
    }

    /**
     * Enhanced NII calculation with compounding and payment frequency
     */
    public NIIResult calculateNetInterestIncome(List<Asset> assets, List<Liability> liabilities,
                                                int compoundingPeriods) {
        double interestIncome = calculateTotalInterest(assets, compoundingPeriods);
        double interestExpense = calculateTotalInterest(liabilities, compoundingPeriods);
        double netInterestIncome = interestIncome - interestExpense;

        double netInterestMargin = calculateTotalValue(assets) > 0 ?
                netInterestIncome / calculateTotalValue(assets) : 0;

        return new NIIResult(interestIncome, interestExpense, netInterestIncome, netInterestMargin);
    }

    private double calculateTotalInterest(List<? extends FinancialInstrument> instruments, int compoundingPeriods) {
        return instruments.stream()
                .mapToDouble(instrument -> {
                    double principal = instrument.getPrincipalAmount();
                    double rate = instrument.getInterestRate();
                    double effectiveRate = Math.pow(1 + rate/compoundingPeriods, compoundingPeriods) - 1;
                    return principal * effectiveRate;
                })
                .sum();
    }

    /**
     * Comprehensive gap analysis with repricing consideration
     */
    public GapAnalysisResult performGapAnalysis(List<Asset> assets, List<Liability> liabilities) {
        Map<String, Double> gapBuckets = new LinkedHashMap<>();

        for (double days : STANDARD_GAP_BUCKETS) {
            double assetGap = calculateRateSensitiveAmount(assets, days);
            double liabilityGap = calculateRateSensitiveAmount(liabilities, days);
            gapBuckets.put(days + " days", assetGap - liabilityGap);
        }

        // Cumulative gap analysis
        Map<String, Double> cumulativeGaps = calculateCumulativeGaps(gapBuckets);

        return new GapAnalysisResult(gapBuckets, cumulativeGaps, analyzeGapRisk(gapBuckets));
    }

    private double calculateRateSensitiveAmount(List<? extends FinancialInstrument> instruments, double days) {
        return instruments.stream()
                .mapToDouble(instrument -> {
                    if (isRateSensitive(instrument, days)) {
                        return instrument.getPrincipalAmount();
                    }
                    return 0;
                })
                .sum();
    }

    private boolean isRateSensitive(FinancialInstrument instrument, double days) {
        // Matures within period OR reprices within period (for variable rate)
        return instrument.getDaysToMaturity() <= days ||
                (!instrument.isFixedRate() && getDaysToNextRepricing(instrument) <= days);
    }

    private long getDaysToNextRepricing(FinancialInstrument instrument) {
        // Simplified: assume variable rate instruments reprice quarterly
        return Math.min(instrument.getDaysToMaturity(), 90);
    }

    private Map<String, Double> calculateCumulativeGaps(Map<String, Double> gapBuckets) {
        Map<String, Double> cumulative = new LinkedHashMap<>();
        double runningTotal = 0;

        for (Map.Entry<String, Double> entry : gapBuckets.entrySet()) {
            runningTotal += entry.getValue();
            cumulative.put(entry.getKey(), runningTotal);
        }

        return cumulative;
    }

    private String analyzeGapRisk(Map<String, Double> gapBuckets) {
        double oneYearGap = gapBuckets.getOrDefault("365 days", 0.0);
        double totalAssets = 1000000; // Would come from portfolio data

        if (Math.abs(oneYearGap) > totalAssets * 0.25) {
            return "HIGH_RISK";
        } else if (Math.abs(oneYearGap) > totalAssets * 0.15) {
            return "MEDIUM_RISK";
        } else {
            return "LOW_RISK";
        }
    }

    /**
     * Interest rate sensitivity analysis (Earnings at Risk)
     */
    public SensitivityAnalysis calculateSensitivity(List<Asset> assets, List<Liability> liabilities,
                                                    double rateShock) {
        double baseNII = calculateNetInterestIncome(assets, liabilities, 1).getNetInterestIncome();

        // Calculate shocked NII
        double shockedNII = calculateNetInterestIncome(
                applyRateShock(assets, rateShock),
                applyRateShock(liabilities, rateShock),
                1
        ).getNetInterestIncome();

        double ear = baseNII - shockedNII; // Earnings at Risk
        double earPercentage = baseNII > 0 ? (ear / baseNII) * 100 : 0;

        return new SensitivityAnalysis(baseNII, shockedNII, ear, earPercentage, rateShock);
    }

    private <T extends FinancialInstrument> List<T> applyRateShock(List<T> instruments, double shock) {
        return instruments.stream()
                .map(instrument -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T shocked = (T) instrument.getClass()
                                .getConstructor(instrument.getClass())
                                .newInstance(instrument);

                        if (!shocked.isFixedRate()) {
                            shocked.setInterestRate(shocked.getInterestRate() + shock);
                        }
                        return shocked;
                    } catch (Exception e) {
                        return instrument;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Economic Value of Equity (EVE) sensitivity
     */
    public EVEAnalysis calculateEVESensitivity(List<Asset> assets, List<Liability> liabilities,
                                               double rateShock) {
        double baseEVE = calculateTotalMarketValue(assets, 0.05) -
                calculateTotalMarketValue(liabilities, 0.05);

        double shockedEVE = calculateTotalMarketValue(assets, 0.05 + rateShock) -
                calculateTotalMarketValue(liabilities, 0.05 + rateShock);

        double eveChange = shockedEVE - baseEVE;
        double eveChangePercentage = baseEVE > 0 ? (eveChange / baseEVE) * 100 : 0;

        return new EVEAnalysis(baseEVE, shockedEVE, eveChange, eveChangePercentage, rateShock);
    }

    // Data classes for comprehensive results
    public static class DurationGapResult {
        private final double durationGap;
        private final double leverageAdjustedGap;
        private final double assetDuration;
        private final double liabilityDuration;
        private final double assetValue;
        private final double liabilityValue;
        private final String status;

        public DurationGapResult(double durationGap, double leverageAdjustedGap,
                                 double assetDuration, double liabilityDuration,
                                 double assetValue, double liabilityValue, String status) {
            this.durationGap = durationGap;
            this.leverageAdjustedGap = leverageAdjustedGap;
            this.assetDuration = assetDuration;
            this.liabilityDuration = liabilityDuration;
            this.assetValue = assetValue;
            this.liabilityValue = liabilityValue;
            this.status = status;
        }
        // Getters

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

        public double getAssetValue() {
            return assetValue;
        }

        public double getLiabilityValue() {
            return liabilityValue;
        }

        public String getStatus() {
            return status;
        }
    }

    public static class NIIResult {
        private final double interestIncome;
        private final double interestExpense;
        private final double netInterestIncome;
        private final double netInterestMargin;

        public NIIResult(double interestIncome, double interestExpense,
                         double netInterestIncome, double netInterestMargin) {
            this.interestIncome = interestIncome;
            this.interestExpense = interestExpense;
            this.netInterestIncome = netInterestIncome;
            this.netInterestMargin = netInterestMargin;
        }
        // Getters

        public double getInterestIncome() {
            return interestIncome;
        }

        public double getInterestExpense() {
            return interestExpense;
        }

        public double getNetInterestIncome() {
            return netInterestIncome;
        }

        public double getNetInterestMargin() {
            return netInterestMargin;
        }
    }

    public static class GapAnalysisResult {
        private final Map<String, Double> gapBuckets;
        private final Map<String, Double> cumulativeGaps;
        private final String riskLevel;

        public GapAnalysisResult(Map<String, Double> gapBuckets,
                                 Map<String, Double> cumulativeGaps, String riskLevel) {
            this.gapBuckets = gapBuckets;
            this.cumulativeGaps = cumulativeGaps;
            this.riskLevel = riskLevel;
        }
        // Getters

        public Map<String, Double> getGapBuckets() {
            return gapBuckets;
        }

        public Map<String, Double> getCumulativeGaps() {
            return cumulativeGaps;
        }

        public String getRiskLevel() {
            return riskLevel;
        }
    }

    public static class SensitivityAnalysis {
        private final double baseNII;
        private final double shockedNII;
        private final double earningsAtRisk;
        private final double earPercentage;
        private final double rateShock;

        public SensitivityAnalysis(double baseNII, double shockedNII, double earningsAtRisk,
                                   double earPercentage, double rateShock) {
            this.baseNII = baseNII;
            this.shockedNII = shockedNII;
            this.earningsAtRisk = earningsAtRisk;
            this.earPercentage = earPercentage;
            this.rateShock = rateShock;
        }
        // Getters

        public double getBaseNII() {
            return baseNII;
        }

        public double getShockedNII() {
            return shockedNII;
        }

        public double getEarningsAtRisk() {
            return earningsAtRisk;
        }

        public double getEarPercentage() {
            return earPercentage;
        }

        public double getRateShock() {
            return rateShock;
        }
    }

    public static class EVEAnalysis {
        private final double baseEVE;
        private final double shockedEVE;
        private final double eveChange;
        private final double eveChangePercentage;
        private final double rateShock;

        public EVEAnalysis(double baseEVE, double shockedEVE, double eveChange,
                           double eveChangePercentage, double rateShock) {
            this.baseEVE = baseEVE;
            this.shockedEVE = shockedEVE;
            this.eveChange = eveChange;
            this.eveChangePercentage = eveChangePercentage;
            this.rateShock = rateShock;
        }
        // Getters

        public double getBaseEVE() {
            return baseEVE;
        }

        public double getShockedEVE() {
            return shockedEVE;
        }

        public double getEveChange() {
            return eveChange;
        }

        public double getEveChangePercentage() {
            return eveChangePercentage;
        }

        public double getRateShock() {
            return rateShock;
        }
    }

    private String getGapStatus(double durationGap) {
        if (Math.abs(durationGap) > 1.0) return "HIGH_RISK";
        if (Math.abs(durationGap) > 0.5) return "MEDIUM_RISK";
        return "LOW_RISK";
    }

    // Helper method from original service
    private double calculateTotalValue(List<? extends FinancialInstrument> instruments) {
        return instruments.stream()
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }
}