package org.example.util;

import org.example.connection.OracleDbConnection;
import org.example.util.schema.TableSchemas;
import org.example.util.sql.TableCreator;

import java.sql.Connection;
import java.sql.SQLException;

public class DBSetup {

    public static void initializeConnectionAndSchema(){
        try {
            Connection conn = OracleDbConnection.getConnection();

            TableCreator.createTableIfNotExists(conn, "COUNTERPARTY", TableSchemas.CREATE_COUNTERPARTY);
            TableCreator.createTableIfNotExists(conn, "ASSET", TableSchemas.CREATE_ASSET);
            TableCreator.createTableIfNotExists(conn, "LIABILITY", TableSchemas.CREATE_LIABILITY);
            TableCreator.createTableIfNotExists(conn, "MATURITY_BUCKET", TableSchemas.CREATE_MATURITY_BUCKET);
            TableCreator.createTableIfNotExists(conn, "BUCKET_GAP", TableSchemas.CREATE_BUCKET_GAP);
            TableCreator.createTableIfNotExists(conn, "LIQUIDITY", TableSchemas.CREATE_LIQUIDITY);

        } catch (SQLException e) {
            System.out.println("Error connecting to database.");
            throw new RuntimeException(e);
        }
    }

}
