package org.example.service;

// src/com/alm/service/LiquidityRiskService.java

import org.example.model.Asset;
import org.example.model.Liability;

import java.util.List;
import java.util.Map;

import java.util.*;
import java.util.stream.Collectors;

public class LiquidityRiskService {

    // Regulatory parameters - Basel III compliant
    private static final Map<String, Double> HQLA_HAIRCUTS = Map.of(
            "cash", 0.00,           // Level 1 - No haircut
            "government_bond", 0.05, // Level 1 - 5% haircut
            "municipal_bond", 0.15,  // Level 2A - 15% haircut
            "corporate_bond", 0.25,  // Level 2B - 25% haircut
            "loan", 1.00            // Not HQLA - 100% haircut
    );

    private static final Map<String, Double> RUNOFF_FACTORS = Map.of(
            "retail_savings", 0.05,   // Stable retail deposits - 5% runoff
            "retail_deposit", 0.10,   // Less stable retail - 10% runoff
            "wholesale_deposit", 0.25, // Operational deposits - 25% runoff
            "callable_deposit", 0.40,  // Callable deposits - 40% runoff
            "foreign_deposit", 0.30,   // Foreign currency deposits - 30% runoff
            "debt", 1.00,             // Debt instruments - 100% runoff
            "bond", 0.80,             // Bonds - 80% runoff
            "note", 0.60,             // Short-term notes - 60% runoff
            "repo", 0.05              // Repos - 5% runoff (collateralized)
    );

    private static final Map<String, Double> RSF_FACTORS = Map.of(
            "cash", 0.00,           // Cash - 0% RSF
            "government_bond", 0.05, // Govt bonds < 1Y - 5% RSF
            "government_bond_long", 0.20, // Govt bonds > 1Y - 20% RSF
            "municipal_bond", 0.50,  // Municipal bonds - 50% RSF
            "corporate_bond", 0.85,  // Corporate bonds - 85% RSF
            "loan_short", 0.50,      // Loans < 1Y - 50% RSF
            "loan_long", 0.85       // Loans > 1Y - 85% RSF
    );

    private static final Map<String, Double> ASF_FACTORS = Map.of(
            "stable_deposit", 0.95,  // Stable deposits - 95% ASF
            "less_stable_deposit", 0.90, // Less stable deposits - 90% ASF
            "wholesale_deposit", 0.50,  // Wholesale funding - 50% ASF
            "debt_1y", 0.50,         // Debt < 1Y - 50% ASF
            "debt_1y_plus", 1.00,    // Debt > 1Y - 100% ASF
            "equity", 1.00           // Equity - 100% ASF
    );

    public LiquidityAnalysis analyzeLiquidity(List<Asset> assets, List<Liability> liabilities) {
        return new LiquidityAnalysis(
                calculateEnhancedMaturityLadder(assets, liabilities),
                calculateRegulatoryLCR(assets, liabilities),
                calculateRegulatoryNSFR(assets, liabilities),
                calculateCurrencyExposure(assets, liabilities),
                calculateLiquidityGaps(assets, liabilities)
        );
    }

    public Map<String, MaturityBucket> calculateEnhancedMaturityLadder(List<Asset> assets, List<Liability> liabilities) {
        Map<String, MaturityBucket> ladder = new LinkedHashMap<>();

        String[] buckets = {"0-30 days", "31-90 days", "91-365 days", "1-3 years", "3+ years"};
        int[][] daysRange = {{0, 30}, {31, 90}, {91, 365}, {366, 1095}, {1096, Integer.MAX_VALUE}};

        for (int i = 0; i < buckets.length; i++) {
            String bucket = buckets[i];
            int minDays = daysRange[i][0];
            int maxDays = daysRange[i][1];

            double assetFlow = calculateAssetCashFlows(assets, minDays, maxDays);
            double liabilityFlow = calculateLiabilityCashFlows(liabilities, minDays, maxDays);
            double netFlow = assetFlow - liabilityFlow;

            ladder.put(bucket, new MaturityBucket(assetFlow, liabilityFlow, netFlow));
        }

        return ladder;
    }

    private double calculateAssetCashFlows(List<Asset> assets, int minDays, int maxDays) {
        return assets.stream()
                .mapToDouble(asset -> {
                    long daysToMaturity = asset.getDaysToMaturity();
                    if (daysToMaturity >= minDays && daysToMaturity <= maxDays) {
                        // Include principal + accrued interest to maturity
                        double principal = asset.getPrincipalAmount();
                        double interest = principal * asset.getInterestRate() * (daysToMaturity / 365.0);
                        return principal + interest;
                    }
                    return 0;
                })
                .sum();
    }

