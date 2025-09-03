package org.example.entity;
import org.example.enums.CreditRating;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class CounterParty {

    private final UUID counterPartyId;
    private double principalAmount;
    private final UUID assetId;
    private final UUID liabilityId;
    private String name;
    private String type;
    private CreditRating creditRating;
    private long phoneNumber;
    private String country;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public CounterParty(UUID assetId, UUID liabilityId, String name, String type, CreditRating creditRating, long phoneNumber, String country,double principalAmount) {
        this.counterPartyId = UUID.randomUUID();
        this.assetId = assetId;
        this.liabilityId = liabilityId;
        this.name = name;
        this.type = type;
        this.creditRating = creditRating;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.principalAmount = principalAmount;
    }

    public UUID getCounterPartyId() {
        return counterPartyId;
    }

    public UUID getAssetId() {
        return assetId;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public double getPrincipalAmount() {
        return this.principalAmount;
    }
    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }
}
