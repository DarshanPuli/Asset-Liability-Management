package org.example.entity;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class BucketGap {

    private UUID gapID;
    private UUID bucketID;
    private float totalAssetsValue;
    private float totalLiabilitiesValue;
    private float netGap;
    private Date calculationDate;
    private String gapType;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public BucketGap() {}

    public BucketGap(float totalAssetsValue, float totalLiabilitiesValue, float netGap, Date calculationDate, String gapType) {
        this.gapID = UUID.randomUUID();
        this.bucketID = UUID.randomUUID();
        this.totalAssetsValue = totalAssetsValue;
        this.totalLiabilitiesValue = totalLiabilitiesValue;
        this.netGap = netGap;
        this.calculationDate = calculationDate;
        this.gapType = gapType;
    }

    public UUID getGapID() {
        return gapID;
    }

    public UUID getBucketID() {
        return bucketID;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

}

