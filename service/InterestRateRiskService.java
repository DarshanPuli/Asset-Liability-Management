package org.example.service;

// src/com/alm/service/InterestRateRiskService.java

import org.example.model.Asset;
import org.example.model.FinancialInstrument;
import org.example.model.Liability;

import java.util.List;

public class InterestRateRiskService {

    public double calculatePortfolioDuration(List<Asset> assets, List<Liability> liabilities, double yield) {
        double assetDuration = calculateWeightedDuration(assets, yield);
        double liabilityDuration = calculateWeightedDuration(liabilities, yield);

        double assetValue = calculateTotalValue(assets);
        double liabilityValue = calculateTotalValue(liabilities);

        return assetDuration - (liabilityValue / assetValue) * liabilityDuration;
    }

    private double calculateWeightedDuration(List<? extends FinancialInstrument> instruments, double yield) {
        double totalValue = calculateTotalValue(instruments);
        if (totalValue == 0) return 0;

        double weightedDuration = 0;
        for (FinancialInstrument instrument : instruments) {
            double instrumentValue = instrument.getPrincipalAmount();
            double duration = instrument.calculateDuration(yield);
            weightedDuration += (instrumentValue / totalValue) * duration;
        }

        return weightedDuration;
    }

    private double calculateTotalValue(List<? extends FinancialInstrument> instruments) {
        return instruments.stream()
                .mapToDouble(FinancialInstrument::getPrincipalAmount)
                .sum();
    }

    public double calculateNetInterestIncome(List<Asset> assets, List<Liability> liabilities) {
        double interestIncome = assets.stream()
                .mapToDouble(a -> a.getPrincipalAmount() * a.getInterestRate())
                .sum();

        double interestExpense = liabilities.stream()
                .mapToDouble(l -> l.getPrincipalAmount() * l.getInterestRate())
                .sum();

        return interestIncome - interestExpense;
    }

    public double calculateGapAnalysis(List<Asset> assets, List<Liability> liabilities, int days) {
        double rateSensitiveAssets = assets.stream()
                .filter(a -> !a.isFixedRate() && a.getDaysToMaturity() <= days)
                .mapToDouble(Asset::getPrincipalAmount)
                .sum();

        double rateSensitiveLiabilities = liabilities.stream()
                .filter(l -> !l.isFixedRate() && l.getDaysToMaturity() <= days)
                .mapToDouble(Liability::getPrincipalAmount)
                .sum();

        return rateSensitiveAssets - rateSensitiveLiabilities;
    }
}