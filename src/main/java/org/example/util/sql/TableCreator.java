package org.example.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static void createTableIfNotExists(Connection connection, String tableName, String createTableSQL) throws SQLException {
        // Oracle stores table names in UPPERCASE
        String checkQuery = "SELECT COUNT(*) FROM user_tables WHERE table_name = '" + tableName.toUpperCase() + "'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {

            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                System.out.println("Table " + tableName + " does not exist. Creating...");
                stmt.executeUpdate(createTableSQL);
                System.out.println("Table " + tableName + " created successfully.");
            } else {
                System.out.println("Table " + tableName + " already exists. Skipping creation.");
            }
        }
    }

}
