package org.example.services;

import org.example.DaoImplementation.LiabilityDaoImpl;
import org.example.entity.Asset;
import org.example.entity.Liability;

import java.sql.SQLException;
import java.time.LocalDate;
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
}
