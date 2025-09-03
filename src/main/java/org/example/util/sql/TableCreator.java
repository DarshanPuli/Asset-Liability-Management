package org.example.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static void createTableIfNotExists(Connection connection, String tableName, String createTableSQL) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM user_tables WHERE table_name = '" + tableName.toUpperCase() + "'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {

            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                System.out.println("Table " + tableName + " does not exist. Creating...");
                try {
                    stmt.executeUpdate(createTableSQL);
                    System.out.println("Table " + tableName + " created successfully.");
                } catch (SQLException e) {
                    System.err.println("Failed to create table " + tableName + ": " + e.getMessage());
                    throw e;
                }
            } else {
                System.out.println("Table " + tableName + " already exists. Skipping creation.");
            }
        } catch (SQLException e) {
            System.err.println("Error checking existence of table " + tableName + ": " + e.getMessage());
            throw e;
        }
    }

    public static void createTriggerIfNotExists(Connection connection, String triggerName, String createTriggerSQL) throws SQLException {
        System.out.println("Creating or replacing trigger " + triggerName + "...");
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTriggerSQL);
            System.out.println("Trigger " + triggerName + " created or replaced successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to create or replace trigger " + triggerName + ": " + e.getMessage());
            throw e;
        }
    }
}
















