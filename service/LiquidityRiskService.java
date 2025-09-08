package org.example.service;

// src/com/alm/service/LiquidityRiskService.java

import org.example.model.Asset;
import org.example.model.Liability;

import java.util.List;
import java.util.Map;

public class LiquidityRiskService {

    public Map<String, Double> calculateMaturityLadder(List<Asset> assets, List<Liability> liabilities) {
        // Group by time buckets (0-30 days, 31-90 days, 91-365 days, 1-3 years, 3+ years)
        //For each bucket → calculates net cash flow = Assets – Liabilities maturing in that range.
        return Map.of(
                "0-30 days", calculateNetCashFlow(assets, liabilities, 0, 30),
                "31-90 days", calculateNetCashFlow(assets, liabilities, 31, 90),
                "91-365 days", calculateNetCashFlow(assets, liabilities, 91, 365),
                "1-3 years", calculateNetCashFlow(assets, liabilities, 366, 1095),
                "3+ years", calculateNetCashFlow(assets, liabilities, 1096, Integer.MAX_VALUE)
        );
    }

    private double calculateNetCashFlow(List<Asset> assets, List<Liability> liabilities, int minDays, int maxDays) {
        double assetCashFlow = assets.stream()
                .filter(a -> {
                    long days = a.getDaysToMaturity();
                    return days >= minDays && days <= maxDays;
                })
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        double liabilityCashFlow = liabilities.stream()
                .filter(l -> {
                    long days = l.getDaysToMaturity();
                    return days >= minDays && days <= maxDays;
                })
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        return assetCashFlow - liabilityCashFlow;
    }

    public double calculateLiquidityCoverageRatio(List<Asset> highQualityLiquidAssets,
                                                  List<Liability> expectedNetCashOutflows) {
        double hqla = highQualityLiquidAssets.stream()
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        double netOutflows = expectedNetCashOutflows.stream()
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        return netOutflows > 0 ? hqla / netOutflows : Double.POSITIVE_INFINITY;
    }

    public double calculateNetStableFundingRatio(List<Asset> requiredStableFundingAssets,
                                                 List<Liability> availableStableFunding) {
        double rsf = requiredStableFundingAssets.stream()
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        double asf = availableStableFunding.stream()
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        return rsf > 0 ? asf / rsf : Double.POSITIVE_INFINITY;
    }
}
