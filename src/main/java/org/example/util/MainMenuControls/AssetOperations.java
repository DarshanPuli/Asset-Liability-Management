package org.example.util.MainMenuControls;

import org.example.services.AssetService;

import java.sql.SQLException;
import java.util.Scanner;

public class AssetOperations {

    private static final AssetService assetService;

    static {
        try {
            assetService = new AssetService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AssetOperations() throws SQLException {
    }

    public static void manageAssets(Scanner scanner) throws SQLException {
        System.out.println("--- Asset Operations ---");
        System.out.println("1. Add Asset Product To bank");
        System.out.println("2. View All Assets By Id");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
             case 1: assetService.addAsset(scanner);
                        break;
            case 2: assetService.getAllAssetsById(scanner);
                        break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
