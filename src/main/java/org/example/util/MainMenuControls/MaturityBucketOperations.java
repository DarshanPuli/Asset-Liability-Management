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
        System.out.println("2. Update MaturityBucket");
        System.out.println("3. View All Buckets");
        System.out.println("4. Find MaturityBucket By Range");
        System.out.println("5. Delete a Maturity Bucket");
        System.out.println("6. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
             case 1: service.addMaturityBucket(scanner); break;
             case 2: service.updateMaturityBucket(scanner); break;
             case 3: service.getMaturityBucket(scanner); break;
             case 4: service.findMaturityBucketByRange(scanner); break;
             case 5: service.deleteMaturityBucket(scanner);  break;
             case 6: return;
             default:System.out.println("Invalid choice!");
        }
    }
}