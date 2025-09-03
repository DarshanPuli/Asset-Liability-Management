package org.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

public class Asset {
    private final UUID assetId;
    private String assetType;
    private double principalAmount;
    private double interestRate;
    private String rateType;
    private LocalDate maturityDate;
    private LocalDate repricingDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int count = 0;

    public Asset(String assetType, double principalAmount, double interestRate, String rateType, LocalDate maturityDate, LocalDate repricingDate, Currency currency) {
        this.assetId = UUID.randomUUID();
        this.assetType = assetType;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
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

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
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

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
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
}
