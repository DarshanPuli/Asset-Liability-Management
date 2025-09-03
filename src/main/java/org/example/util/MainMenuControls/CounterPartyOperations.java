package org.example.util.MainMenuControls;

import java.util.Scanner;

public class CounterPartyOperations {
    public static void manageCounterParties(Scanner scanner) {
        System.out.println("--- CounterParty Operations ---");
        System.out.println("1. Add Counterparty");
        System.out.println("2. View CounterParty");
        System.out.println("3. Edit CounterParty");
        System.out.println("4. Delete CounterParty");
        System.out.println("5. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            // case 1: addCounterParty(scanner); break;
            // case 2: viewCounterParty(); break;
            // case 3: editCounterParty(scanner); break;
            // case 4: deleteCounterParty(scanner); break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }
}