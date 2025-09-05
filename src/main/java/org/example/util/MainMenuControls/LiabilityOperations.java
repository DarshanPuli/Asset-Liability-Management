package org.example.util.MainMenuControls;

import org.example.services.LiabilityService;

import java.sql.SQLException;
import java.util.Scanner;

public class LiabilityOperations {

    public static final LiabilityService liabilityService;

    static {
        try {
            liabilityService = new LiabilityService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void manageLiabilities(Scanner scanner) throws SQLException {

        System.out.println("--- Liability Operations ---");
        System.out.println("1. Add Liability product to bank");
        System.out.println("2. View Liability by ID");
        System.out.println("3. View total liabilities value");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
             case 1: liabilityService.addLiability(scanner); break;
             case 2: liabilityService.getAllLiabilitiesById(scanner); break;
             case 3: liabilityService.getAllLiabilitiesValue(); break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }

}