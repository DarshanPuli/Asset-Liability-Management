package org.example.util;

import org.example.connection.OracleDbConnection;
import org.example.util.schema.TableSchemas;
import org.example.util.schema.TableTriggers;
import org.example.util.sql.TableCreator;

import java.sql.Connection;
import java.sql.SQLException;

public class DBSetup {

    public static void initializeConnectionAndSchema() {
        try {
            Connection conn = OracleDbConnection.getConnection();

            // Set session timezone (optional, adjust to your needs, e.g., IST UTC+5:30)
            try (var stmt = conn.createStatement()) {
                stmt.execute("ALTER SESSION SET TIME_ZONE = '+05:30'");
            }

            // Create tables
            TableCreator.createTableIfNotExists(conn, "COUNTERPARTY", TableSchemas.CREATE_COUNTERPARTY);
            TableCreator.createTableIfNotExists(conn, "ASSET", TableSchemas.CREATE_ASSET);
            TableCreator.createTableIfNotExists(conn, "LIABILITY", TableSchemas.CREATE_LIABILITY);
            TableCreator.createTableIfNotExists(conn, "MATURITY_BUCKET", TableSchemas.CREATE_MATURITY_BUCKET);
            TableCreator.createTableIfNotExists(conn, "BUCKET_GAP", TableSchemas.CREATE_BUCKET_GAP);
            TableCreator.createTableIfNotExists(conn, "LIQUIDITY", TableSchemas.CREATE_LIQUIDITY);

            // Create triggers
            TableCreator.createTriggerIfNotExists(conn, "update_counterparty_timestamp", TableTriggers.TRIGGER_COUNTERPARTY);
            TableCreator.createTriggerIfNotExists(conn, "update_asset_timestamp", TableTriggers.TRIGGER_ASSET);
            TableCreator.createTriggerIfNotExists(conn, "update_liability_timestamp", TableTriggers.TRIGGER_LIABILITY);
            TableCreator.createTriggerIfNotExists(conn, "update_maturitybucket_timestamp", TableTriggers.TRIGGER_MATURITY_BUCKET);
            TableCreator.createTriggerIfNotExists(conn, "update_bucketgap_timestamp", TableTriggers.TRIGGER_BUCKET_GAP);
            TableCreator.createTriggerIfNotExists(conn, "update_liquidity_timestamp", TableTriggers.TRIGGER_LIQUIDITY);

        } catch (SQLException e) {
            System.out.println("Error connecting to database or creating schema/triggers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}