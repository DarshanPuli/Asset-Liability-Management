package org.example.entity;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;


@Data
public class Asset {
    private final UUID assetId;
    private String assetName;
    private double interestRate;
    private String rateType;
    private LocalDate repricingDate;
    private String quality;

    public Asset(){
        this.assetId = UUID.randomUUID();
    }

    public Asset(String assetName, double interestRate, String rateType, LocalDate repricingDate, String quality) {
        this();
        this.assetName = assetName;
        this.interestRate = interestRate;
        this.rateType = rateType;
        this.repricingDate = repricingDate;
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", interestRate=" + interestRate +
                ", rateType='" + rateType + '\'' +
                ", repricingDate=" + repricingDate +
                ", quality='" + quality + '\'' +
                '}';
    }
}
