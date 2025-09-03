package org.example.services;

import org.example.DaoImplementation.CounterPartyDaoImplementation;
import org.example.entity.CounterParty;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class CounterPartyService {

    CounterPartyDaoImplementation counterPartyDaoImplementation = new CounterPartyDaoImplementation();

    public CounterPartyService() throws SQLException {
    }

    public void addCounterParty(Scanner sc) throws SQLException
    {
        System.out.println("Enter counter party assetId");
        UUID assetId = UUID.fromString(sc.nextLine());
        System.out.println("Enter counter party name");
        String name = sc.nextLine();
        System.out.println("Enter counter party number");
        String number = sc.nextLine();
        System.out.println("Enter counter party type");
        String type = sc.nextLine();
        System.out.println("Enter counter party credit rating");
        String creditRating = sc.nextLine();
        System.out.println("Enter counter party country");
        String country = sc.nextLine();
        System.out.println("Enter principal amount");
        int principalAmount = Integer.parseInt(sc.nextLine());
        CounterParty counterParty = new CounterParty(assetId,name,type,creditRating,number,country,principalAmount);
        counterPartyDaoImplementation.addCounterParty(counterParty);
        counterPartyDaoImplementation.addToMaturityBucket(counterParty);

    }
}
