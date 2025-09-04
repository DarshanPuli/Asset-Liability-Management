package org.example.Dao;

import org.example.entity.Asset;

import java.sql.SQLException;
import java.util.UUID;

public interface AssetDao {

    public void addAsset(Asset asset) throws SQLException;

}
