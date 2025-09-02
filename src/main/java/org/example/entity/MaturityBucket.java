package org.example.entity;
import java.sql.Timestamp;
import java.util.UUID;

public class MaturityBucket {

    private UUID bucketID;
    private String bucketName;
    private int startRange;
    private int endRange;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public MaturityBucket(){}

    public MaturityBucket(String bucketName, int startRange, int endRange, String description) {
        this.bucketID = UUID.randomUUID();
        this.bucketName = bucketName;
        this.startRange = startRange;
        this.endRange = endRange;
        this.description = description;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public UUID getBucketID() {
        return bucketID;
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

    public Timestamp getCreationDate() {
        return createdAt;
    }

    public Timestamp getLastUpdated() {
        return updatedAt;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.updatedAt = lastUpdated;
    }
}

