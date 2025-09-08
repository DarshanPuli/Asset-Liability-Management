package org.example.utility;

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.model.Loan;
import org.example.model.Scenario;
import org.example.service.ScenarioAnalysisService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ScenarioAnalysisReporter {

    private final ScenarioAnalysisService scenarioService;

    public ScenarioAnalysisReporter() {
        this.scenarioService = new ScenarioAnalysisService();
    }

    public void generateStressTestingReport(List<Scenario> scenarios, List<Asset> assets,
                                            List<Liability> liabilities, List<Loan> loans) {
        System.out.println("=== COMPREHENSIVE STRESS TESTING REPORT ===");
        System.out.println("Generated: " + new Date());
        System.out.println("===========================================\n");

        Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results =
                scenarioService.runStressTesting(scenarios, assets, liabilities, loans);

        printExecutiveSummary(results);
        printScenarioComparison(results);
        printMostSevereScenarios(results);
        printDetailedScenarioAnalysis(results);
        printRecommendations(results);
    }

    private void printExecutiveSummary(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        System.out.println("1. EXECUTIVE SUMMARY");
        System.out.println("====================");

        long severeCount = results.values().stream().filter(r -> r.getOverallSeverity().equals("SEVERE")).count();
        long highCount = results.values().stream().filter(r -> r.getOverallSeverity().equals("HIGH")).count();

        System.out.printf("Total Scenarios Analyzed: %d%n", results.size());
        System.out.printf("Severe Impact Scenarios: %d%n", severeCount);
        System.out.printf("High Impact Scenarios: %d%n", highCount);
        System.out.println();

        System.out.println("Most Vulnerable Areas:");
        printVulnerabilityAnalysis(results);
        System.out.println();
    }

    private void printScenarioComparison(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        System.out.println("2. SCENARIO COMPARISON");
        System.out.println("======================");
        System.out.printf("%-20s %-12s %-10s %-10s %-10s %-10s%n",
                "Scenario", "Severity", "NII Δ%", "EVE Δ%", "EL Δ%", "LCR Δ");
        System.out.println("--------------------------------------------------------------------------------");

        results.entrySet().stream()
                .sorted((e1, e2) -> compareSeverity(e2.getValue().getOverallSeverity(), e1.getValue().getOverallSeverity()))
                .forEach(entry -> {
                    ScenarioAnalysisService.ScenarioAnalysisResult result = entry.getValue();
                    System.out.printf("%-20s %-12s %-10.1f %-10.1f %-10.1f %-10.2f%n",
                            entry.getKey(),
                            result.getOverallSeverity(),
                            result.getNiiImpact() * 100,
                            result.getEveImpact() * 100,
                            result.getElImpact() * 100,
                            result.getLcrImpact()
                    );
                });
        System.out.println();
    }

    private void printMostSevereScenarios(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        System.out.println("3. MOST SEVERE SCENARIOS");
        System.out.println("========================");

        List<ScenarioAnalysisService.ScenarioAnalysisResult> severeScenarios =
                scenarioService.getMostSevereScenarios(results, 3);

        severeScenarios.forEach(result -> {
            System.out.printf("• %s (%s):%n", result.getScenario().getName(), result.getOverallSeverity());
            System.out.printf("  NII Impact: %.1f%%, EVE Impact: %.1f%%, EL Impact: %.1f%%%n",
                    result.getNiiImpact() * 100, result.getEveImpact() * 100, result.getElImpact() * 100);
        });
        System.out.println();
    }

    private void printDetailedScenarioAnalysis(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        System.out.println("4. DETAILED SCENARIO ANALYSIS");
        System.out.println("=============================");

        results.forEach((name, result) -> {
            System.out.printf("\nScenario: %s (%s)%n", name, result.getOverallSeverity());
            System.out.printf("NII: $%,.2f → $%,.2f (Δ%.1f%%)%n",
                    result.getBaseNii(), result.getScenarioNii(), result.getNiiImpact() * 100);
            System.out.printf("EVE: $%,.2f → $%,.2f (Δ%.1f%%)%n",
                    result.getBaseEve(), result.getScenarioEve(), result.getEveImpact() * 100);
            System.out.printf("EL: $%,.2f → $%,.2f (Δ%.1f%%)%n",
                    result.getBaseEl(), result.getScenarioEl(), result.getElImpact() * 100);
            System.out.printf("LCR: %.2f → %.2f%n", result.getBaseLcr(), result.getScenarioLcr());
            System.out.printf("NSFR: %.2f → %.2f%n", result.getBaseNsfr(), result.getScenarioNsfr());
        });
        System.out.println();
    }

    private void printRecommendations(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        System.out.println("5. RECOMMENDATIONS & MITIGATION STRATEGIES");
        System.out.println("===========================================");

        // Analyze common vulnerabilities across scenarios
        if (isNiiVulnerable(results)) {
            System.out.println("• Implement interest rate hedging strategies");
        }
        if (isElVulnerable(results)) {
            System.out.println("• Increase credit loss provisions");
            System.out.println("• Enhance underwriting standards");
        }
        if (isLiquidityVulnerable(results)) {
            System.out.println("• Increase high-quality liquid assets");
            System.out.println("• Extend liability maturities");
        }
        if (isCapitalVulnerable(results)) {
            System.out.println("• Raise additional capital");
            System.out.println("• Reduce risk-weighted assets");
        }
    }

    private boolean isNiiVulnerable(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        return results.values().stream()
                .anyMatch(r -> r.getNiiImpact() < -0.15);
    }

    private boolean isElVulnerable(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        return results.values().stream()
                .anyMatch(r -> r.getElImpact() > 0.30);
    }

    private boolean isLiquidityVulnerable(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        return results.values().stream()
                .anyMatch(r -> r.getLcrImpact() < -0.10 || r.getNsfrImpact() < -0.08);
    }

    private boolean isCapitalVulnerable(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        return results.values().stream()
                .anyMatch(r -> r.getEveImpact() < -0.10);
    }

    private void printVulnerabilityAnalysis(Map<String, ScenarioAnalysisService.ScenarioAnalysisResult> results) {
        // Implementation for vulnerability analysis
    }

    private int compareSeverity(String severity1, String severity2) {
        Map<String, Integer> severityRank = Map.of("SEVERE", 4, "HIGH", 3, "MODERATE", 2, "LOW", 1);
        return Integer.compare(severityRank.getOrDefault(severity1, 0), severityRank.getOrDefault(severity2, 0));
    }

    // Usage example

}
