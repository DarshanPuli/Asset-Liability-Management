package org.example.entity;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

public class MaturityBucket {

    private UUID bucketID;
    private String bucketName;
    private int startRange;
    private int endRange;
    private String description;
    private double totalAssetsValue;
    private double totalLiabilitiesValue;
    private double netGap;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MaturityBucket(){}

    public MaturityBucket(UUID bucketID, String bucketName, int startRange, int endRange, String description, float totalAssetsValue, float totalLiabilitiesValue, float netGap, Timestamp createdAt, Timestamp updatedAt) {
        this.bucketID = bucketID;
        this.bucketName = bucketName;
        this.startRange = startRange;
        this.endRange = endRange;
        this.description = description;
        this.totalAssetsValue = totalAssetsValue;
        this.totalLiabilitiesValue = totalLiabilitiesValue;
        this.netGap = netGap;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getBucketID() {
        return bucketID;
    }

    public void setUuid(UUID bucketID) {
        this.bucketID = bucketID;
    }

    public void setBucketID(UUID bucketID) {
        this.bucketID = bucketID;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getStartRange() {
        return startRange;
    }

    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalAssetsValue() {
        return totalAssetsValue;
    }

    public void setTotalAssetsValue(double totalAssetsValue) {
        this.totalAssetsValue = totalAssetsValue;
    }

    public double getTotalLiabilitiesValue() {
        return totalLiabilitiesValue;
    }

    public void setTotalLiabilitiesValue(double totalLiabilitiesValue) {
        this.totalLiabilitiesValue = totalLiabilitiesValue;
    }

    public double getNetGap() {
        return netGap;
    }

    public void setNetGap(double netGap) {
        this.netGap = netGap;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

