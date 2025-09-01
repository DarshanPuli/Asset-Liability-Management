package org.example.db;

import org.example.entity.Asset;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class AssetDB {
    private ArrayList<Asset> assets = new ArrayList<Asset>();
    private static AssetDB instance= null;

    private AssetDB(){}

    public static AssetDB getInstance(){
        if(instance==null){
            instance = new AssetDB();
            return instance;
        }else{
            return instance;
        }
    }

    public void addAsset(Asset asset){
        assets.add(asset);
    }

    public ArrayList<Asset> getAssets(){
        return assets;
    }

    public Asset getAsset(UUID assetId){
        for(Asset asset : assets){
            if(asset.getAssetId().equals(assetId)){
                return asset;
            }
        }
        return null;
    }
}