    private double calculateLiabilityCashFlows(List<Liability> liabilities, int minDays, int maxDays) {
        return liabilities.stream()
                .mapToDouble(liability -> {
                    long daysToMaturity = liability.getDaysToMaturity();
                    if (daysToMaturity >= minDays && daysToMaturity <= maxDays) {
                        // Include principal + interest payments
                        double principal = liability.getPrincipalAmount();
                        double interest = principal * liability.getInterestRate() * (daysToMaturity / 365.0);
                        return principal + interest;
                    }
                    return 0;
                })
                .sum();
    }

    public double calculateRegulatoryLCR(List<Asset> assets, List<Liability> liabilities) {
        // Calculate adjusted High Quality Liquid Assets
        double adjustedHQLA = assets.stream()
                .mapToDouble(asset -> {
                    double haircut = getHQLAHaircut(asset);
                    return asset.getPrincipalAmount() * (1 - haircut);
                })
                .sum();

        // Calculate total net cash outflows with runoff factors
        double totalNetOutflows = liabilities.stream()
                .mapToDouble(liability -> {
                    double runoffFactor = getRunoffFactor(liability);
                    return liability.getPrincipalAmount() * runoffFactor;
                })
                .sum();

        // Also consider potential drawdowns on undrawn commitments
        double undrawnCommitments = calculateUndrawnCommitments(assets);
        totalNetOutflows += undrawnCommitments * 0.10; // 10% of undrawn commitments

        return totalNetOutflows > 0 ? (adjustedHQLA / totalNetOutflows) * 100 : Double.POSITIVE_INFINITY;
    }

    public double calculateRegulatoryNSFR(List<Asset> assets, List<Liability> liabilities) {
        double requiredStableFunding = assets.stream()
                .mapToDouble(asset -> asset.getPrincipalAmount() * getRSFFactor(asset))
                .sum();

        double availableStableFunding = liabilities.stream()
                .mapToDouble(liability -> liability.getPrincipalAmount() * getASFFactor(liability))
                .sum();

        return requiredStableFunding > 0 ? (availableStableFunding / requiredStableFunding) * 100 : Double.POSITIVE_INFINITY;
    }

    private double getHQLAHaircut(Asset asset) {
        return HQLA_HAIRCUTS.getOrDefault(asset.getType().toLowerCase(), 1.00);
    }

    private double getRunoffFactor(Liability liability) {
        String liabilityType = liability.getType().toLowerCase();

        // Enhanced logic based on your data structure
        if (liabilityType.equals("savings")) return 0.05;
        if (liabilityType.equals("deposit")) {
            if (liability.getName().toLowerCase().contains("callable")) return 0.40;
            if (!liability.getCurrency().equals("USD")) return 0.30;
            return 0.10;
        }
        if (liabilityType.equals("repo")) return 0.05;

        return RUNOFF_FACTORS.getOrDefault(liabilityType, 1.00);
    }

    private double getRSFFactor(Asset asset) {
        long daysToMaturity = asset.getDaysToMaturity();

        switch (asset.getType().toLowerCase()) {
            case "cash":
                return 0.00;
            case "government_bond":
                return daysToMaturity <= 365 ? 0.05 : 0.20;
            case "municipal_bond":
                return 0.50;
            case "corporate_bond":
                return 0.85;
            case "loan":
                return daysToMaturity <= 365 ? 0.50 : 0.85;
            default:
                return 1.00;
        }
    }

    private double getASFFactor(Liability liability) {
        long daysToMaturity = liability.getDaysToMaturity();
        String type = liability.getType().toLowerCase();

        if (type.equals("savings")) return 0.95;
        if (type.equals("deposit")) {
            if (liability.getName().toLowerCase().contains("callable")) return 0.50;
            return 0.90;
        }
        if (type.equals("debt") || type.equals("bond")) {
            return daysToMaturity <= 365 ? 0.50 : 1.00;
        }
        if (type.equals("note")) return 0.50;
        if (type.equals("repo")) return 0.0; // Repos are not stable funding

        return 0.0;
    }

    private double calculateUndrawnCommitments(List<Asset> assets) {
        // For your data, assume 20% of loan portfolio is undrawn
        return assets.stream()
                .filter(a -> a.getType().equalsIgnoreCase("loan"))
                .mapToDouble(Asset::getPrincipalAmount)
                .sum() * 0.20;
    }

