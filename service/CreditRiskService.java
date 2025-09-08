package org.example.service;

// src/com/alm/service/CreditRiskService.java

import org.example.model.Asset;
import org.example.model.Loan;

import java.util.List;

public class CreditRiskService {
    //used to calculate total losses we might expect
    public double calculateTotalExpectedLoss(List<Loan> loans) {
        return loans.stream()
                .mapToDouble(Loan::calculateExpectedLoss)
                .sum();
    }
    //This is needed for regulatory capital calculation.
    public double calculateRiskWeightedAssets(List<Asset> assets) {
        return assets.stream()
                .mapToDouble(this::calculateRiskWeight)
                .sum();
    }

    private double calculateRiskWeight(Asset asset) {
        if (asset instanceof Loan) {
            Loan loan = (Loan) asset;
            // Higher risk weight for lower credit ratings
            double riskWeight = 1.0 - (loan.getCreditRating() / 10.0);
            return asset.getPrincipalAmount() * Math.max(0.2, riskWeight);
        }

        // Lower risk weight for non-loan assets
        return asset.getPrincipalAmount() * 0.2;
    }

    public double calculateCapitalAdequacyRatio(double capital, double riskWeightedAssets) {
        return riskWeightedAssets > 0 ? capital / riskWeightedAssets : Double.POSITIVE_INFINITY;
    }

    public double calculateProbabilityOfDefault(List<Loan> loans, int rating) {
        long count = loans.stream()
                .filter(loan -> loan.getCreditRating() == rating)
                .count();

        long defaulted = loans.stream()
                .filter(loan -> loan.getCreditRating() == rating)
                .filter(loan -> loan.getProbabilityOfDefault() > 0.5) // Simplified default condition
                .count();

        return count > 0 ? (double) defaulted / count : 0;
    }
}