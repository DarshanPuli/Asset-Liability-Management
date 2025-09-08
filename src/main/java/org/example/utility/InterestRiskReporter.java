package org.example.utility;

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.service.InterestRateRiskService;

import java.util.Date;
import java.util.List;


public class InterestRiskReporter {
    private static final double DURATION_GAP_WARNING_THRESHOLD = 0.5; // years
    private static final double NII_VOLATILITY_THRESHOLD = 0.20; // 20% change
    private final InterestRateRiskService irrService;

    public InterestRiskReporter() {
        this.irrService = new InterestRateRiskService();
    }

    public void generateFullIRRReport(List<Asset> assets, List<Liability> liabilities) {
        System.out.println("=== INTEREST RATE RISK REPORT ===");
        System.out.println("Generated: " + new Date());
        System.out.println("=================================\n");

        // Calculate all metrics
        double currentYield = 0.05; // Assume 5% yield environment
        InterestRateRiskService.DurationGapResult durationGap =
                irrService.calculatePortfolioDurationGap(assets, liabilities, currentYield);

        InterestRateRiskService.NIIResult nii =
                irrService.calculateNetInterestIncome(assets, liabilities, 4); // Quarterly compounding

        InterestRateRiskService.GapAnalysisResult gapAnalysis =
                irrService.performGapAnalysis(assets, liabilities);

        InterestRateRiskService.SensitivityAnalysis sensitivity =
                irrService.calculateSensitivity(assets, liabilities, 0.01); // 1% rate shock

        InterestRateRiskService.EVEAnalysis eveSensitivity =
                irrService.calculateEVESensitivity(assets, liabilities, 0.02); // 2% rate shock

        // Print report sections
        printExecutiveSummary(durationGap, nii);
        printDurationGapAnalysis(durationGap);
        printNIIAnalysis(nii);
        printGapAnalysis(gapAnalysis);
        printSensitivityAnalysis(sensitivity, eveSensitivity);
        printRecommendations(durationGap, gapAnalysis, sensitivity);
    }

    private void printExecutiveSummary(InterestRateRiskService.DurationGapResult durationGap,
                                       InterestRateRiskService.NIIResult nii) {
        System.out.println("1. EXECUTIVE SUMMARY");
        System.out.println("====================");
        System.out.printf("Duration Gap: %.2f years %s%n", durationGap.getDurationGap(),
                getRiskIcon(durationGap.getStatus()));
        System.out.printf("Net Interest Income: $%,.2f%n", nii.getNetInterestIncome());
        System.out.printf("Net Interest Margin: %.2f%%%n", nii.getNetInterestMargin() * 100);
        System.out.println();
    }

    private void printDurationGapAnalysis(InterestRateRiskService.DurationGapResult result) {
        System.out.println("2. DURATION GAP ANALYSIS");
        System.out.println("========================");
        System.out.printf("Asset Duration: %.2f years%n", result.getAssetDuration());
        System.out.printf("Liability Duration: %.2f years%n", result.getLiabilityDuration());
        System.out.printf("Duration Gap: %.2f years %s%n", result.getDurationGap(),
                getRiskIcon(result.getStatus()));
        System.out.printf("Leverage-Adjusted Gap: %.2f years%n", result.getLeverageAdjustedGap());
        System.out.printf("Asset Value: $%,.2f%n", result.getAssetValue());
        System.out.printf("Liability Value: $%,.2f%n", result.getLiabilityValue());
        System.out.println();
    }

    private void printNIIAnalysis(InterestRateRiskService.NIIResult nii) {
        System.out.println("3. NET INTEREST INCOME ANALYSIS");
        System.out.println("===============================");
        System.out.printf("Interest Income: $%,.2f%n", nii.getInterestIncome());
        System.out.printf("Interest Expense: $%,.2f%n", nii.getInterestExpense());
        System.out.printf("Net Interest Income: $%,.2f%n", nii.getNetInterestIncome());
        System.out.printf("Net Interest Margin: %.2f%%%n", nii.getNetInterestMargin() * 100);
        System.out.println();
    }

    private void printGapAnalysis(InterestRateRiskService.GapAnalysisResult gapAnalysis) {
        System.out.println("4. GAP ANALYSIS");
        System.out.println("===============");
        System.out.printf("%-12s %-15s %-15s%n", "Bucket", "Gap", "Cumulative");
        System.out.println("----------------------------------------");

        gapAnalysis.getGapBuckets().forEach((bucket, gap) -> {
            double cumulative = gapAnalysis.getCumulativeGaps().get(bucket);
            System.out.printf("%-12s $%,-14.2f $%,-14.2f%n", bucket, gap, cumulative);
        });

        System.out.printf("%nRisk Level: %s %s%n", gapAnalysis.getRiskLevel(),
                getRiskIcon(gapAnalysis.getRiskLevel()));
        System.out.println();
    }

    private void printSensitivityAnalysis(InterestRateRiskService.SensitivityAnalysis sensitivity,
                                          InterestRateRiskService.EVEAnalysis eveSensitivity) {
        System.out.println("5. SENSITIVITY ANALYSIS");
        System.out.println("=======================");
        System.out.println("Earnings at Risk (1% Rate Shock):");
        System.out.printf("  Base NII: $%,.2f%n", sensitivity.getBaseNII());
        System.out.printf("  Shocked NII: $%,.2f%n", sensitivity.getShockedNII());
        System.out.printf("  Earnings at Risk: $%,.2f (%.1f%%)%n",
                sensitivity.getEarningsAtRisk(), sensitivity.getEarPercentage());

        System.out.println("\nEconomic Value of Equity Sensitivity (2% Rate Shock):");
        System.out.printf("  Base EVE: $%,.2f%n", eveSensitivity.getBaseEVE());
        System.out.printf("  Shocked EVE: $%,.2f%n", eveSensitivity.getShockedEVE());
        System.out.printf("  EVE Change: $%,.2f (%.1f%%)%n",
                eveSensitivity.getEveChange(), eveSensitivity.getEveChangePercentage());
        System.out.println();
    }

    private void printRecommendations(InterestRateRiskService.DurationGapResult durationGap,
                                      InterestRateRiskService.GapAnalysisResult gapAnalysis,
                                      InterestRateRiskService.SensitivityAnalysis sensitivity) {
        System.out.println("6. RECOMMENDATIONS");
        System.out.println("==================");

        if (Math.abs(durationGap.getDurationGap()) > DURATION_GAP_WARNING_THRESHOLD) {
            if (durationGap.getDurationGap() > 0) {
                System.out.println("• Consider adding shorter-term assets or longer-term liabilities");
            } else {
                System.out.println("• Consider adding longer-term assets or shorter-term liabilities");
            }
        }

        if (sensitivity.getEarPercentage() > NII_VOLATILITY_THRESHOLD * 100) {
            System.out.println("• Implement interest rate hedging strategies");
            System.out.println("• Consider interest rate swaps or options");
        }

        if (gapAnalysis.getRiskLevel().equals("HIGH_RISK")) {
            System.out.println("• Rebalance maturity profile to reduce gap risk");
            System.out.println("• Increase liquidity buffers for negative gap periods");
        }

        System.out.println("• Enhance monitoring of variable rate exposures");
        System.out.println("• Consider basis risk between asset and liability repricing");
    }

    private String getRiskIcon(String riskLevel) {
        return switch (riskLevel) {
            case "LOW_RISK", "LOW" -> "✅";
            case "MEDIUM_RISK", "MEDIUM" -> "⚠️";
            case "HIGH_RISK", "HIGH" -> "❌";
            default -> "❓";
        };
    }

}
