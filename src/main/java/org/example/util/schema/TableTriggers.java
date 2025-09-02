package org.example.util.schema;

public class TableTriggers {
    public static final String TRIGGER_COUNTERPARTY = """
        CREATE OR REPLACE TRIGGER update_counterparty_timestamp
        BEFORE UPDATE ON CounterParty
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;

    public static final String TRIGGER_ASSET = """
        CREATE OR REPLACE TRIGGER update_asset_timestamp
        BEFORE UPDATE ON Asset
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;

    public static final String TRIGGER_LIABILITY = """
        CREATE OR REPLACE TRIGGER update_liability_timestamp
        BEFORE UPDATE ON Liability
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;

    public static final String TRIGGER_MATURITY_BUCKET = """
        CREATE OR REPLACE TRIGGER update_maturitybucket_timestamp
        BEFORE UPDATE ON MaturityBucket
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;

    public static final String TRIGGER_BUCKET_GAP = """
        CREATE OR REPLACE TRIGGER update_bucketgap_timestamp
        BEFORE UPDATE ON BucketGap
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;

    public static final String TRIGGER_LIQUIDITY = """
        CREATE OR REPLACE TRIGGER update_liquidity_timestamp
        BEFORE UPDATE ON Liquidity
        FOR EACH ROW
        BEGIN
            :NEW.UpdatedAt := CURRENT_TIMESTAMP;
        END;
        """;
}