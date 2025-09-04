package org.example.Dao;

import org.example.connection.OracleDbConnection;
import org.example.entity.MaturityBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public interface MaturityBucketDao {
    public void addMaturityBucket(MaturityBucket maturityBucket) throws SQLException;
}
