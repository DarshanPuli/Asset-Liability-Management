package org.example.utility;

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.service.LiquidityRiskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LiquidityReporter {

    private final LiquidityRiskService liquidityService;

    public LiquidityReporter() {
        this.liquidityService = new LiquidityRiskService();
    }

    public void generateFullReport(List<Asset> assets, List<Liability> liabilities) {
        System.out.println("=== LIQUIDITY RISK REPORT ===");
        System.out.println("Generated: " + LocalDate.now());
        System.out.println("=============================\n");

        LiquidityRiskService.LiquidityAnalysis analysis =
                liquidityService.analyzeLiquidity(assets, liabilities);

        printExecutiveSummary(analysis);
        printRegulatoryCompliance(analysis);
        printMaturityAnalysis(analysis.getMaturityLadder());
        printCurrencyAnalysis(analysis.getCurrencyExposure());
        printGapAnalysis(analysis.getLiquidityGaps());
        printRecommendations(analysis);
    }

    private void printExecutiveSummary(LiquidityRiskService.LiquidityAnalysis analysis) {
        System.out.println("1. EXECUTIVE SUMMARY");
        System.out.println("====================");
        System.out.printf("LCR Ratio: %.2f%% %s%n", analysis.getLcrRatio(),
                analysis.getLcrRatio() >= 100 ? "✅" : "❌");
        System.out.printf("NSFR Ratio: %.2f%% %s%n", analysis.getNsfrRatio(),
                analysis.getNsfrRatio() >= 100 ? "✅" : "❌");
        System.out.println();
    }

    private void printRegulatoryCompliance(LiquidityRiskService.LiquidityAnalysis analysis) {
        System.out.println("2. REGULATORY COMPLIANCE");
        System.out.println("========================");
        System.out.printf("Liquidity Coverage Ratio (LCR): %.2f%% %s%n",
                analysis.getLcrRatio(), getComplianceStatus(analysis.getLcrRatio(), 100));
        System.out.printf("Net Stable Funding Ratio (NSFR): %.2f%% %s%n",
                analysis.getNsfrRatio(), getComplianceStatus(analysis.getNsfrRatio(), 100));
        System.out.println();
    }

    private void printMaturityAnalysis(Map<String, LiquidityRiskService.MaturityBucket> ladder) {
        System.out.println("3. MATURITY LADDER ANALYSIS");
        System.out.println("===========================");
        System.out.printf("%-12s %-15s %-15s %-15s%n", "Bucket", "Assets", "Liabilities", "Net Flow");
        System.out.println("------------------------------------------------------------");

        ladder.forEach((bucket, data) -> {
            System.out.printf("%-12s %,-15.2f %,-15.2f %,-15.2f %s%n",
                    bucket, data.getAssetCashFlow(), data.getLiabilityCashFlow(),
                    data.getNetCashFlow(), getFlowIndicator(data.getNetCashFlow()));
        });
        System.out.println();
    }

    private void printCurrencyAnalysis(Map<String, LiquidityRiskService.CurrencyExposure> exposure) {
        System.out.println("4. CURRENCY EXPOSURE");
        System.out.println("====================");
        System.out.printf("%-6s %-15s %-15s %-15s %s%n", "Curr", "Assets", "Liabilities", "Net", "Status");
        System.out.println("------------------------------------------------------------");

        exposure.forEach((currency, data) -> {
            System.out.printf("%-6s %,-15.2f %,-15.2f %,-15.2f %s%n",
                    currency, data.getAssets(), data.getLiabilities(),
                    data.getNetExposure(), getExposureStatus(data.getNetExposure()));
        });
        System.out.println();
    }

    private void printGapAnalysis(Map<String, Double> gaps) {
        System.out.println("5. LIQUIDITY GAPS");
        System.out.println("=================");
        gaps.forEach((period, gap) -> {
            System.out.printf("%-10s: %,-15.2f %s%n", period, gap, getGapStatus(gap));
        });
        System.out.println();
    }

    private void printRecommendations(LiquidityRiskService.LiquidityAnalysis analysis) {
        System.out.println("6. RECOMMENDATIONS");
        System.out.println("==================");

        if (analysis.getLcrRatio() < 100) {
            System.out.println("• Increase High Quality Liquid Assets (HQLA)");
            System.out.println("• Reduce short-term wholesale funding");
            System.out.println("• Extend liability maturities");
        }

        if (analysis.getNsfrRatio() < 100) {
            System.out.println("• Increase stable funding sources");
            System.out.println("• Reduce long-term illiquid assets");
            System.out.println("• Issue longer-term debt");
        }

        // Add more specific recommendations based on your analysis
    }

    // Helper methods for status indicators
    private String getComplianceStatus(double ratio, double threshold) {
        return ratio >= threshold ? "✅ COMPLIANT" : "❌ NON-COMPLIANT";
    }

    private String getFlowIndicator(double netFlow) {
        return netFlow >= 0 ? "🟢" : "🔴";
    }

    private String getExposureStatus(double netExposure) {
        if (Math.abs(netExposure) < 10000) return "🟡 BALANCED";
        return netExposure > 0 ? "🔴 LONG" : "🔴 SHORT";
    }

    private String getGapStatus(double gap) {
        return gap >= 0 ? "🟢" : "🔴";
    }
}