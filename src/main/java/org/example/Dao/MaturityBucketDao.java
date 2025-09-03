package org.example.Dao;

import org.example.connection.OracleDbConnection;
import org.example.entity.MaturityBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public interface MaturityBucketDao {
    MaturityBucket findMaturityBucketByRange(int monthsLeftToMaturity) throws SQLException;
    void updateMaturityBucket(MaturityBucket maturityBucket) throws SQLException;
    void getMaturityBuckets() throws SQLException;
}
