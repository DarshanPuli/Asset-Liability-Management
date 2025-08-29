package entity;

import enums.CreditRating;
import enums.RateType;

import java.util.Currency;
import java.util.Date;
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
}
