package entity;

import java.util.UUID;

import db.LiabilityDB;
import enums.CreditRating;
import enums.RateType;

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
    private RateType rateType;
    private Date maturityDate;
    private Optional<Date> repricingDate;
    private Currency currency;
    private String counterPartyId;
    private String maturityBucketId;
    private Date lastUpdated;
    private int count = 0;

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public Liability(String liabilityType, double principleAmount, double interestRate, RateType rateType, Date maturityDate, Optional<Date> repricingDate, Currency currency,String counterPartyId ,String maturityBucketId, Date lastUpdated) {
        this.liabilityId = UUID.randomUUID();
        this.liabilityType = liabilityType;
        this.principleAmount = principleAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
        this.repricingDate = repricingDate;
        this.currency = currency;
        this.counterPartyId= counterPartyId;
        this.maturityBucketId = maturityBucketId;
        this.lastUpdated = lastUpdated;

        //addLiabilitytoDB(this);
    }

  //  public void addLiabilityToDB(Liability liability){
  //      liabilityDBInstance.addLiability(liability);
    //}

//    public int updateCount(String liabilityId){
//        int count = 0;
//        Liability liability = liabilityDBInstance.getLiability(liabilityId);
//        liability.count+=1;
//        //update db
//        return count;
//    }

    public int updateCount(){
        this.count+=1;
        //update db
        return this.count;
    }
    //add to maturity bucket
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
