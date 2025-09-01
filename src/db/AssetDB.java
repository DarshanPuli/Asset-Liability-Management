package db;

import entity.Asset;

import java.util.ArrayList;
import java.util.Objects;

public class AssetDB {
    private ArrayList<Asset> assets = new ArrayList<Asset>();
    private AssetDB instance= null;

    private AssetDB(){}

    public AssetDB getInstance(){
        return this.instance!=null?this.instance:new AssetDB();
    }

    public void addAsset(Asset asset){
        assets.add(asset);
    }

    public ArrayList<Asset> getAssets(){
        return assets;
    }

    public Asset getAsset(String assetId){
        for(Asset asset : assets){
            if(asset.getAssetId().equals(assetId)){
                return asset;
            }
        }
        return null;
    }
}
