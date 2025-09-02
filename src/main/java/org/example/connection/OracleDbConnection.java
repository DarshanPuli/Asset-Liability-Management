package org.example.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConnection {

    private static final String CONNECTION_STRING = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING,USERNAME,PASSWORD);
    }
}