    public Map<String, CurrencyExposure> calculateCurrencyExposure(List<Asset> assets, List<Liability> liabilities) {
        Map<String, CurrencyExposure> exposure = new HashMap<>();

        // Group by currency
        Map<String, Double> assetByCurrency = assets.stream()
                .collect(Collectors.groupingBy(Asset::getCurrency,
                        Collectors.summingDouble(Asset::getPrincipalAmount)));

        Map<String, Double> liabilityByCurrency = liabilities.stream()
                .collect(Collectors.groupingBy(Liability::getCurrency,
                        Collectors.summingDouble(Liability::getPrincipalAmount)));

        // Calculate net exposure
        Set<String> allCurrencies = new HashSet<>();
        allCurrencies.addAll(assetByCurrency.keySet());
        allCurrencies.addAll(liabilityByCurrency.keySet());

        for (String currency : allCurrencies) {
            double assetsAmount = assetByCurrency.getOrDefault(currency, 0.0);
            double liabilitiesAmount = liabilityByCurrency.getOrDefault(currency, 0.0);
            double netExposure = assetsAmount - liabilitiesAmount;

            exposure.put(currency, new CurrencyExposure(assetsAmount, liabilitiesAmount, netExposure));
        }

        return exposure;
    }

    public Map<String, Double> calculateLiquidityGaps(List<Asset> assets, List<Liability> liabilities) {
        Map<String, Double> gaps = new HashMap<>();

        int[] timePeriods = {7, 30, 90, 180, 365}; // days
        for (int days : timePeriods) {
            double assetFlow = calculateAssetCashFlows(assets, 0, days);
            double liabilityFlow = calculateLiabilityCashFlows(liabilities, 0, days);
            gaps.put(days + " days", assetFlow - liabilityFlow);
        }

        return gaps;
    }

    // Helper classes for comprehensive reporting
    public static class LiquidityAnalysis {
        private final Map<String, MaturityBucket> maturityLadder;
        private final double lcrRatio;
        private final double nsfrRatio;
        private final Map<String, CurrencyExposure> currencyExposure;
        private final Map<String, Double> liquidityGaps;

        public LiquidityAnalysis(Map<String, MaturityBucket> maturityLadder, double lcrRatio,
                                 double nsfrRatio, Map<String, CurrencyExposure> currencyExposure,
                                 Map<String, Double> liquidityGaps) {
            this.maturityLadder = maturityLadder;
            this.lcrRatio = lcrRatio;
            this.nsfrRatio = nsfrRatio;
            this.currencyExposure = currencyExposure;
            this.liquidityGaps = liquidityGaps;
        }

        // Getters and toString for reporting

        @Override
        public String toString() {
            return "LiquidityAnalysis{" +
                    "maturityLadder=" + maturityLadder +
                    ", lcrRatio=" + lcrRatio +
                    ", nsfrRatio=" + nsfrRatio +
                    ", currencyExposure=" + currencyExposure +
                    ", liquidityGaps=" + liquidityGaps +
                    '}';
        }

        public Map<String, MaturityBucket> getMaturityLadder() {
            return maturityLadder;
        }

        public double getLcrRatio() {
            return lcrRatio;
        }

        public double getNsfrRatio() {
            return nsfrRatio;
        }

        public Map<String, CurrencyExposure> getCurrencyExposure() {
            return currencyExposure;
        }

        public Map<String, Double> getLiquidityGaps() {
            return liquidityGaps;
        }
    }

    public static class MaturityBucket {
        private final double assetCashFlow;
        private final double liabilityCashFlow;
        private final double netCashFlow;

        public MaturityBucket(double assetCashFlow, double liabilityCashFlow, double netCashFlow) {
            this.assetCashFlow = assetCashFlow;
            this.liabilityCashFlow = liabilityCashFlow;
            this.netCashFlow = netCashFlow;
        }

        // Getters

        public double getAssetCashFlow() {
            return assetCashFlow;
        }

        public double getLiabilityCashFlow() {
            return liabilityCashFlow;
        }

        public double getNetCashFlow() {
            return netCashFlow;
        }
    }

    public static class CurrencyExposure {
        private final double assets;
        private final double liabilities;
        private final double netExposure;

        public CurrencyExposure(double assets, double liabilities, double netExposure) {
            this.assets = assets;
            this.liabilities = liabilities;
            this.netExposure = netExposure;
        }

        // Getters

        public double getAssets() {
            return assets;
        }

        public double getLiabilities() {
            return liabilities;
        }

        public double getNetExposure() {
            return netExposure;
        }
    }
}
