package org.example.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import org.example.db.LiabilityDB;
import org.example.enums.CreditRating;
import org.example.enums.RateType;


import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

//add count param -> update count everytime an liability is created
//remove creditRating
//remove counterPartyId
//add assedId to counterParty

public class Liability {
    //private static LiabilityDB liabilityDBInstance = LiabilityDB.getInstance();
    private final UUID liabilityId;
    private String liabilityType;
    private double principleAmount;
    private double interestRate;
    private String rateType;
    private Date maturityDate;
    private LocalDate repricingDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private int count = 0;

    public Liability(String liabilityType, double principleAmount, double interestRate, String rateType, Date maturityDate, LocalDate repricingDate, Currency currency,UUID counterPartyId) {
        this.liabilityId = UUID.randomUUID();
        this.liabilityType = liabilityType;
        this.principleAmount = principleAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
        this.repricingDate = repricingDate;
    }

    public int updateCount(){
        this.count+=1;
        //update db
        return this.count;
    }

    public Liability addLiabilityToMaturityBucket(){
        //find maturity bucket corresponding to maturity date
        //String bucketId = MaturityBucket.find(maturityDate);
        //this.maturityBucketId = bucketId;
        return this;
    }

    public UUID getLiabilityId() {
        return liabilityId;
    }

    public String getLiabilityType() {
        return liabilityType;
    }

    public void setLiabilityType(String liabilityType) {
        this.liabilityType = liabilityType;
    }

    public double getPrincipleAmount() {
        return principleAmount;
    }

    public void setPrincipleAmount(double principleAmount) {
        this.principleAmount = principleAmount;
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

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
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
