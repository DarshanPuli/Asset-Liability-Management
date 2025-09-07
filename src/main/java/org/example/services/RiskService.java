package org.example.services;

import org.example.DaoImplementation.AssetDaoImpl;
import org.example.entity.Asset;
import org.example.entity.AssetsHeld;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

import static org.example.App.dotenv;

public class RiskService {

    AssetDaoImpl assetDaoImpl = new AssetDaoImpl();

    public RiskService() throws SQLException {
    }

    public void longTermBondRisk(double marketInterestRate) throws SQLException {
        UUID bondId1 = UUID.fromString("17e24c80-9fd4-407c-adfe-33eec1d7c420");
        UUID bondId2 = UUID.fromString("5ebf9925-39cf-4fcc-8ecd-0116a7c78f8b");
        UUID bondId3 = UUID.fromString("85135faa-cf57-424c-be20-dc0984fd9db8");
        UUID[] ids = {bondId1,bondId2,bondId3};
        List<AssetsHeld> bonds = assetDaoImpl.getAllAssetsByAssetId(ids);
        System.out.println(bonds);
        System.out.printf("%-15s %-15s %-15s%n", "Actual Value","Present Value", "Risk Level");
        System.out.println("-----------------------------------------------------------");
        for(AssetsHeld bond : bonds){
            Asset asset = assetDaoImpl.getAsset(bond.getAssetId());

            double presentValue = calculatePresentValue(bond,asset.getInterestRate(),marketInterestRate);
            double actualValue = bond.getPrincipalAmount();
            String risk = actualValue<=presentValue?"low":"high";
            System.out.printf("%-15.2f %-15.2f %-15s%n", actualValue,presentValue, risk);
        }
    }

    public double calculatePresentValue(AssetsHeld asset,double presentInterestRate,double marketInterestRate) throws SQLException {
        Period period = Period.between(LocalDate.now(),asset.getMaturityDate());
        double yearsToMaturity = period.getYears()+ (double) period.getMonths() /12;
        double presentValue = 0;
        double pow = 0;
        while(yearsToMaturity > 0){
            if(yearsToMaturity<1){
                pow += yearsToMaturity;
                double amountAfterInterest = asset.getPrincipalAmount()*presentInterestRate*yearsToMaturity/100;
                presentValue+= (asset.getPrincipalAmount()+amountAfterInterest)/Math.pow(1+marketInterestRate/100,pow++);
            }else {
                pow+=1;
                presentValue += (asset.getPrincipalAmount() * presentInterestRate / 100) / Math.pow(1 + marketInterestRate / 100, pow);
            }
            yearsToMaturity -= 1;
        }
        return presentValue;
    }
}
