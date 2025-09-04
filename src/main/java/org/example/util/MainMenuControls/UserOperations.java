package org.example.util.MainMenuControls;

import org.example.services.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class UserOperations {
    private static final UserService userService = new UserService();
    public static void manageUsers(Scanner scanner) throws SQLException {
        System.out.println("--- User Operations ---");
        System.out.println("1. Add User");
        System.out.println("2. Purchase Asset");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1: userService.addUser(scanner);
                break;
            case 2: userService.purchaseAsset(scanner);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }
}
