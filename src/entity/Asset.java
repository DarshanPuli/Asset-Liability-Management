package entity;

import enums.CreditRating;
import enums.RateType;

import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class Asset {
    private String assetId;
    private String assetType;
    private double pricipleAmount;
    private double interestRate;
    private RateType rateType;
    private Date maturityDate;
    private Optional<Date> repricingDate;
    private Currency currency;
    private String counterPartyId;
    private String maturityBucketId;
    private CreditRating creditRating;
    private Date lastUpdated;

    public Asset(String assetId, String assetType, double pricipleAmount, double interestRate, RateType rateType, Date maturityDate, Optional<Date> repricingDate, Currency currency, String counterPartyId, String maturityBucketId, CreditRating creditRating, Date lastUpdated) {
        this.assetId = assetId;
        this.assetType = assetType;
        this.pricipleAmount = pricipleAmount;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.maturityDate = maturityDate;
        this.repricingDate = repricingDate;
        this.currency = currency;
        this.counterPartyId = counterPartyId;
        this.maturityBucketId = maturityBucketId;
        this.creditRating = creditRating;
        this.lastUpdated = lastUpdated;
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

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getMaturityBucketId() {
        return maturityBucketId;
    }

    public void setMaturityBucketId(String maturityBucketId) {
        this.maturityBucketId = maturityBucketId;
    }

    public CreditRating getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
