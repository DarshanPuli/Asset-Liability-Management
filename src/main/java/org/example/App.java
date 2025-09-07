package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.util.DBSetup;
import org.example.util.MainMenuControls.*;

import java.sql.SQLException;
import java.util.Scanner;

public class App {
    public static final Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        DBSetup.initializeConnectionAndSchema();

        while (running) {
            MenuPrinter.printMainMenu();
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    AssetOperations.manageAssets(sc);
                    break;
                case 2:
                    LiabilityOperations.manageLiabilities(sc);
                    break;
                case 3:
                    UserOperations.manageUsers(sc);
                    break;
                case 4:
                    MaturityBucketOperations.manageMaturityBuckets(sc);
                    break;
                case 5:
                    SystemOperations.getPortfolioValue();
                    break;
                case 6:
                    SystemOperations.runScenarioSimulation();
                    break;
//                case 7:
//                    SystemOperations.viewLiquidityPosition();
//                    break;
//                case 8:
//                    SystemOperations.generateReports();
//                    break;
//                case 9:
//                    System.out.println("Exiting System! Bye!");
//                    running = false;
//                    break;
                default:
                    System.out.println("Invalid choice!");
            }
            System.out.println();
        }

        sc.close();
    }
}