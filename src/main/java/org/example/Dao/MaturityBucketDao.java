package org.example.Dao;

import org.example.connection.OracleDbConnection;
import org.example.entity.AssetsHeld;
import org.example.entity.LiabilitiesHeld;
import org.example.entity.MaturityBucket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface MaturityBucketDao {
    public void addMaturityBucket(MaturityBucket maturityBucket) throws SQLException;
    public List<MaturityBucket> getAllMaturityBuckets() throws SQLException;
    public void addAssetToMaturityBucket(AssetsHeld assetsHeld) throws SQLException;
    public void addLiabilityToMaturityBucket(LiabilitiesHeld liabilitiesHeld) throws SQLException;
}
