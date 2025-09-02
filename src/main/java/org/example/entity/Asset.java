package org.example.entity;
import org.example.enums.RateType;

import java.sql.Timestamp;
import java.util.*;

public class Asset {
    private final UUID assetId;
    private String assetType;
    private double principalAmount;
    private double interestRate;
    private RateType rateType;
    private Date maturityDate;
    private Optional<Date> repricingDate;
    private Currency currency;
    private String maturityBucketId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int count = 0;

    public Asset(String assetType, double principalAmount, double interestRate, RateType rateType, Date maturityDate, Optional<Date> repricingDate, Currency currency, String maturityBucketId) {
        this.assetId = UUID.randomUUID();
        this.assetType = assetType;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
        this.repricingDate = repricingDate;
        this.currency = currency;
        this.maturityBucketId = maturityBucketId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
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

    public double getprincipalAmount() {
        return principalAmount;
    }

    public void setprincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public RateType getRateType() {
        return rateType;
    }

    public void setRateType(RateType rateType) {
        this.rateType = rateType;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Optional<Date> getRepricingDate() {
        return repricingDate;
    }

    public void setRepricingDate(Optional<Date> repricingDate) {
        this.repricingDate = repricingDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getMaturityBucketId() {
        return maturityBucketId;
    }

    public void setMaturityBucketId(String maturityBucketId) {
        this.maturityBucketId = maturityBucketId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
