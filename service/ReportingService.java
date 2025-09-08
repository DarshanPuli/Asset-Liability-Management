package org.example.service;

// src/com/alm/service/ReportingService.java

import org.example.model.Asset;
import org.example.model.FinancialInstrument;
import org.example.model.Liability;
import org.example.model.Loan;

import java.util.*;
import java.util.stream.Collectors;

public class ReportingService {

    public void generateMaturityProfileReport(List<Asset> assets, List<Liability> liabilities) {
        System.out.println("=== MATURITY PROFILE REPORT ===");

        System.out.println("\nAssets Maturity Profile:");
        printMaturityProfile(assets);

        System.out.println("\nLiabilities Maturity Profile:");
        printMaturityProfile(liabilities);
    }

    // In ReportingService class
    public void generateMatchingReport(List<Asset> assets, List<Liability> liabilities) {
        MatchingService matchingService = new MatchingService();
        MatchingService.MatchingResult result = matchingService.analyzeMatching(assets, liabilities);

        System.out.println("=== ASSET-LIABILITY MATCHING REPORT ===");
        System.out.printf("Currency Matching: %.1f%%%n", result.getCurrencyMatchScore());
        System.out.printf("Duration Matching: %.1f%%%n", result.getDurationMatchScore());
        System.out.printf("Maturity Matching: %.1f%%%n", result.getMaturityMatchScore());
        System.out.printf("Rate Type Matching: %.1f%%%n", result.getRateTypeMatchScore());
        System.out.printf("Overall Assessment: %s%n", result.getOverallAssessment());

        System.out.println("\n=== RECOMMENDATIONS ===");
        List<String> suggestions = matchingService.getMatchingSuggestions(assets, liabilities);
        suggestions.forEach(s -> System.out.println("• " + s));

        // Show detailed currency exposure
        System.out.println("\n=== CURRENCY EXPOSURE DETAILS ===");
        printCurrencyExposure(assets, liabilities);

        // Show maturity profile
        System.out.println("\n=== MATURITY PROFILE ===");
        generateMaturityProfileReport(assets, liabilities);
    }

    private void printMaturityProfile(List<? extends FinancialInstrument> instruments) {
        Map<String, Double> maturityBuckets = Map.of(
                "0-30 days", sumByMaturity(instruments, 0, 30),
                "31-90 days", sumByMaturity(instruments, 31, 90),
                "91-365 days", sumByMaturity(instruments, 91, 365),
                "1-3 years", sumByMaturity(instruments, 366, 1095),
                "3+ years", sumByMaturity(instruments, 1096, Integer.MAX_VALUE)
        );

        maturityBuckets.forEach((bucket, amount) -> {
            System.out.printf("%-12s: %,.2f\n", bucket, amount);
        });
    }

    // Add this method to the ReportingService class

    public void printCurrencyExposure(List<Asset> assets, List<Liability> liabilities) {
        // Calculate currency exposure for assets
        Map<String, Double> assetCurrencyExposure = new HashMap<>();
        for (Asset asset : assets) {
            String currency = asset.getCurrency();
            assetCurrencyExposure.put(currency,
                    assetCurrencyExposure.getOrDefault(currency, 0.0) + asset.getPrincipalAmount());
        }

        // Calculate currency exposure for liabilities
        Map<String, Double> liabilityCurrencyExposure = new HashMap<>();
        for (Liability liability : liabilities) {
            String currency = liability.getCurrency();
            liabilityCurrencyExposure.put(currency,
                    liabilityCurrencyExposure.getOrDefault(currency, 0.0) + liability.getPrincipalAmount());
        }

        // Get all unique currencies
        Set<String> allCurrencies = new HashSet<>();
        allCurrencies.addAll(assetCurrencyExposure.keySet());
        allCurrencies.addAll(liabilityCurrencyExposure.keySet());

        // Print currency exposure table
        System.out.println("Currency | Assets     | Liabilities | Net Exposure | Status");
        System.out.println("---------|------------|-------------|--------------|--------");

        for (String currency : allCurrencies) {
            double assetAmount = assetCurrencyExposure.getOrDefault(currency, 0.0);
            double liabilityAmount = liabilityCurrencyExposure.getOrDefault(currency, 0.0);
            double netExposure = assetAmount - liabilityAmount;

            String status;
            if (Math.abs(netExposure) < 1000) { // Within tolerance
                status = "MATCHED";
            } else if (netExposure > 0) {
                status = "LONG (More Assets)";
            } else {
                status = "SHORT (More Liabilities)";
            }

            System.out.printf("%-8s | %10.2f | %11.2f | %12.2f | %s%n",
                    currency, assetAmount, liabilityAmount, netExposure, status);
        }

        // Calculate overall matching percentage
        double totalMatched = 0.0;
        double totalExposure = 0.0;

        for (String currency : allCurrencies) {
            double assetAmount = assetCurrencyExposure.getOrDefault(currency, 0.0);
            double liabilityAmount = liabilityCurrencyExposure.getOrDefault(currency, 0.0);
            totalExposure += Math.max(assetAmount, liabilityAmount);
            totalMatched += Math.min(assetAmount, liabilityAmount);
        }

        double matchPercentage = totalExposure > 0 ? (totalMatched / totalExposure) * 100 : 100;
        System.out.printf("%nOverall Currency Matching: %.1f%%%n", matchPercentage);
    }

    private double sumByMaturity(List<? extends FinancialInstrument> instruments, int minDays, int maxDays) {
        return instruments.stream()
                .filter(i -> {
                    long days = i.getDaysToMaturity();
                    return days >= minDays && days <= maxDays;
                })
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }

    public void generateRiskMetricsReport(InterestRateRiskService irrService,
                                          CreditRiskService crService,
                                          List<Asset> assets, List<Liability> liabilities,
                                          List<Loan> loans) {
        System.out.println("\n=== RISK METRICS REPORT ===");

        double duration = irrService.calculatePortfolioDuration(assets, liabilities, 0.05);
        double nii = irrService.calculateNetInterestIncome(assets, liabilities);
        double expectedLoss = crService.calculateTotalExpectedLoss(loans);
        double rwa = crService.calculateRiskWeightedAssets(assets);

        System.out.printf("Portfolio Duration: %.2f years\n", duration);
        System.out.printf("Net Interest Income: %,.2f\n", nii);
        System.out.printf("Total Expected Loss: %,.2f\n", expectedLoss);
        System.out.printf("Risk Weighted Assets: %,.2f\n", rwa);
    }

    public void generateCreditQualityReport(List<Loan> loans) {
        System.out.println("\n=== CREDIT QUALITY REPORT ===");

        Map<Integer, Double> exposureByRating = loans.stream()
                .collect(Collectors.groupingBy(
                        Loan::getCreditRating,
                        Collectors.summingDouble(Loan::getPrincipalAmount)
                ));

        System.out.println("Exposure by Credit Rating:");
        exposureByRating.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    System.out.printf("Rating %d: %,.2f\n", entry.getKey(), entry.getValue());
                });
    }
    // In ReportingService

}