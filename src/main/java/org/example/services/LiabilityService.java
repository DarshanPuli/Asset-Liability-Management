package org.example.services;

import org.example.DaoImplementation.LiabilityDaoImpl;
import org.example.entity.Asset;
import org.example.entity.AssetsHeld;
import org.example.entity.LiabilitiesHeld;
import org.example.entity.Liability;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class LiabilityService {
    LiabilityDaoImpl liabilityDaoImpl = new LiabilityDaoImpl();

    public LiabilityService() throws SQLException {
    }

    public void addLiability(Scanner sc) throws SQLException {
        System.out.println("Enter Liability Name: ");
        String liabilityName = sc.nextLine();
        System.out.println("Enter Liability Interest rate");
        double interestRate = sc.nextDouble();
        System.out.println("Enter Liability Rate type (s/d)");
        String rateType = sc.next();
        LocalDate repricingDate = null;
        if(rateType.equals("d")){
            System.out.println("enter repricing date (YYYY-MM-DD)");
            repricingDate = LocalDate.parse(sc.next());
        }

        Liability liability = new Liability(liabilityName,interestRate,rateType,repricingDate);
        liabilityDaoImpl.addLiability(liability);
    }
    public void getAllLiabilitiesById(Scanner sc) throws SQLException {
        System.out.println("Enter liability Id: ");
        String liabilityId = sc.nextLine();
        List<LiabilitiesHeld> liabilities = liabilityDaoImpl.getAllLiabilitiesByLiabilityId(liabilityId);
        for(LiabilitiesHeld liability : liabilities){
            System.out.println(liability);
        }
    }
    public void getAllLiabilitiesValue() throws SQLException {
        long totalLiabilitiesValue = liabilityDaoImpl.getTotalLiabilitiesValue();
        System.out.println("Total Liabilities Value: " + totalLiabilitiesValue);
    }
}
