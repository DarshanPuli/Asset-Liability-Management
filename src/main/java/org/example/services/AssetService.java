package org.example.services;

import org.example.DaoImplementation.AssetDaoImpl;
import org.example.entity.Asset;
import org.example.entity.AssetsHeld;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AssetService {

    private final AssetDaoImpl assetDaoimpl = new AssetDaoImpl();

    public AssetService() throws SQLException {
    }

    public void addAsset(Scanner sc) throws SQLException {
        System.out.println("Enter Asset Name: ");
        String assetName = sc.nextLine();
        System.out.println("Enter Asset Interest rate");
        double interestRate = sc.nextDouble();
        System.out.println("Enter Asset Rate type (s/d)");
        String rateType = sc.next();
        LocalDate repricingDate = null;
        if(rateType.equals("d")){
            System.out.println("enter repricing date (YYYY-MM-DD)");
            repricingDate = LocalDate.parse(sc.next());
        }
        System.out.println("Enter quality of asset (high/low)");
        String quality = sc.next();

        Asset asset = new Asset(assetName,interestRate,rateType,repricingDate,quality);
        assetDaoimpl.addAsset(asset);
    }


    public void getAllAssetsById(Scanner sc) throws SQLException {
        System.out.println("Enter Asset Id: ");
        String assetId = sc.nextLine();
        List<AssetsHeld> assets = assetDaoimpl.getAllAssetsByAssetId(assetId);
        for(AssetsHeld asset : assets){
            System.out.println(asset);
        }
    }

    public void getAllAssetsValue() throws SQLException {
        long totalAssetsValue = assetDaoimpl.getTotalAssetsValue();
        System.out.println("Total Assets Value: " + totalAssetsValue);
    }
}
