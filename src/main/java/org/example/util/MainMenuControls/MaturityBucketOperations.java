package org.example.util.MainMenuControls;

import org.example.services.MaturityBucketService;

import java.sql.SQLException;
import java.util.Scanner;

public class MaturityBucketOperations {
    public static MaturityBucketService service;

    static {
        try {
            service = new MaturityBucketService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void manageMaturityBuckets(Scanner scanner) throws SQLException {
        System.out.println("--- MaturityBuckets Operations ---");
        System.out.println("1. Add MaturityBucket");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
             case 1: service.addMaturityBucket(scanner); break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}