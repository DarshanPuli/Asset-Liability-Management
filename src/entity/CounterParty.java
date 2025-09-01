package entity;

import db.AssetDB;
import db.CounterPartyDB;
import enums.CreditRating;

import java.util.Date;

public class CounterParty {

    private CounterPartyDB counterPartyDBInstance = CounterPartyDB.getInstance();

    private String counterPartyId;
    private String assetId;
    private String name;
    private String type;
    private CreditRating creditRating;
    private long phoneNumber;
    private String country;
    private Date creationDate;
    private Date lastUpdated;

    public CounterParty(String counterPartyId, String assetId, String name, String type, CreditRating creditRating, long phoneNumber, String country, Date creationDate, Date lastUpdated) {
        this.counterPartyId = counterPartyId;
        this.assetId = assetId;
        this.name = name;
        this.type = type;
        this.creditRating = creditRating;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.creationDate = creationDate;
        this.lastUpdated = lastUpdated;

        addCounterPartyToDB(this);
        updateCount(this.assetId);
    }

    public void addCounterPartyToDB(CounterParty counterParty){
        counterPartyDBInstance.addCounterParty(counterParty);
    }

    public int updateCount(String assetId){
        AssetDB assetDB = AssetDB.getInstance();
        Asset asset = assetDB.getAsset(assetId);
        return asset.updateCount();
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public void setCounterPartyId(String counterPartyId) {
        this.counterPartyId = counterPartyId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CreditRating getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
