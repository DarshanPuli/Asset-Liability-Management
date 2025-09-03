package org.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class Asset {
    private final UUID assetId;
    private String assetType;
    private double interestRate;
    private String rateType;
    private int monthsToExpiry;
    private LocalDate repricingDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int count = 0;

    public Asset(String assetType, double interestRate, String rateType, int monthsToExpiry, LocalDate repricingDate) {
        this.assetId = UUID.randomUUID();
        this.assetType = assetType;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.monthsToExpiry = monthsToExpiry;
        this.repricingDate = repricingDate;
    }

    public Asset() {
        this.assetId = UUID.randomUUID();
    }

    public void helloWorld(){
        System.out.println("Hello World");
    }

    public UUID getAssetId() {
        return assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public LocalDate getRepricingDate() {
        return repricingDate;
    }

    public void setRepricingDate(LocalDate repricingDate) {
        this.repricingDate = repricingDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setMonthsToExpiry(int monthsToExpiry) {
        this.monthsToExpiry = monthsToExpiry;
    }

    public int getMonthsToExpiry() {
        return monthsToExpiry;
    }
}
