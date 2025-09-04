package org.example.util;

import org.example.connection.OracleDbConnection;
import org.example.util.schema.TableSchemas;
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
            TableCreator.createTableIfNotExists(conn, "MATURITYBUCKET", TableSchemas.CREATE_MATURITY_BUCKET);
            TableCreator.createTableIfNotExists(conn, "USERS", TableSchemas.CREATE_USER);
            TableCreator.createTableIfNotExists(conn, "ASSET", TableSchemas.CREATE_ASSET);
            TableCreator.createTableIfNotExists(conn, "LIABILITY", TableSchemas.CREATE_LIABILITY);
            TableCreator.createTableIfNotExists(conn, "ASSETSHELD", TableSchemas.CREATE_ASSETS_HELD);
            TableCreator.createTableIfNotExists(conn, "LIABILITIESHELD", TableSchemas.CREATE_LIABILITY_HELD);
            TableCreator.createTableIfNotExists(conn, "LIQUIDITY", TableSchemas.CREATE_LIQUIDITY);

        } catch (SQLException e) {
            System.out.println("Error connecting to database or creating schema/triggers: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}