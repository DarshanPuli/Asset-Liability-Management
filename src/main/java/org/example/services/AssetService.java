package org.example.services;

import org.example.DaoImplementation.AssetDaoImplementation;
import org.example.entity.Asset;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class AssetService {

    private final AssetDaoImplementation assetDaoimpl = new AssetDaoImplementation();

    public AssetService() throws SQLException {
    }

    public void addAsset(Scanner sc) throws SQLException {
        System.out.println("Enter Asset Type");
        String assetType = sc.next();
        System.out.println("Enter Asset Interest rate");
        double interestRate = sc.nextDouble();
        System.out.println("Enter Asset Rate type");
        String rateType = sc.next();
        System.out.println("Enter Asset Months to Expiry");
        int monthsToExpiry = sc.nextInt();
        System.out.println("enter repricing date if applicable (YYYY-MM-DD)");
        LocalDate repricingDate = LocalDate.parse(sc.next());
        Asset asset = new Asset(assetType,interestRate,rateType,monthsToExpiry,repricingDate);
        assetDaoimpl.addAsset(asset);
    }
}
