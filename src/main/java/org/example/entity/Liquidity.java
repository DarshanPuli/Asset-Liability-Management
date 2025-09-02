package entity;



import db.LiquidityDB;

import java.util.Date;

public class Liquidity {
    private static LiquidityDB liquidityDBInstance = LiquidityDB.getInstance();

    private String liquidityId;
    private Date assessmentDate;
    private double cashFlowNet;
    private double cashReserve;
    private double avgInterest;
    private double currentRatio;
    private double quickRatio;
    private double liquidityCoverageRatio;
    private double netStableFundingRatio;
    private double totalLiquidAssets;
    private double totalShortTermLiabilities;
    private double liquidityGapShortTerm;
    private String bucketId;
    private String scenarioId;
    private String description;

    public Liquidity(String liquidityId,
                     Date assessmentDate,
                     double cashFlowNet,
                     double cashReserve,
                     double avgInterest,
                     double currentRatio,
                     double quickRatio,
                     double liquidityCoverageRatio,
                     double netStableFundingRatio,
                     double totalLiquidAssets,
                     double totalShortTermLiabilities,
                     double liquidityGapShortTerm,
                     String bucketId,
                     String scenarioId,
                     String description) {

        this.liquidityId = liquidityId;
        this.assessmentDate = assessmentDate;
        this.cashFlowNet = cashFlowNet;
        this.cashReserve = cashReserve;
        this.avgInterest = avgInterest;
        this.currentRatio = currentRatio;
        this.quickRatio = quickRatio;
        this.liquidityCoverageRatio = liquidityCoverageRatio;
        this.netStableFundingRatio = netStableFundingRatio;
        this.totalLiquidAssets = totalLiquidAssets;
        this.totalShortTermLiabilities = totalShortTermLiabilities;
        this.liquidityGapShortTerm = liquidityGapShortTerm;
        this.bucketId = bucketId;
        this.scenarioId = scenarioId;
        this.description = description;

        addLiquidityToDB(this);
    }

    public void addLiquidityToDB(Liquidity liquidity) {
        liquidityDBInstance.addLiquidity(liquidity);
    }


    public double calculateCurrentRatio(double currentAssets, double currentLiabilities) {
        if (currentLiabilities == 0) return 0.0;
        this.currentRatio = currentAssets / currentLiabilities;
        return this.currentRatio;
    }


    public double calculateQuickRatio(double cash, double marketableSecurities, double currentLiabilities) {
        if (currentLiabilities == 0) return 0.0;
        this.quickRatio = (cash + marketableSecurities) / currentLiabilities;
        return this.quickRatio;
    }


    public double calculateLCR(double highQualityLiquidAssets, double netCashOutflows30Days) {
        if (netCashOutflows30Days == 0) return 0.0;
        this.liquidityCoverageRatio = highQualityLiquidAssets / netCashOutflows30Days;
        return this.liquidityCoverageRatio;
    }


    public double calculateNSFR(double availableStableFunding, double requiredStableFunding) {
        if (requiredStableFunding == 0) return 0.0;
        this.netStableFundingRatio = availableStableFunding / requiredStableFunding;
        return this.netStableFundingRatio;
    }


    public double calculateLiquidityGap(double shortTermAssets, double shortTermLiabilities) {
        this.liquidityGapShortTerm = shortTermAssets - shortTermLiabilities;
        return this.liquidityGapShortTerm;
    }



    public String getLiquidityId() { return liquidityId; }
    public void setLiquidityId(String liquidityId) { this.liquidityId = liquidityId; }

    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }

    public double getCashFlowNet() { return cashFlowNet; }
    public void setCashFlowNet(double cashFlowNet) { this.cashFlowNet = cashFlowNet; }

    public double getCashReserve() { return cashReserve; }
    public void setCashReserve(double cashReserve) { this.cashReserve = cashReserve; }

    public double getAvgInterest() { return avgInterest; }
    public void setAvgInterest(double avgInterest) { this.avgInterest = avgInterest; }

    public double getCurrentRatio() { return currentRatio; }
    public void setCurrentRatio(double currentRatio) { this.currentRatio = currentRatio; }

    public double getQuickRatio() { return quickRatio; }
    public void setQuickRatio(double quickRatio) { this.quickRatio = quickRatio; }

    public double getLiquidityCoverageRatio() { return liquidityCoverageRatio; }
    public void setLiquidityCoverageRatio(double liquidityCoverageRatio) { this.liquidityCoverageRatio = liquidityCoverageRatio; }

    public double getNetStableFundingRatio() { return netStableFundingRatio; }
    public void setNetStableFundingRatio(double netStableFundingRatio) { this.netStableFundingRatio = netStableFundingRatio; }

    public double getTotalLiquidAssets() { return totalLiquidAssets; }
    public void setTotalLiquidAssets(double totalLiquidAssets) { this.totalLiquidAssets = totalLiquidAssets; }

    public double getTotalShortTermLiabilities() { return totalShortTermLiabilities; }
    public void setTotalShortTermLiabilities(double totalShortTermLiabilities) { this.totalShortTermLiabilities = totalShortTermLiabilities; }

    public double getLiquidityGapShortTerm() { return liquidityGapShortTerm; }
    public void setLiquidityGapShortTerm(double liquidityGapShortTerm) { this.liquidityGapShortTerm = liquidityGapShortTerm; }

    public String getBucketId() { return bucketId; }
    public void setBucketId(String bucketId) { this.bucketId = bucketId; }

    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

