package org.example.util.schema;

public class TableSchemas {

    public static final String CREATE_COUNTERPARTY = """
            CREATE TABLE CounterParty (
                CounterPartyID VARCHAR2(36) PRIMARY KEY,
                AssetID VARCHAR2(36),
                LiabilityID VARCHAR2(36),
                Name VARCHAR2(100) NOT NULL,
                Type VARCHAR2(50) NOT  NULL,
                CreditRating VARCHAR2(20),
                PhoneNumber NUMBER(15),
                Country VARCHAR2(100),
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_counterparty_asset FOREIGN KEY (AssetID)
                     REFERENCES Asset(AssetID) ON DELETE SET NULL,
                CONSTRAINT fk_counterparty_liability FOREIGN KEY (LiabilityID)
                     REFERENCES Liability(LiabilityID) ON DELETE SET NULL
            )
        """;

    public static final String CREATE_ASSET = """
            CREATE TABLE Asset (
                 AssetID VARCHAR2(36) PRIMARY KEY,
                 AssetType VARCHAR2(50) NOT NULL,
                 PrincipalAmount NUMBER(15,2) CHECK (PrincipalAmount >= 0),
                 InterestRate NUMBER(5,2) CHECK (InterestRate BETWEEN 0 AND 100),
                 RateType VARCHAR2(20),
                 MaturityDate DATE NOT NULL,
                 RepricingDate DATE,
                 CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                 UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

    public static final String CREATE_LIABILITY = """
            CREATE TABLE Liability (
                LiabilityID VARCHAR2(36) PRIMARY KEY,
                LiabilityType VARCHAR2(50) NOT NULL,
                PrincipalAmount NUMBER(15,2) CHECK (PrincipalAmount >= 0),
                InterestRate NUMBER(5,2) CHECK (InterestRate BETWEEN 0 AND 100),
                RateType VARCHAR2(20),
                MaturityDate DATE NOT NULL,
                RepricingDate DATE,
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

    public static final String CREATE_MATURITY_BUCKET = """
            CREATE TABLE MaturityBucket (
                BucketID VARCHAR2(36) PRIMARY KEY,
                BucketName VARCHAR2(50) NOT NULL UNIQUE,
                StartRange NUMBER NOT NULL CHECK (StartRange >= 0),
                EndRange NUMBER NOT NULL CHECK (EndRange >= 0),
                Description VARCHAR2(200),
                TotalAssetsValue NUMBER(15,2),
                TotalLiabilitiesValue NUMBER(15,2),
                NetGap NUMBER(15,2),
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT chk_maturity_range CHECK (EndRange >= StartRange)
            )
        """;

    public static final String CREATE_LIQUIDITY = """
            CREATE TABLE Liquidity (
                LiquidityID VARCHAR2(36) PRIMARY KEY,
                AssessmentDate DATE NOT NULL,
                CashFlowNet NUMBER(15,2),
                CashReserve NUMBER(15,2) CHECK (CashReserve >= 0),
                AvgInterest NUMBER(5,2) CHECK (AvgInterest BETWEEN 0 AND 100),
                CurrentRatio NUMBER(10,2),
                QuickRatio NUMBER(10,2),
                TotalLiquidAssets NUMBER(15,2) CHECK (TotalLiquidAssets >= 0),
                TotalShortTermLiabilities NUMBER(15,2) CHECK (TotalShortTermLiabilities >= 0),
                BucketID VARCHAR2(36),
                ScenarioID VARCHAR2(36),
                Description VARCHAR2(200),
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_liquidity_bucket FOREIGN KEY (BucketID)
                    REFERENCES MaturityBucket(BucketID) ON DELETE SET NULL
            )
        """;
}