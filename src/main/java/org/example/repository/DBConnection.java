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
<<<<<<< HEAD

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@10.120.159.196:1521:free","system","root");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }


        return connection;
    }
=======
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/alm?createDatabaseIfNotExist=true",
                "root",
                "rohit"
            );

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }


>>>>>>> f03904fa03a6bd15d86b7e9223532f88c9489127
}