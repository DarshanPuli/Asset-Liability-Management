package org.example.util.MainMenuControls;

import java.util.Scanner;

public class AssetOperations {
    public static void manageAssets(Scanner scanner) {
        System.out.println("--- Asset Operations ---");
        System.out.println("1. Add Asset");
        System.out.println("2. View Assets");
        System.out.println("3. Edit Asset");
        System.out.println("4. Delete Asset");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
//             case 1: addAsset(scanner); break;
            // case 2: viewAssets(); break;
            // case 3: editAsset(scanner); break;
            // case 4: deleteAsset(scanner); break;
            case 5:
                return;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
