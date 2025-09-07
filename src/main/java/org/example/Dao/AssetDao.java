package org.example.Dao;

import org.example.entity.Asset;
import org.example.entity.AssetsHeld;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AssetDao {

    public void addAsset(Asset asset) throws SQLException;
    public Asset getAsset(UUID assetId) throws SQLException;
    public List<AssetsHeld> getAllAssetsByAssetId(String assetId) throws SQLException;
    public long getTotalAssetsValue() throws SQLException;
}
