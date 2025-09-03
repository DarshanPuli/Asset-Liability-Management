package org.example.util.schema;

public class TableSchemas {

    public static final String CREATE_COUNTERPARTY = """
            CREATE TABLE CounterParty (
                Counter_Party_ID VARCHAR2(36) PRIMARY KEY,
                Asset_ID VARCHAR2(36),
                Liability_ID VARCHAR2(36),
                Name VARCHAR2(100) NOT NULL,
                Type VARCHAR2(50) NOT  NULL,
                Credit_Rating VARCHAR2(20),
                Phone_Number NUMBER(15),
                Country VARCHAR2(100),
                Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                Updated_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_counterparty_asset FOREIGN KEY (Asset_ID)
                     REFERENCES Asset(Asset_ID) ON DELETE SET NULL,
                CONSTRAINT fk_counterparty_liability FOREIGN KEY (Liability_ID)
                     REFERENCES Liability(Liability_ID) ON DELETE SET NULL
            )
        """;

    public static final String CREATE_ASSET = """
            CREATE TABLE Asset (
                 Asset_ID VARCHAR2(36) PRIMARY KEY,
                 Asset_Type VARCHAR2(50) NOT NULL,
                 Interest_Rate NUMBER(5,2) CHECK (Interest_Rate BETWEEN 0 AND 100),
                 Rate_Type VARCHAR2(20),
                 Months_To_Expiry NUMBER NOT NULL,
                 Repricing_Date DATE,
                 CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                 UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

    public static final String CREATE_LIABILITY = """
            CREATE TABLE Liability (
                Liability_ID VARCHAR2(36) PRIMARY KEY,
                Liability_Type VARCHAR2(50) NOT NULL,
                Principal_Amount NUMBER(15,2) CHECK (Principal_Amount >= 0),
                Interest_Rate NUMBER(5,2) CHECK (Interest_Rate BETWEEN 0 AND 100),
                Rate_Type VARCHAR2(20),
                Maturity_Date DATE NOT NULL,
                Repricing_Date DATE,
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

    public static final String CREATE_MATURITY_BUCKET = """
            CREATE TABLE MaturityBucket (
                Bucket_ID VARCHAR2(36) PRIMARY KEY,
                Bucket_Name VARCHAR2(50) NOT NULL UNIQUE,
                Start_Range NUMBER NOT NULL CHECK (Start_Range >= 0),
                End_Range NUMBER NOT NULL CHECK (End_Range >= 0),
                Description VARCHAR2(200),
                Total_Assets_Value NUMBER(15,2) DEFAULT 0.0,
                Total_Liabilities_Value NUMBER(15,2)  DEFAULT 0.0,
                Net_Gap NUMBER(15,2)  DEFAULT 0.0,
                Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                Updated_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT chk_maturity_range CHECK (End_Range >= Start_Range)
            )
        """;

    public static final String CREATE_LIQUIDITY = """
            CREATE TABLE Liquidity (
                Liquidity_ID VARCHAR2(36) PRIMARY KEY,
                Assessment_Date DATE NOT NULL,
                Cash_Flow_Net NUMBER(15,2),
                Cash_Reserve NUMBER(15,2) CHECK (Cash_Reserve >= 0),
                Avg_Interest NUMBER(5,2) CHECK (Avg_Interest BETWEEN 0 AND 100),
                Current_Ratio NUMBER(10,2),
                Quick_Ratio NUMBER(10,2),
                Total_Liquid_Assets NUMBER(15,2) CHECK (Total_Liquid_Assets >= 0),
                Total_Short_Term_Liabilities NUMBER(15,2) CHECK (Total_Short_Term_Liabilities >= 0),
                Bucket_ID VARCHAR2(36),
                Scenario_ID VARCHAR2(36),
                Description VARCHAR2(200),
                CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT fk_liquidity_bucket FOREIGN KEY (Bucket_ID)
                    REFERENCES MaturityBucket(Bucket_ID) ON DELETE SET NULL
            )
        """;
}