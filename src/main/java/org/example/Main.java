package org.example;// src/com/alm/Main.java

import org.example.model.*;
import org.example.repository.DataRepository;
import org.example.service.*;
import org.example.utility.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private final DataRepository repository;
    LiquidityReporter reporter = new LiquidityReporter();
    CreditRiskReporter creditRiskReportereporter = new CreditRiskReporter();

    private final List<Loan> loans;
    private List<Asset> assets;
    private List<Liability> liabilities;
    private final List<Scenario> scenarios;

    public Main() {
        this.repository = new DataRepository();
        InterestRateRiskService interestRateRiskService = new InterestRateRiskService();
        LiquidityRiskService liquidityRiskService = new LiquidityRiskService();
        CreditRiskService creditRiskService = new CreditRiskService();
        ScenarioAnalysisService scenarioAnalysisService = new ScenarioAnalysisService();
        ReportingService reportingService = new ReportingService();

        // Load data
        this.assets = repository.loadAssets();
        this.liabilities = repository.loadLiabilities();
        this.scenarios = repository.loadScenarios();
        this.loans = repository.getLoans();
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("=== ASSET LIABILITY MANAGEMENT SYSTEM ===");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = InputHelper.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    manageAssets();
                    break;
                case 2:
                    manageLiabilities();
                    break;
                case 3:
                    viewPortfolio();
                    break;
                case 4:
                    analyzeInterestRateRisk();
                    break;
                case 5:
                    analyzeLiquidityRisk();
                    break;
                case 6:
                    analyzeCreditRisk();
                    break;
                case 7:
                    manageScenarios();
                    break;
                case 8:
                    ALMReporter reporter = new ALMReporter();
                    reporter.generateFullALMReport(assets, liabilities);
                    break;
                case 9:
                    saveData();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Manage Assets");
        System.out.println("2. Manage Liabilities");
        System.out.println("3. View Portfolio");
        System.out.println("4. Interest Rate Risk Analysis");
        System.out.println("5. Liquidity Risk Analysis");
        System.out.println("6. Credit Risk Analysis");
        System.out.println("7. Scenario Management");
        System.out.println("8. Matching Reports");
        System.out.println("9. Save Data");
        System.out.println("0. Exit");
    }

    private void manageAssets() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Assets ---");
            System.out.println("1. Add Asset");
            System.out.println("2. List Assets");
            System.out.println("3. Update Asset");
            System.out.println("4. Delete Asset");
            System.out.println("0. Back");
            int choice = InputHelper.getIntInput("Choose an option: ");
            switch (choice) {
                case 1:
                    addAsset();
                    break;
                case 2:
                    listAssets();
                    break;
                case 3:
                    updateAsset();
                    break;
                case 4:
                    deleteAsset();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void manageLiabilities() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Manage Liabilities ---");
            System.out.println("1. Add Liability");
            System.out.println("2. List Liabilities");
            System.out.println("3. Update Liability");
            System.out.println("4. Delete Liability");
            System.out.println("0. Back");
            int choice = InputHelper.getIntInput("Choose an option: ");
            switch (choice) {
                case 1:
                    addLiability();
                    break;
                case 2:
                    listLiabilities();
                    break;
                case 3:
                    updateLiability();
                    break;
                case 4:
                    deleteLiability();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addAsset() {
        System.out.println("\n=== ADD NEW ASSET ===");

        String id = InputHelper.getStringInput("Enter asset ID: ");
        String name = InputHelper.getStringInput("Enter asset name: ");
        double principal = InputHelper.getDoubleInput("Enter principal amount: ");
        double rate = InputHelper.getDoubleInput("Enter interest rate (decimal): ");
        LocalDate maturity = InputHelper.getDateInput("Enter maturity date");
        String currency = InputHelper.getStringInput("Enter currency: ");
        boolean isFixed = InputHelper.getBooleanInput("Is fixed rate?");

        // Check if it's a loan (for credit risk parameters)
        if (InputHelper.getBooleanInput("Is this a loan?")) {
            String borrower = InputHelper.getStringInput("Enter borrower name: ");
            int rating = InputHelper.getIntInput("Enter credit rating (1-10): ");
            double pd = InputHelper.getDoubleInput("Enter probability of default (0-1): ");
            double lgd = InputHelper.getDoubleInput("Enter loss given default (0-1): ");

            Loan loan = new Loan(id, name, principal, rate, maturity, currency, isFixed,
                    borrower, rating, pd, lgd);
            repository.saveAsset(loan);
        } else {
            Asset asset = new Asset(id, name, principal, rate, maturity, currency, isFixed, "Investment");
            repository.saveAsset(asset);
        }
        assets = repository.loadAssets();
        System.out.println("Asset added successfully!");
    }

    private void addLiability() {
        System.out.println("\n=== ADD NEW LIABILITY ===");

        String id = InputHelper.getStringInput("Enter liability ID: ");
        String name = InputHelper.getStringInput("Enter liability name: ");
        double principal = InputHelper.getDoubleInput("Enter principal amount: ");
        double rate = InputHelper.getDoubleInput("Enter interest rate (decimal): ");
        LocalDate maturity = InputHelper.getDateInput("Enter maturity date");
        String currency = InputHelper.getStringInput("Enter currency: ");
        boolean isFixed = InputHelper.getBooleanInput("Is fixed rate?");

        // Check if it's a deposit
        if (InputHelper.getBooleanInput("Is this a deposit?")) {
            String depositor = InputHelper.getStringInput("Enter depositor name: ");
            boolean withdrawable = InputHelper.getBooleanInput("Is withdrawable?");

            Deposit deposit = new Deposit(id, name, principal, rate, maturity, currency, isFixed,
                    depositor, withdrawable);
            repository.saveLiability(deposit);
        } else {
            Liability liability = new Liability(id, name, principal, rate, maturity, currency, isFixed, "Debt");
            repository.saveLiability(liability);
        }
        liabilities = repository.loadLiabilities();

        // Add 8% of liability principal to Cash Reserve (C001)
        double cashReserveAdd = 0.08 * principal;
        Asset cashReserve = assets.stream().filter(a -> a.getId().equals("C001")).findFirst().orElse(null);
        if (cashReserve != null) {
            Asset updatedCashReserve = new Asset(
                cashReserve.getId(),
                cashReserve.getName(),
                cashReserve.getPrincipalAmount() + cashReserveAdd,
                cashReserve.getInterestRate(),
                cashReserve.getMaturityDate(),
                cashReserve.getCurrency(),
                cashReserve.isFixedRate(),
                cashReserve.getType()
            );
            repository.updateAsset(updatedCashReserve);
        } else {
            Asset newCashReserve = new Asset(
                "C001",
                "Cash Reserve",
                cashReserveAdd,
                0.0,
                null,
                currency,
                true,
                "cash"
            );
            repository.saveAsset(newCashReserve);
        }
        assets = repository.loadAssets();
        System.out.println("Liability added successfully! 8% added to Cash Reserve.");
    }

    private void viewPortfolio() {
        System.out.println("\n=== PORTFOLIO OVERVIEW ===");
        assets = repository.loadAssets();
        liabilities = repository.loadLiabilities();
        double totalAssets = assets.stream().mapToDouble(Asset::getPrincipalAmount).sum();
        double totalLiabilities = liabilities.stream().mapToDouble(Liability::getPrincipalAmount).sum();
        double netWorth = totalAssets - totalLiabilities;

        System.out.printf("Total Assets: %,.2f\n", totalAssets);
        System.out.printf("Total Liabilities: %,.2f\n", totalLiabilities);
        System.out.printf("Net Worth: %,.2f\n", netWorth);

        System.out.println("\nAssets:");
        assets.forEach(asset -> System.out.println("  " + asset));

        System.out.println("\nLiabilities:");
        liabilities.forEach(liability -> System.out.println("  " + liability));
    }

    private void analyzeInterestRateRisk() {
        InterestRiskReporter reporter = new InterestRiskReporter();
        reporter.generateFullIRRReport(assets, liabilities);

    }


    // Main method to generate full liquidity report
    public void analyzeLiquidityRisk() {
        reporter.generateFullReport(assets, liabilities);
    }

    private void analyzeCreditRisk() {
        double tier1Capital = 2500000; // Your tier 1 capital
        double tier2Capital = 1000000; // Your tier 2 capital

        creditRiskReportereporter.generateFullCreditReport(loans, assets, tier1Capital, tier2Capital);
    }
    // In Main.java

    private void manageScenarios() {
        System.out.println("\n=== SCENARIO MANAGEMENT ===");
        System.out.println("1. Create New Scenario");
        System.out.println("2. Run Scenarios");

        int choice = InputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                createScenario();
                break;
            case 2:
                runScenarios();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void createScenario() {
        System.out.println("\n=== CREATE NEW SCENARIO ===");

        String name = InputHelper.getStringInput("Enter scenario name: ");
        String description = InputHelper.getStringInput("Enter scenario description: ");

        Scenario scenario = new Scenario(name, description);

        // Add rate changes
        boolean addingRates = true;
        while (addingRates) {
            String currency = InputHelper.getStringInput("Enter currency for rate change (or 'done' to finish): ");
            if (currency.equalsIgnoreCase("done")) {
                addingRates = false;
            } else {
                double change = InputHelper.getDoubleInput("Enter rate change (decimal): ");
                scenario.addRateChange(currency, change);
            }
        }

        // Set other parameters
        double liquidityShock = InputHelper.getDoubleInput("Enter liquidity shock factor (0-1): ");
        double defaultIncrease = InputHelper.getDoubleInput("Enter default rate increase (0-1): ");

        scenario.setLiquidityShockFactor(liquidityShock);
        scenario.setDefaultRateIncrease(defaultIncrease);

        scenarios.add(scenario);
        System.out.println("Scenario created successfully!");
    }

    private void runScenarios() {

        ScenarioAnalysisReporter reporter = new ScenarioAnalysisReporter();

        System.out.println("\nScenario Results:");
        reporter.generateStressTestingReport(scenarios, assets, liabilities, loans);

    }

    private void saveData() {
        repository.saveAssets(assets);
        repository.saveLiabilities(liabilities);
        repository.saveScenarios(scenarios);
        System.out.println("Data saved successfully!");
    }

    private void listAssets() {
        System.out.println("\n--- Asset List ---");
        for (Asset asset : assets) {
            System.out.println(asset);
        }
    }

    private void updateAsset() {
        listAssets();
        String id = InputHelper.getStringInput("Enter Asset ID to update: ");
        Asset assetToUpdate = assets.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
        if (assetToUpdate == null) {
            System.out.println("Asset not found.");
            return;
        }
        String name = InputHelper.getStringInput("Enter new name (current: " + assetToUpdate.getName() + "): ");
        double principal = InputHelper.getDoubleInput("Enter new principal amount (current: " + assetToUpdate.getPrincipalAmount() + "): ");
        double rate = InputHelper.getDoubleInput("Enter new interest rate (current: " + assetToUpdate.getInterestRate() + "): ");
        LocalDate maturity = InputHelper.getDateInput("Enter new maturity date (current: " + assetToUpdate.getMaturityDate() + ")");
        String currency = InputHelper.getStringInput("Enter new currency (current: " + assetToUpdate.getCurrency() + "): ");
        boolean isFixed = InputHelper.getBooleanInput("Is fixed rate? (current: " + assetToUpdate.isFixedRate() + ")");
        assetToUpdate = new Asset(id, name, principal, rate, maturity, currency, isFixed, assetToUpdate.getType());
        repository.updateAsset(assetToUpdate);
        assets = repository.loadAssets();
        System.out.println("Asset updated.");
    }

    private void deleteAsset() {
        listAssets();
        String id = InputHelper.getStringInput("Enter Asset ID to delete: ");
        repository.deleteAsset(id);
        assets = repository.loadAssets();
        System.out.println("Asset deleted.");
    }

    private void listLiabilities() {
        System.out.println("\n--- Liability List ---");
        for (Liability liability : liabilities) {
            System.out.println(liability);
        }
    }

    private void updateLiability() {
        listLiabilities();
        String id = InputHelper.getStringInput("Enter Liability ID to update: ");
        Liability liabilityToUpdate = liabilities.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
        if (liabilityToUpdate == null) {
            System.out.println("Liability not found.");
            return;
        }
        String name = InputHelper.getStringInput("Enter new name (current: " + liabilityToUpdate.getName() + "): ");
        double principal = InputHelper.getDoubleInput("Enter new principal amount (current: " + liabilityToUpdate.getPrincipalAmount() + "): ");
        double rate = InputHelper.getDoubleInput("Enter new interest rate (current: " + liabilityToUpdate.getInterestRate() + "): ");
        LocalDate maturity = InputHelper.getDateInput("Enter new maturity date (current: " + liabilityToUpdate.getMaturityDate() + ")");
        String currency = InputHelper.getStringInput("Enter new currency (current: " + liabilityToUpdate.getCurrency() + "): ");
        boolean isFixed = InputHelper.getBooleanInput("Is fixed rate? (current: " + liabilityToUpdate.isFixedRate() + ")");
        liabilityToUpdate = new Liability(id, name, principal, rate, maturity, currency, isFixed, liabilityToUpdate.getType());
        repository.updateLiability(liabilityToUpdate);
        liabilities = repository.loadLiabilities();
        System.out.println("Liability updated.");
    }

    private void deleteLiability() {
        listLiabilities();
        String id = InputHelper.getStringInput("Enter Liability ID to delete: ");
        repository.deleteLiability(id);
        liabilities = repository.loadLiabilities();
        System.out.println("Liability deleted.");
    }
}
