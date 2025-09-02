package org.example.DaoImplementation;

import org.example.Dao.AssetDao;
import org.example.entity.Asset;

import java.util.UUID;

public class AssetDaoImplementation implements AssetDao {
    @Override
    public void addAsset(Asset asset) {

    }

    @Override
    public void deleteAsset(Asset asset) {

    }

    @Override
    public void updateAsset(Asset asset) {

    }

    @Override
    public Asset getAsset(UUID assetId) {
        return null;
    }

    @Override
    public int updateCount(UUID assetId) {
        return 0;
    }

    @Override
    public Asset addAssetToMaturityBucket(UUID assetId) {
        return null;
    }
}
