package org.example.util.MainMenuControls;

import org.example.services.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class UserOperations {
    private static UserService userService;

    static {
        try {
            userService = new UserService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void manageUsers(Scanner scanner) throws SQLException {
        System.out.println("--- User Operations ---");
        System.out.println("1. Add User");
        System.out.println("2. Purchase Asset");
        System.out.println("3. Purchase Liability");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1: userService.addUser(scanner);
                break;
            case 2: userService.purchaseAsset(scanner);
                break;
            case 3: userService.purchaseLiability(scanner);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
