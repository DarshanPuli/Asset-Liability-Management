package org.example.repository;

import org.example.model.Asset;
import java.util.List;

public interface AssetDAO {
    void saveAsset(Asset asset);
    void updateAsset(Asset asset);
    void deleteAsset(String assetId);
    Asset getAssetById(String assetId);
    List<Asset> getAllAssets();
}
