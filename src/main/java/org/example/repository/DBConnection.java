package org.example.repository;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBConnection {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@0.0.0.0:1521:free","system","root");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }


        return connection;
    }

}