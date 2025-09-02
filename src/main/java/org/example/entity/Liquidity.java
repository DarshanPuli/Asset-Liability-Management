package org.example.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Liquidity {

    private UUID liquidityId;
    private Date assessmentDate;
    private double cashFlowNet;
    private double cashReserve;
    private double avgInterest;
    private double currentRatio;
    private double quickRatio;
    private double totalLiquidAssets;
    private double totalShortTermLiabilities;
    private UUID bucketId;
    private String scenarioId;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Liquidity(Date assessmentDate, double cashFlowNet, double cashReserve, double avgInterest, double currentRatio, double quickRatio,  double totalLiquidAssets, double totalShortTermLiabilities,  UUID bucketId, String scenarioId, String description) {
        this.liquidityId = UUID.randomUUID();
        this.assessmentDate = assessmentDate;
        this.cashFlowNet = cashFlowNet;
        this.cashReserve = cashReserve;
        this.avgInterest = avgInterest;
        this.currentRatio = currentRatio;
        this.quickRatio = quickRatio;
        this.totalLiquidAssets = totalLiquidAssets;
        this.totalShortTermLiabilities = totalShortTermLiabilities;
        this.bucketId = bucketId;  // check
        this.scenarioId = scenarioId;
        this.description = description;
    }

    public UUID getLiquidityId() { return liquidityId; }

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

    public double getTotalLiquidAssets() { return totalLiquidAssets; }
    public void setTotalLiquidAssets(double totalLiquidAssets) { this.totalLiquidAssets = totalLiquidAssets; }

    public double getTotalShortTermLiabilities() { return totalShortTermLiabilities; }
    public void setTotalShortTermLiabilities(double totalShortTermLiabilities) { this.totalShortTermLiabilities = totalShortTermLiabilities; }

    public UUID getBucketId() { return bucketId; }

    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String scenarioId) { this.scenarioId = scenarioId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

}