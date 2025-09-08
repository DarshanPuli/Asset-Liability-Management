package org.example.utility;

import org.example.model.Asset;
import org.example.model.Loan;
import org.example.service.CreditRiskService;

import java.util.*;
import java.util.stream.Collectors;

public class CreditRiskReporter {

    private final CreditRiskService creditService;

    public CreditRiskReporter() {
        this.creditService = new CreditRiskService();
    }

    public void generateFullCreditReport(List<Loan> loans, List<Asset> assets,
                                         double tier1Capital, double tier2Capital) {
        System.out.println("=== CREDIT RISK REPORT ===");
        System.out.println("Generated: " + new Date());
        System.out.println("==========================\n");

        // Calculate all metrics
        double totalEL = creditService.calculateTotalExpectedLoss(loans);
        double rwa = creditService.calculateRiskWeightedAssets(assets);
        CreditRiskService.CapitalAdequacyResult car =
                creditService.calculateCapitalAdequacy(tier1Capital, tier2Capital, rwa);
        CreditRiskService.PortfolioCreditMetrics metrics =
                creditService.calculatePortfolioMetrics(loans);
        CreditRiskService.StressTestResult stressTest =
                creditService.performStressTest(loans, 1.5, 1.2); // 50% PD shock, 20% LGD shock
        CreditRiskService.ConcentrationAnalysis concentration =
                creditService.analyzeConcentration(loans);

        // Print report sections
        printExecutiveSummary(totalEL, rwa, car);
        printPortfolioMetrics(metrics);
        printCapitalAdequacy(car);
        printStressTesting(stressTest);
        printConcentrationAnalysis(concentration);
        printRecommendations(car, metrics, concentration);
    }

    private void printExecutiveSummary(double totalEL, double rwa,
                                       CreditRiskService.CapitalAdequacyResult car) {
        System.out.println("1. EXECUTIVE SUMMARY");
        System.out.println("====================");
        System.out.printf("Total Expected Loss: $%,.2f%n", totalEL);
        System.out.printf("Risk-Weighted Assets: $%,.2f%n", rwa);
        System.out.printf("Capital Adequacy Ratio: %.2f%% %s%n", car.getCar() * 100,
                getStatusIcon(car.getStatus()));
        System.out.println();
    }

    private void printPortfolioMetrics(CreditRiskService.PortfolioCreditMetrics metrics) {
        System.out.println("2. PORTFOLIO METRICS");
        System.out.println("====================");
        System.out.printf("Total Exposure: $%,.2f%n", metrics.getTotalExposure());
        System.out.printf("Average Probability of Default: %.2f%%%n", metrics.getAveragePD() * 100);
        System.out.printf("Average Loss Given Default: %.2f%%%n", metrics.getAverageLGD() * 100);
        System.out.printf("Expected Loss Ratio: %.2f%%%n",
                (metrics.getTotalExpectedLoss() / metrics.getTotalExposure()) * 100);
        System.out.println();

        System.out.println("Exposure by Credit Rating:");
        System.out.printf("%-12s %-15s %s%n", "Rating", "Exposure", "Distribution");
        System.out.println("----------------------------------------");
        metrics.getExposureByRating().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    double distribution = (entry.getValue() / metrics.getTotalExposure()) * 100;
                    System.out.printf("%-12d $%,-14.2f %.1f%%%n",
                            entry.getKey(), entry.getValue(), distribution);
                });
        System.out.println();
    }

    private void printCapitalAdequacy(CreditRiskService.CapitalAdequacyResult car) {
        System.out.println("3. CAPITAL ADEQUACY");
        System.out.println("===================");
        System.out.printf("Total Capital: $%,.2f%n", car.getTotalCapital());
        System.out.printf("Risk-Weighted Assets: $%,.2f%n", car.getRwa());
        System.out.printf("CAR Ratio: %.2f%% %s%n", car.getCar() * 100,
                getStatusIcon(car.getStatus()));
        System.out.printf("Tier 1 Ratio: %.2f%%%n", car.getTier1Ratio() * 100);
        System.out.println();

        System.out.println("Regulatory Thresholds:");
        System.out.println("• Well Capitalized: ≥ 10.5%");
        System.out.println("• Adequately Capitalized: ≥ 8.0%");
        System.out.println("• Under-capitalized: ≥ 6.0%");
        System.out.println("• Critically under-capitalized: < 6.0%");
        System.out.println();
    }

    private void printStressTesting(CreditRiskService.StressTestResult stressTest) {
        System.out.println("4. STRESS TESTING");
        System.out.println("=================");
        System.out.printf("Base Expected Loss: $%,.2f%n", stressTest.getBaseExpectedLoss());
        System.out.printf("Stressed Expected Loss: $%,.2f%n", stressTest.getStressedExpectedLoss());
        System.out.printf("Increase: $%,.2f (%.1f%%)%n",
                stressTest.getStressedExpectedLoss() - stressTest.getBaseExpectedLoss(),
                ((stressTest.getStressedExpectedLoss() / stressTest.getBaseExpectedLoss()) - 1) * 100);
        System.out.printf("PD Shock: %.1fx, LGD Shock: %.1fx%n",
                stressTest.getPdShock(), stressTest.getLgdShock());
        System.out.println();
    }

    private void printConcentrationAnalysis(CreditRiskService.ConcentrationAnalysis concentration) {
        System.out.println("5. CONCENTRATION ANALYSIS");
        System.out.println("=========================");

        double largestShare = (concentration.getLargestExposure() / concentration.getTotalExposure()) * 100;
        System.out.printf("Largest Exposure: $%,.2f (%.1f%%)%n",
                concentration.getLargestExposure(), largestShare);

        System.out.println("\nTop 5 Borrowers:");
        concentration.getBorrowerExposure().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    double share = (entry.getValue() / concentration.getTotalExposure()) * 100;
                    System.out.printf("• %s: $%,.2f (%.1f%%)%n", entry.getKey(), entry.getValue(), share);
                });

        System.out.println("\nSector Exposure:");
        concentration.getSectorExposure().forEach((sector, exposure) -> {
            double share = (exposure / concentration.getTotalExposure()) * 100;
            System.out.printf("• %s: $%,.2f (%.1f%%)%n", sector, exposure, share);
        });
        System.out.println();
    }

    private void printRecommendations(CreditRiskService.CapitalAdequacyResult car,
                                      CreditRiskService.PortfolioCreditMetrics metrics,
                                      CreditRiskService.ConcentrationAnalysis concentration) {
        System.out.println("6. RECOMMENDATIONS");
        System.out.println("==================");

        if (car.getCar() < 0.105) {
            System.out.println("• Increase capital buffers to meet Basel III requirements");
        }

        if (metrics.getAveragePD() > 0.05) {
            System.out.println("• Review underwriting standards for high-PD loans");
        }

        double largestShare = concentration.getLargestExposure() / concentration.getTotalExposure();
        if (largestShare > 0.10) {
            System.out.println("• Reduce single-borrower concentration risk");
        }

        // Additional recommendations based on your risk appetite
        System.out.println("• Enhance collateral requirements for lower-rated borrowers");
        System.out.println("• Consider credit derivatives for portfolio hedging");
        System.out.println("• Implement more granular risk-based pricing");
    }

    private String getStatusIcon(String status) {
        return switch (status) {
            case "WELL_CAPITALIZED" -> "✅";
            case "ADEQUATELY_CAPITALIZED" -> "⚠️";
            case "UNDERCAPITALIZED" -> "❌";
            case "CRITICALLY_UNDERCAPITALIZED" -> "🔥";
            default -> "❓";
        };
    }

}