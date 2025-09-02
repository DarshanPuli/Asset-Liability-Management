package org.example.entity;
import java.sql.Timestamp;
import java.util.UUID;

public class MaturityBucket {

    private UUID bucketID;
    private String bucketName;
    private int startRange;
    private int endRange;
    private String description;
    private Timestamp creationDate;
    private Timestamp lastUpdated;

    // Getters and Setters

    public UUID getBucketID() {
        return bucketID;
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

