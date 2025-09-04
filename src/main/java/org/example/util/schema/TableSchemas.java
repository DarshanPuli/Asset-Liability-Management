package org.example.util.schema;

public class TableSchemas {

    public static final String CREATE_USER ="""
        CREATE TABLE Users (
        User_ID VARCHAR2(36) PRIMARY KEY,
        Name VARCHAR2(100) NOT NULL,
        Phone_Number VARCHAR2(20),
        Credit_Rating NUMBER(3) CHECK (Credit_Rating BETWEEN 0 AND 100)
    )
""";

    public static final String CREATE_ASSET = """
    CREATE TABLE Asset (
        Asset_ID VARCHAR2(36) PRIMARY KEY,
        Asset_Name VARCHAR2(100) NOT NULL,
        Interest_Rate NUMBER(5,2) CHECK (Interest_Rate BETWEEN 0 AND 100),
        Rate_Type VARCHAR2(20),
        Repricing_Date DATE,
        Quality VARCHAR2(50)
    )
""";


    public static final String CREATE_ASSETS_HELD = """
    CREATE TABLE AssetsHeld (
        User_ID VARCHAR2(36) NOT NULL,
        Asset_ID VARCHAR2(36) NOT NULL,
        Principal_Amount NUMBER(15,2) CHECK (Principal_Amount >= 0),
        Maturity_Date DATE NOT NULL,
        Amount_Left_To_Repay NUMBER(15,2) CHECK (Amount_Left_To_Repay >= 0),
        Possibility_Of_Default NUMBER(3) CHECK (Possibility_Of_Default BETWEEN 0 AND 100),
        Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT pk_assetsheld PRIMARY KEY (User_ID, Asset_ID),
        CONSTRAINT fk_assetsheld_asset FOREIGN KEY (Asset_ID) REFERENCES Asset(Asset_ID) ON DELETE CASCADE,
        CONSTRAINT fk_assetsheld_user FOREIGN KEY (User_ID) REFERENCES Users(User_ID) ON DELETE CASCADE
    )
""";



    public static final String CREATE_LIABILITY = """
    CREATE TABLE Liability (
        Liability_ID VARCHAR2(36) PRIMARY KEY,
        Liability_Name VARCHAR2(100) NOT NULL,
        Interest_Rate NUMBER(5,2) CHECK (Interest_Rate BETWEEN 0 AND 100),
        Rate_Type VARCHAR2(20),
        Repricing_Date DATE
    )
""";


    public static final String CREATE_LIABILITY_HELD = """
    CREATE TABLE LiabilitiesHeld (
        User_ID VARCHAR2(36) NOT NULL,
        Liability_ID VARCHAR2(36) NOT NULL,
        Principal_Amount NUMBER(15,2) CHECK (Principal_Amount >= 0),
        Maturity_Date DATE NOT NULL,
        Amount_Left_To_Repay NUMBER(15,2) CHECK (Amount_Left_To_Repay >= 0),
        Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT pk_liabilitiesheld PRIMARY KEY (User_ID, Liability_ID),
        CONSTRAINT fk_liabilitiesheld_liability FOREIGN KEY (Liability_ID) REFERENCES Liability(Liability_ID) ON DELETE CASCADE,
        CONSTRAINT fk_liabilitiesheld_user FOREIGN KEY (User_ID) REFERENCES Users(User_ID) ON DELETE CASCADE
    )
""";


    public static final String CREATE_MATURITY_BUCKET = """
    CREATE TABLE MaturityBucket (
        Bucket_ID VARCHAR2(36) PRIMARY KEY,
        Start_Range NUMBER NOT NULL CHECK (Start_Range >= 0),
        End_Range NUMBER NOT NULL CHECK (End_Range >= 0),
        Total_Assets_Value NUMBER(15,2) DEFAULT 0,
        Total_Liabilities_Value NUMBER(15,2) DEFAULT 0,
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