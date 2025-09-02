package org.example.entity;
import java.sql.Date;
import java.util.UUID;

public class BucketGap {

    private UUID gapID;
    private UUID bucketID;
    private float totalAssetsValue;
    private float totalLiabilitiesValue;
    private float netGap;
    private Date calculationDate;
    private String gapType;

    // Getters and Setters

    public UUID getGapID() {
        return gapID;
    }

    public void setGapID(UUID gapID) {
        this.gapID = gapID;
    }

    public UUID getBucketID() {
        return bucketID;
    }

    public void setBucketID(UUID bucketID) {
        this.bucketID = bucketID;
    }

    public float getTotalAssetsValue() {
        return totalAssetsValue;
    }

    public void setTotalAssetsValue(float totalAssetsValue) {
        this.totalAssetsValue = totalAssetsValue;
    }

    public float getTotalLiabilitiesValue() {
        return totalLiabilitiesValue;
    }

    public void setTotalLiabilitiesValue(float totalLiabilitiesValue) {
        this.totalLiabilitiesValue = totalLiabilitiesValue;
    }

    public float getNetGap() {
        return netGap;
    }

    public void setNetGap(float netGap) {
        this.netGap = netGap;
    }

    public Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }

    public String getGapType() {
        return gapType;
    }

    public void setGapType(String gapType) {
        this.gapType = gapType;
    }
}

