package org.example.utility;

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.service.MatchingService;

import java.util.Date;
import java.util.List;

public class ALMReporter {

    private final MatchingService matchingService;

    public ALMReporter() {
        this.matchingService = new MatchingService();
    }

    public void generateFullALMReport(List<Asset> assets, List<Liability> liabilities) {
        System.out.println("=== ASSET-LIABILITY MANAGEMENT REPORT ===");
        System.out.println("Generated: " + new Date());
        System.out.println("=========================================\n");

        double yield = 0.05; // Assume 5% yield environment
        MatchingService.ALMAnalysis analysis = matchingService.analyzeALM(assets, liabilities, yield);

        printExecutiveSummary(analysis);
        printDurationAnalysis(analysis.getDurationMatch());
        printCurrencyAnalysis(analysis.getCurrencyMatch());
        printMaturityAnalysis(analysis.getMaturityMatch());
        printRateTypeAnalysis(analysis.getRateTypeMatch());
        printRecommendations(analysis.getRecommendations());
    }

    private void printExecutiveSummary(MatchingService.ALMAnalysis analysis) {
        System.out.println("1. EXECUTIVE SUMMARY");
        System.out.println("====================");
        System.out.printf("Overall ALM Score: %.1f%% %s%n", analysis.getOverallScore(),
                getStatusIcon(analysis.getOverallStatus()));
        System.out.printf("Overall Status: %s%n", analysis.getOverallStatus());
        System.out.println();
    }

    private void printDurationAnalysis(MatchingService.DurationMatchResult duration) {
        System.out.println("2. DURATION MATCHING ANALYSIS");
        System.out.println("=============================");
        System.out.printf("Asset Duration: %.2f years%n", duration.getAssetDuration());
        System.out.printf("Liability Duration: %.2f years%n", duration.getLiabilityDuration());
        System.out.printf("Duration Gap: %.2f years %s%n", duration.getDurationGap(),
                getStatusIcon(duration.getStatus()));
        System.out.printf("Leverage-Adjusted Gap: %.2f years%n", duration.getLeverageAdjustedGap());
        System.out.printf("Match Score: %.1f%%%n", duration.getMatchScore());
        System.out.printf("Total Assets: $%,.2f%n", duration.getTotalAssets());
        System.out.printf("Total Liabilities: $%,.2f%n", duration.getTotalLiabilities());
        System.out.println();
    }

    private void printCurrencyAnalysis(MatchingService.CurrencyMatchResult currency) {
        System.out.println("3. CURRENCY MATCHING ANALYSIS");
        System.out.println("=============================");
        System.out.printf("Overall Match: %.1f%% %s%n", currency.getMatchPercentage(),
                getStatusIcon(currency.getStatus()));

        System.out.println("\nCurrency Exposures:");
        System.out.printf("%-6s %-12s %-12s %-12s %s%n", "Curr", "Assets", "Liabilities", "Net", "Status");
        System.out.println("------------------------------------------------");

        currency.getExposures().forEach((curr, exposure) -> {
            String status = Math.abs(exposure.getNetExposure()) < 1000 ? "BALANCED" : "EXPOSED";
            System.out.printf("%-6s %,-12.2f %,-12.2f %,-12.2f %s%n",
                    curr, exposure.getAssets(), exposure.getLiabilities(),
                    exposure.getNetExposure(), getStatusIcon(status));
        });
        System.out.println();
    }

    private void printMaturityAnalysis(MatchingService.MaturityMatchResult maturity) {
        System.out.println("4. MATURITY MATCHING ANALYSIS");
        System.out.println("=============================");
        System.out.printf("Overall Match: %.1f%% %s%n", maturity.getMatchPercentage(),
                getStatusIcon(maturity.getStatus()));

        System.out.println("\nMaturity Bucket Analysis:");
        System.out.printf("%-12s %-12s %-12s %-12s%n", "Bucket", "Assets", "Liabilities", "Gap");
        System.out.println("--------------------------------------------");

        maturity.getAssetBuckets().forEach((bucket, assetBucket) -> {
            double assetAmount = assetBucket.getAmount();
            double liabilityAmount = maturity.getLiabilityBuckets().getOrDefault(bucket,
                    new MatchingService.MaturityBucket(bucket)).getAmount();
            double gap = assetAmount - liabilityAmount;

            System.out.printf("%-12s %,-12.2f %,-12.2f %,-12.2f %s%n",
                    bucket, assetAmount, liabilityAmount, gap, getGapIcon(gap));
        });
        System.out.println();
    }

    private void printRateTypeAnalysis(MatchingService.RateTypeMatchResult rateType) {
        System.out.println("5. RATE TYPE MATCHING ANALYSIS");
        System.out.println("==============================");
        System.out.printf("Match Score: %.1f%% %s%n", rateType.getMatchScore(),
                getStatusIcon(rateType.getStatus()));

        System.out.printf("Fixed Assets: $%,.2f (%.1f%%)%n",
                rateType.getFixedAssets(), rateType.getFixedRatioAssets() * 100);
        System.out.printf("Variable Assets: $%,.2f (%.1f%%)%n",
                rateType.getVariableAssets(), (1 - rateType.getFixedRatioAssets()) * 100);
        System.out.printf("Fixed Liabilities: $%,.2f (%.1f%%)%n",
                rateType.getFixedLiabilities(), rateType.getFixedRatioLiabilities() * 100);
        System.out.printf("Variable Liabilities: $%,.2f (%.1f%%)%n",
                rateType.getVariableLiabilities(), (1 - rateType.getFixedRatioLiabilities()) * 100);
        System.out.printf("Mismatch: %.1f%%%n", rateType.getMismatch() * 100);
        System.out.println();
    }

    private void printRecommendations(List<String> recommendations) {
        System.out.println("6. RECOMMENDATIONS & ACTION ITEMS");
        System.out.println("==================================");
        recommendations.forEach(rec -> System.out.println("• " + rec));
    }

    private String getStatusIcon(String status) {
        return switch (status.toUpperCase()) {
            case "MATCHED", "EXCELLENT", "BALANCED" -> "✅";
            case "GOOD", "FAIR" -> "⚠️";
            case "MISMATCHED", "POOR", "EXPOSED" -> "❌";
            default -> "❓";
        };
    }

    private String getGapIcon(double gap) {
        if (Math.abs(gap) < 1000) return "✅";
        return gap > 0 ? "🔺" : "🔻";
    }

}