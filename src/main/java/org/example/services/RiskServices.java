package org.example.services;
import java.util.*;

import org.example.entity.*;
import org.example.enums.CreditRating;
import org.example.enums.RateType;

public class RiskServices {

    public double calculateInterestRisk(List<Asset> assets, List<Liability> liabilities, double newRate) {
        double assetImpact = assets.stream()
                .mapToDouble(asset -> asset.getprincipalAmount() * (newRate - asset.getInterestRate()))
                .sum();

        double liabilityImpact = liabilities.stream()
                .mapToDouble(liability -> liability.getPrincipleAmount() * (newRate - liability.getInterestRate()))
                .sum();

        return assetImpact - liabilityImpact; // Net Interest Rate Risk
    }
    /**
     * Liquidity Risk:
     * Formula: (Total Liquid Assets - Short Term Liabilities) / Short Term Liabilities
     */
    public double calculateLiquidityRisk(Liquidity liquidity) {
        return (liquidity.getTotalLiquidAssets() - liquidity.getTotalShortTermLiabilities())
                / liquidity.getTotalShortTermLiabilities();
    }

    /**
     * Credit Risk:
     * Formula: Exposure × Probability of Default (PD) × Loss Given Default (LGD)
     */
    public double calculateCreditRisk(CounterParty counterParty, Asset asset) {
        double exposure = asset.getprincipalAmount();
        double pd = getProbabilityOfDefault(counterParty.getCreditRating());
        double lgd = 0.45; // 45% LGD assumption
        return exposure * pd * lgd;
    }

    private double getProbabilityOfDefault(CreditRating rating) {
        switch (rating) {
            //case AAA: return 0.001; // 0.1%
            //case AA: return 0.002;
            case LOW: return 0.005;
           // case BBB: return 0.01;
            case MID: return 0.05;
           // case B: return 0.10;
            case HIGH: return 0.20;
            default: return 0.30;
        }
    }
}
