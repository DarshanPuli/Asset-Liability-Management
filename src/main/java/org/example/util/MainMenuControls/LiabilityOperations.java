package org.example.util.MainMenuControls;

import java.util.Scanner;

public class LiabilityOperations {
    public static void manageLiabilities(Scanner scanner) {
        System.out.println("--- Liability Operations ---");
        System.out.println("1. Add Liability");
        System.out.println("2. View Liability");
        System.out.println("3. Edit Liability");
        System.out.println("4. Delete Liability");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            // case 1: addLiability(scanner); break;
            // case 2: viewLiability(); break;
            // case 3: editLiability(scanner); break;
            // case 4: deleteLiability(scanner); break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }
}