package org.example;// src/com/alm/Main.java

import org.example.model.*;
import org.example.repository.DataRepository;
import org.example.service.*;
import org.example.utility.InputHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    private final DataRepository repository;
    private final InterestRateRiskService interestRateRiskService;
    private final LiquidityRiskService liquidityRiskService;
    private final CreditRiskService creditRiskService;
    private final ScenarioAnalysisService scenarioAnalysisService;
    private final ReportingService reportingService;

    private List<Asset> assets;
    private List<Liability> liabilities;
    private List<Scenario> scenarios;

    public Main() {
        this.repository = new DataRepository();
        this.interestRateRiskService = new InterestRateRiskService();
        this.liquidityRiskService = new LiquidityRiskService();
        this.creditRiskService = new CreditRiskService();
        this.scenarioAnalysisService = new ScenarioAnalysisService();
        this.reportingService = new ReportingService();

        // Load data
        this.assets = repository.loadAssets();
        this.liabilities = repository.loadLiabilities();
        this.scenarios = repository.loadScenarios();
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
                    addAsset();
                    break;
                case 2:
                    addLiability();
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
                    generateReports();
                    break;
                case 9:
                    saveData();
                    break;
                case 10:
                    MatchingService ms = new MatchingService();
                    System.out.println(assets);
                    System.out.println(liabilities);
                    ms.analyzeMatching(assets, liabilities);
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
        System.out.println("1. Add Asset");
        System.out.println("2. Add Liability");
        System.out.println("3. View Portfolio");
        System.out.println("4. Interest Rate Risk Analysis");
        System.out.println("5. Liquidity Risk Analysis");
        System.out.println("6. Credit Risk Analysis");
        System.out.println("7. Scenario Management");
        System.out.println("8. Generate Reports");
        System.out.println("9. Save Data");
        System.out.println("10. Matching Reports");
        System.out.println("0. Exit");
    }

    public static double getPD(int rating) {
        switch (rating) {
            case 10:
                return 0.001;
            case 9:
                return 0.0025;
            case 8:
                return 0.005;
            case 7:
                return 0.01;
            case 6:
                return 0.02;
            case 5:
                return 0.03;
            case 4:
                return 0.05;
            case 3:
                return 0.08;
            case 2:
                return 0.12;
            case 1:
                return 0.20;
            default:
                throw new IllegalArgumentException("Invalid rating (1-10 expected)");
        }
    }

    public static double getLGD(int rating) {
        switch (rating) {
            case 10:
                return 0.20;
            case 9:
                return 0.25;
            case 8:
                return 0.30;
            case 7:
                return 0.35;
            case 6:
                return 0.40;
            case 5:
                return 0.45;
            case 4:
                return 0.50;
            case 3:
                return 0.55;
            case 2:
                return 0.60;
            case 1:
                return 0.70;
            default:
                throw new IllegalArgumentException("Invalid rating (1-10 expected)");
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
            double pd = getPD(rating);
            double lgd = getLGD(rating);

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
        System.out.println("Liability added successfully!");
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
        System.out.println("\n=== INTEREST RATE RISK ANALYSIS ===");
        //Net Interest Income (NII) → difference between interest earned and paid.
        double duration = interestRateRiskService.calculatePortfolioDuration(assets, liabilities, 0.05);
        double nii = interestRateRiskService.calculateNetInterestIncome(assets, liabilities);
        //Gap Analysis → mismatch between rate-sensitive assets and liabilities.
        double gap30 = interestRateRiskService.calculateGapAnalysis(assets, liabilities, 30);
        double gap90 = interestRateRiskService.calculateGapAnalysis(assets, liabilities, 90);

        System.out.printf("Portfolio Duration: %.2f years\n", duration);
        System.out.printf("Net Interest Income: %,.2f\n", nii);
        System.out.printf("30-Day Gap: %,.2f\n", gap30);
        System.out.printf("90-Day Gap: %,.2f\n", gap90);

        // Sensitivity analysis
        System.out.println("\nInterest Rate Sensitivity:");
        for (double shock : new double[] { -0.01, -0.005, 0.005, 0.01 }) {
            List<Asset> shockedAssets = applyRateShock(assets, shock);
            List<Liability> shockedLiabilities = applyRateShock(liabilities, shock);

            double shockedNii = interestRateRiskService.calculateNetInterestIncome(shockedAssets, shockedLiabilities);
            double niiChange = shockedNii - nii;

            System.out.printf("NII change for %.1f%% rate shock: %,.2f\n", shock * 100, niiChange);
        }
    }

    private <T extends FinancialInstrument> List<T> applyRateShock(List<T> instruments, double shock) {
        List<T> shockedInstruments = new ArrayList<>();
        for (T instrument : instruments) {
            try {
                @SuppressWarnings("unchecked")
                T copy = (T) instrument.getClass()
                        .getConstructor(instrument.getClass())
                        .newInstance(instrument);

                if (!copy.isFixedRate()) {
                    copy.setInterestRate(copy.getInterestRate() + shock);
                }

                shockedInstruments.add(copy);
            } catch (Exception e) {
                // If we can't clone, use original
                shockedInstruments.add(instrument);
            }
        }
        return shockedInstruments;
    }

    private void analyzeLiquidityRisk() {
        System.out.println("\n=== LIQUIDITY RISK ANALYSIS ===");

        Map<String, Double> maturityLadder = liquidityRiskService.calculateMaturityLadder(assets, liabilities);
        System.out.println("Maturity Ladder (Net Cash Flow):");
        maturityLadder.forEach((bucket, amount) -> {
            System.out.printf("%-12s: %,.2f\n", bucket, amount);
        });

        // Identify liquidity gaps
        System.out.println("\nLiquidity Gaps:");
        maturityLadder.forEach((bucket, amount) -> {
            if (amount < 0) {
                System.out.printf("Liquidity gap in %s: %,.2f\n", bucket, Math.abs(amount));
            }
        });
    }

    private void analyzeCreditRisk() {
        System.out.println("\n=== CREDIT RISK ANALYSIS ===");

        // Extract loans from assets
        List<Loan> loans = new ArrayList<>();
        for (Asset asset : assets) {
            if (asset instanceof Loan) {
                loans.add((Loan) asset);
            }
        }

        double expectedLoss = creditRiskService.calculateTotalExpectedLoss(loans);
        double rwa = creditRiskService.calculateRiskWeightedAssets(assets);
        double car = creditRiskService.calculateCapitalAdequacyRatio(1000000, rwa); // Assuming 1M capital

        System.out.printf("Total Expected Loss: %,.2f\n", expectedLoss);
        System.out.printf("Risk Weighted Assets: %,.2f\n", rwa);
        System.out.printf("Capital Adequacy Ratio: %.2f%%\n", car * 100);

        // PD by rating
        System.out.println("\nProbability of Default by Rating:");
        for (int rating = 1; rating <= 10; rating++) {
            double pd = creditRiskService.calculateProbabilityOfDefault(loans, rating);
            System.out.printf("Rating %d: %.2f%%\n", rating, pd * 100);
        }
    }

    // In Main.java
    private void analyzeCurrencyExposure() {
        System.out.println("=== CURRENCY EXPOSURE ANALYSIS ===");
        reportingService.printCurrencyExposure(assets, liabilities);
    }

    private void manageScenarios() {
        System.out.println("\n=== SCENARIO MANAGEMENT ===");
        System.out.println("1. Create New Scenario");
        System.out.println("2. Run Existing Scenarios");
        System.out.println("3. View Scenario Results");

        int choice = InputHelper.getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                createScenario();
                break;
            case 2:
                runScenarios();
                break;
            case 3:
                viewScenarioResults();
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
        System.out.println("\n=== RUN SCENARIOS ===");

        if (scenarios.isEmpty()) {
            System.out.println("No scenarios available. Please create a scenario first.");
            return;
        }

        for (int i = 0; i < scenarios.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, scenarios.get(i).getName());
        }

        int choice = InputHelper.getIntInput("Select scenario to run: ") - 1;

        if (choice < 0 || choice >= scenarios.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Scenario scenario = scenarios.get(choice);
        ScenarioAnalysisService.ScenarioResult result = scenarioAnalysisService.analyzeScenario(scenario, assets,
                liabilities);

        System.out.println("\nScenario Results:");
        System.out.println(result);
    }

    private void viewScenarioResults() {
        // This would typically store and retrieve results from previous runs
        System.out.println("Scenario results functionality would be implemented here.");
    }

    private void generateReports() {
        System.out.println("\n=== GENERATE REPORTS ===");

        // Extract loans from assets
        List<Loan> loans = new ArrayList<>();
        for (Asset asset : assets) {
            if (asset instanceof Loan) {
                loans.add((Loan) asset);
            }
        }

        reportingService.generateMaturityProfileReport(assets, liabilities);
        reportingService.generateRiskMetricsReport(interestRateRiskService, creditRiskService,
                assets, liabilities, loans);
        reportingService.generateCreditQualityReport(loans);
    }

    private void saveData() {
        repository.saveAssets(assets);
        repository.saveLiabilities(liabilities);
        repository.saveScenarios(scenarios);
        System.out.println("Data saved successfully!");
    }
}
