import java.util.Scanner;

public class Main {

    private static void printMainMenu() {
        System.out.println("Welcome to the Asset Liability Management System");
        System.out.println("Please select the option:");
        System.out.println("1. Asset Operations");
        System.out.println("2. Liability Operations");
        System.out.println("3. Counterparty Operations");
        System.out.println("4. Define / View Maturity Buckets");
        System.out.println("5. Run Risk Analysis");
        System.out.println("6. Run Scenario Simulations");
        System.out.println("7. View Liquidity Position");
        System.out.println("8. Generate Reports");
        System.out.println("9. Exit");
    }

    private static void manageAssets(Scanner scanner) {
        System.out.println("--- Asset Operations ---");
        System.out.println("1. Add Asset");
        System.out.println("2. View Assets");
        System.out.println("3. Edit Asset");
        System.out.println("4. Delete Asset");
        System.out.println("5. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        // Import and call the methods
        switch (option) {
//            case 1: addAsset(scanner); break;
//            case 2: viewAssets(); break;
//            case 3: editAsset(scanner); break;
//            case 4: deleteAsset(scanner); break;
            case 5: return;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void manageLiabilities(Scanner scanner) {
        System.out.println("--- Liability Operations ---");
        System.out.println("1. Add Liability");
        System.out.println("2. View Liability");
        System.out.println("3. Edit Liability");
        System.out.println("4. Delete Liability");
        System.out.println("5. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
//            Import and Call the Methods
//            case 1: addLiability(scanner); break;
//            case 2: viewLiability(); break;
//            case 3: editLiability(scanner); break;
//            case 4: deleteLiability(scanner); break;
            case 5: return;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void manageCounterParties(Scanner scanner) {
        System.out.println("--- CounterParty Operations ---");
        System.out.println("1. Add Counterparty");
        System.out.println("2. View CounterParty");
        System.out.println("3. Edit CounterParty");
        System.out.println("4. Delete CounterParty");
        System.out.println("5. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
//            Import and Call the Methods
//            case 1: addCounterParty(scanner); break;
//            case 2: viewCounterParty(); break;
//            case 3: editCounterParty(scanner); break;
//            case 4: deleteCounterParty(scanner); break;
            case 5: return;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void manageMaturityBuckets(Scanner scanner) {
        System.out.println("--- MaturityBuckets Operations ---");
        System.out.println("1. Add MaturityBucket");
        System.out.println("2. View MaturityBucket");
        System.out.println("3. Edit MaturityBucket");
        System.out.println("4. Delete MaturityBucket");
        System.out.println("5. Back to Main Menu");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
//            Import and Call the Methods
//            case 1: addMaturityBucket(scanner); break;
//            case 2: viewMaturityBucket(); break;
//            case 3: editMaturityBucket(scanner); break;
//            case 4: deleteMaturityBucket(scanner); break;
            case 5: return;
            default: System.out.println("Invalid choice!");
        }
    }

    private static void runRiskAnalysis() {
        // Import and call the methods
        System.out.println("Running Risk Analysis - Not yet implemented.");
    }

    private static void runScenarioSimulation() {
        // Import and call the methods
        System.out.println("Running Scenario Simulation - Not yet implemented.");
    }

    private static void viewLiquidityPosition() {
        // Import and call the methods
        System.out.println("Viewing Liquidity Position - Not yet implemented.");
    }

    private static void generateReports() {
        // Import and call the methods
        System.out.println("Generating Reports - Not yet implemented.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while(running) {
            printMainMenu();
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: manageAssets(sc); break;
                case 2: manageLiabilities(sc); break;
                case 3: manageCounterParties(sc); break;
                case 4: manageMaturityBuckets(sc); break;
                case 5: runRiskAnalysis();  break;
                case 6: runScenarioSimulation(); break;
                case 7: viewLiquidityPosition(); break;
                case 8: generateReports(); break;
                case 9: System.out.println("Exiting System! Bye!");
                        running = false;
                        break;
                        default: System.out.println("Invalid choice!");
            }
            System.out.println();
        }

        sc.close();
    }
}