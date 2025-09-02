package org.example.Dao;

import org.example.entity.Asset;

import java.util.UUID;

public interface AssetDao {

    public void addAsset(Asset asset);
    public void deleteAsset(Asset asset);
    public void updateAsset(Asset asset);
    public Asset getAsset(UUID assetId);
    public int updateCount(UUID assetId);
    public Asset addAssetToMaturityBucket(UUID assetId);

}
