package org.example.connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDbConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String CONNECTION_STRING = dotenv.get("DB_URL");
    private static final String USERNAME = dotenv.get("DB_USERNAME");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
            System.out.println("Connected to database successfully.");
        }catch(SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Connection Failed! Check output console");
            System.exit(1);
        }
        return conn;
    }
}
