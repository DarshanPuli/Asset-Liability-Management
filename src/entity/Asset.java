package entity;

import db.AssetDB;
import enums.CreditRating;
import enums.RateType;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

//add count param -> update count everytime an asset is created
//remove creditRating
//remove counterPartyId
//add assedId to counterParty

public class Asset {
    private static AssetDB assetDBInstance = AssetDB.getInstance();
    private String assetId;
    private String assetType;
    private double pricipleAmount;
    private double interestRate;
    private RateType rateType;
    private Date maturityDate;
    private Optional<Date> repricingDate;
    private Currency currency;
    private String maturityBucketId;
    private Date lastUpdated;
    private int count = 0;

    public Asset(String assetId, String assetType, double pricipleAmount, double interestRate, RateType rateType, Date maturityDate, Optional<Date> repricingDate, Currency currency, String maturityBucketId, Date lastUpdated) {
        this.assetId = assetId;
        this.assetType = assetType;
        this.pricipleAmount = pricipleAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
        this.repricingDate = repricingDate;
        this.currency = currency;
        this.maturityBucketId = maturityBucketId;
        this.lastUpdated = lastUpdated;

        addAssetToDB(this);
    }

    public void addAssetToDB(Asset asset){
        assetDBInstance.addAsset(asset);
    }

//    public int updateCount(String assetId){
//        int count = 0;
//        Asset asset = assetDBInstance.getAsset(assetId);
//        asset.count+=1;
//        //update db
//        return count;
//    }

    public int updateCount(){
        this.count+=1;
        //update db
        return this.count;
    }

    //add to maturity bucket
    public Asset addAssetToMaturityBucket(){
        //find maturity bucket corresponding to maturity date
        //String bucketId = MaturityBucket.find(maturityDate);
        //this.maturityBucketId = bucketId;
        return this;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public double getPricipleAmount() {
        return pricipleAmount;
    }

    public void setPricipleAmount(double pricipleAmount) {
        this.pricipleAmount = pricipleAmount;
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
