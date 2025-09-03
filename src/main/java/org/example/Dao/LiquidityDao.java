package org.example.Dao;

import org.example.entity.Liquidity;

public interface LiquidityDao {

//    public void addLiquidityToDB(Liquidity liquidity) {
//        liquidityDBInstance.addLiquidity(liquidity);
//    }
//
//
//    public double calculateCurrentRatio(double currentAssets, double currentLiabilities) {
//        if (currentLiabilities == 0) return 0.0;
//        this.currentRatio = currentAssets / currentLiabilities;
//        return this.currentRatio;
//    }
//
//
//    public double calculateQuickRatio(double cash, double marketableSecurities, double currentLiabilities) {
//        if (currentLiabilities == 0) return 0.0;
//        this.quickRatio = (cash + marketableSecurities) / currentLiabilities;
//        return this.quickRatio;
//    }
//
//
//    public double calculateLCR(double highQualityLiquidAssets, double netCashOutflows30Days) {
//        if (netCashOutflows30Days == 0) return 0.0;
//        this.liquidityCoverageRatio = highQualityLiquidAssets / netCashOutflows30Days;
//        return this.liquidityCoverageRatio;
//    }
//
//
//    public double calculateNSFR(double availableStableFunding, double requiredStableFunding) {
//        if (requiredStableFunding == 0) return 0.0;
//        this.netStableFundingRatio = availableStableFunding / requiredStableFunding;
//        return this.netStableFundingRatio;
//    }
//
//
//    public double calculateLiquidityGap(double shortTermAssets, double shortTermLiabilities) {
//        this.liquidityGapShortTerm = shortTermAssets - shortTermLiabilities;
//        return this.liquidityGapShortTerm;
//    }

}
