package org.example.util.schema;

public class TableSchemas {

    public static final String CREATE_COUNTERPARTY = """
        CREATE TABLE CounterParty (
            CounterPartyID VARCHAR2(36) PRIMARY KEY,
            Name VARCHAR2(100) NOT NULL,
            RiskRating VARCHAR2(20),
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

    public static final String CREATE_ASSET = """
        CREATE TABLE Asset (
            AssetID VARCHAR2(36) PRIMARY KEY,
            Type VARCHAR2(50) NOT NULL,
            PrincipalAmount NUMBER(15,2) CHECK (PrincipalAmount >= 0),
            InterestRate NUMBER(5,2) CHECK (InterestRate BETWEEN 0 AND 100),
            CounterPartyID VARCHAR2(36) NOT NULL,
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_asset_counterparty FOREIGN KEY (CounterPartyID)
                REFERENCES CounterParty(CounterPartyID) ON DELETE CASCADE
        )
        """;

    public static final String CREATE_LIABILITY = """
        CREATE TABLE Liability (
            LiabilityID VARCHAR2(36) PRIMARY KEY,
            Type VARCHAR2(50) NOT NULL,
            Amount NUMBER(15,2) CHECK (Amount >= 0),
            InterestRate NUMBER(5,2) CHECK (InterestRate BETWEEN 0 AND 100),
            CounterPartyID VARCHAR2(36) NOT NULL,
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_liability_counterparty FOREIGN KEY (CounterPartyID)
                REFERENCES CounterParty(CounterPartyID) ON DELETE CASCADE
        )
        """;

    public static final String CREATE_MATURITY_BUCKET = """
        CREATE TABLE MaturityBucket (
            BucketID VARCHAR2(36) PRIMARY KEY,
            Name VARCHAR2(50) NOT NULL UNIQUE,
            StartDay NUMBER NOT NULL CHECK (StartDay >= 0),
            EndDay NUMBER NOT NULL CHECK (EndDay >= 0),
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT chk_maturity_range CHECK (EndDay >= StartDay)
        )
        """;

    public static final String CREATE_BUCKET_GAP = """
        CREATE TABLE BucketGap (
            GapID VARCHAR2(36) PRIMARY KEY,
            BucketID VARCHAR2(36) NOT NULL,
            GapAmount NUMBER(15,2) NOT NULL,
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_gap_bucket FOREIGN KEY (BucketID)
                REFERENCES MaturityBucket(BucketID) ON DELETE CASCADE
        )
        """;

    public static final String CREATE_LIQUIDITY = """
        CREATE TABLE Liquidity (
            LiquidityID VARCHAR2(36) PRIMARY KEY,
            AssetID VARCHAR2(36) NOT NULL,
            BucketID VARCHAR2(36) NOT NULL,
            Amount NUMBER(15,2) NOT NULL CHECK (Amount >= 0),
            CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_liquidity_asset FOREIGN KEY (AssetID)
                REFERENCES Asset(AssetID) ON DELETE CASCADE,
            CONSTRAINT fk_liquidity_bucket FOREIGN KEY (BucketID)
                REFERENCES MaturityBucket(BucketID) ON DELETE CASCADE
        )
        """;
}