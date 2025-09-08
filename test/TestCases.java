package org.example.test;

import org.example.model.*;
import org.example.service.*;
import org.example.repository.DataRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestCases {
    public static void main(String[] args) {
    }
    }
//        System.out.println("=== ALM SYSTEM TEST CASES ===\n");
//
//        testFinancialInstrumentModel();
//        testLoanModel();
//        testDepositModel();
//        testScenarioModel();
//        testInterestRateRiskService();
//        testLiquidityRiskService();
//        testCreditRiskService();
//        testScenarioAnalysisService();
//        testDataRepository();
//
//        System.out.println("\n=== ALL TESTS COMPLETED ===");
//    }
//
//    public static void testFinancialInstrumentModel() {
//        System.out.println("1. Testing Financial Instrument Model");
//        LocalDate futureDate = LocalDate.now().plusYears(2);
//        Asset asset = new Asset("A1", "Test Asset", 100000, 0.05, futureDate, "USD", true, "Investment");
//        System.out.println(asset);
//        System.out.println("Principal: " + asset.getPrincipalAmount());
//        System.out.println("Interest Rate: " + asset.getInterestRate());
//        System.out.println("Currency: " + asset.getCurrency());
//        System.out.println("Days to Maturity: " + asset.getDaysToMaturity());
//        System.out.println("Year Fraction to Maturity: " + asset.getYearFractionToMaturity());
//        System.out.println("Present Value (5%): " + asset.calculatePresentValue(0.05));
//        System.out.println("Duration (5%): " + asset.calculateDuration(0.05));
//        System.out.println("   ✓ Financial Instrument tests passed\n");
//    }
//
//    public static void testLoanModel() {
//        System.out.println("2. Testing Loan Model");
//        LocalDate futureDate = LocalDate.now().plusYears(3);
//        Loan loan = new Loan("L1", "Business Loan", 200000, 0.07, futureDate, "USD", true, "ABC Corp", 7, 0.02, 0.4);
//        System.out.println(loan);
//        System.out.println("Borrower: " + loan.getBorrower());
//        System.out.println("Credit Rating: " + loan.getCreditRating());
//        System.out.println("Probability of Default: " + loan.getProbabilityOfDefault());
//        System.out.println("Loss Given Default: " + loan.getLossGivenDefault());
//        System.out.println("Expected Loss: " + loan.calculateExpectedLoss());
//        System.out.println("Duration (5%): " + loan.calculateDuration(0.05));
//        System.out.println("Present Value (5%): " + loan.calculatePresentValue(0.05));
//        System.out.println("   ✓ Loan model tests passed\n");
//    }
//
//    public static void testDepositModel() {
//        System.out.println("3. Testing Deposit Model");
//        LocalDate futureDate = LocalDate.now().plusYears(1);
//        Deposit deposit = new Deposit("D1", "Savings Deposit", 50000, 0.02, futureDate, "USD", false, "John Doe", true);
//        System.out.println(deposit);
//        System.out.println("Depositor: " + deposit.getDepositor());
//        System.out.println("Withdrawable: " + deposit.isWithdrawable());
//        System.out.println("Variable Rate: " + !deposit.isFixedRate());
//        deposit.setInterestRate(0.03);
//        System.out.println("New Interest Rate: " + deposit.getInterestRate());
//        System.out.println("Duration (5%): " + deposit.calculateDuration(0.05));
//        System.out.println("Present Value (5%): " + deposit.calculatePresentValue(0.05));
//        System.out.println("   ✓ Deposit model tests passed\n");
//    }
//
//    public static void testScenarioModel() {
//        System.out.println("4. Testing Scenario Model");
//        Scenario scenario = new Scenario("Rate Hike", "1% increase in USD rates");
//        scenario.addRateChange("USD", 0.01);
//        scenario.setLiquidityShockFactor(0.1);
//        scenario.setDefaultRateIncrease(0.005);
//        System.out.println(scenario);
//        System.out.println("USD Rate Change: " + scenario.getRateChangeForCurrency("USD"));
//        System.out.println("Liquidity Shock: " + scenario.getLiquidityShockFactor());
//        System.out.println("Default Rate Increase: " + scenario.getDefaultRateIncrease());
//        System.out.println("   ✓ Scenario model tests passed\n");
//    }
//
//    public static void testInterestRateRiskService() {
//        System.out.println("5. Testing Interest Rate Risk Service");
//        List<Asset> assets = createTestAssets();
//        List<Liability> liabilities = createTestLiabilities();
//
//        InterestRateRiskService service = new InterestRateRiskService();
//        double nii = service.calculateNetInterestIncome(assets, liabilities);
//        System.out.println("Net Interest Income: " + nii);
//        double duration = service.calculatePortfolioDuration(assets, liabilities, 0.05);
//        System.out.println("Portfolio Duration (5%): " + duration);
//        double gap30 = service.calculateGapAnalysis(assets, liabilities, 30);
//        double gap365 = service.calculateGapAnalysis(assets, liabilities, 365);
//        System.out.println("Gap Analysis 30 days: " + gap30);
//        System.out.println("Gap Analysis 365 days: " + gap365);
//
//        // Test matching
//        testMatching(assets, liabilities);
//
//        System.out.println("   ✓ Interest Rate Risk Service tests passed\n");
//    }
//
//    private static void testMatching(List<Asset> assets, List<Liability> liabilities) {
//        System.out.println("\nTesting Asset-Liability Matching");
//        MatchingService matchingService = new MatchingService();
//
//        // Test duration matching
//        boolean isDurationMatched = matchingService.isDurationMatched(assets, liabilities, 0.05, 0.5);
//        System.out.println("Duration Matched (tolerance 0.5): " + isDurationMatched);
//
//        // Test currency matching
//        boolean hasCurrencyMismatch = matchingService.hasCurrencyMismatch(assets, liabilities);
//        System.out.println("Has Currency Mismatch: " + hasCurrencyMismatch);
//
//        // Get optimal matching result
//        MatchingService.MatchingResult matchingResult = matchingService.findOptimalMatching(assets, liabilities);
//        System.out.println("Currency Match Score: " + matchingResult.getCurrencyMatchScore());
//        System.out.println("Duration Match Score: " + matchingResult.getDurationMatchScore());
//        System.out.println("Maturity Match Score: " + matchingResult.getMaturityMatchScore());
//
//        // Get matching suggestions
//        List<String> suggestions = matchingService.getMatchingSuggestions(assets, liabilities);
//        System.out.println("\nMatching Suggestions:");
//        suggestions.forEach(s -> System.out.println("- " + s));
//    }
//
//    private static List<Asset> createTestAssets() {
//        List<Asset> assets = new ArrayList<>();
//        LocalDate now = LocalDate.now();
//
//        // Corporate Bonds
//        assets.add(new Asset("B1", "Corporate Bond AAA", 1000000, 0.045, now.plusYears(5), "USD", true, "Bond"));
//        assets.add(new Asset("B2", "Corporate Bond AA", 750000, 0.052, now.plusYears(3), "USD", true, "Bond"));
//
//        // Government Securities
//        assets.add(new Asset("G1", "Treasury Bond", 2000000, 0.035, now.plusYears(10), "USD", true, "Government"));
//        assets.add(new Asset("G2", "Treasury Bill", 500000, 0.025, now.plusMonths(3), "USD", true, "Government"));
//
//        // Loans
//        assets.add(new Loan("L1", "Commercial Loan", 1500000, 0.068, now.plusYears(7), "USD", true,
//                           "Corp A", 7, 0.02, 0.4));
//        assets.add(new Loan("L2", "SME Loan", 300000, 0.085, now.plusYears(2), "EUR", true,
//                           "Corp B", 5, 0.04, 0.5));
//
//        // Short-term investments
//        assets.add(new Asset("ST1", "Money Market", 400000, 0.03, now.plusMonths(1), "USD", false, "Money Market"));
//        assets.add(new Asset("ST2", "Commercial Paper", 600000, 0.035, now.plusMonths(6), "USD", true, "Commercial Paper"));
//
//        return assets;
//    }
//
//    private static List<Liability> createTestLiabilities() {
//        List<Liability> liabilities = new ArrayList<>();
//        LocalDate now = LocalDate.now();
//
//        // Deposits
//        liabilities.add(new Deposit("D1", "Savings Deposits", 1000000, 0.02, now.plusYears(1), "USD", false, "Retail", true));
//        liabilities.add(new Deposit("D2", "Time Deposit", 500000, 0.035, now.plusYears(2), "USD", true, "Corporate", false));
//
//        // Wholesale Funding
//        liabilities.add(new Liability("WF1", "Interbank Loan", 750000, 0.04, now.plusMonths(3), "USD", true, "Wholesale"));
//        liabilities.add(new Liability("WF2", "Repo", 300000, 0.025, now.plusDays(7), "USD", true, "Repo"));
//
//        // Long-term Funding
//        liabilities.add(new Liability("LT1", "Bond Issued", 2000000, 0.055, now.plusYears(5), "USD", true, "Bond"));
//        liabilities.add(new Liability("LT2", "Subordinated Debt", 400000, 0.065, now.plusYears(8), "EUR", true, "Sub Debt"));
//
//        // Other Liabilities
//        liabilities.add(new Liability("OT1", "Commercial Paper", 250000, 0.03, now.plusMonths(6), "USD", true, "CP"));
//
//        return liabilities;
//    }
//
//    public static void testLiquidityRiskService() {
//        System.out.println("6. Testing Liquidity Risk Service");
//        List<Asset> assets = new ArrayList<>();
//        List<Liability> liabilities = new ArrayList<>();
//        LocalDate now = LocalDate.now();
//        assets.add(new Asset("A1", "30D Asset", 50000, 0.03, now.plusDays(30), "USD", true, "Investment"));
//        assets.add(new Asset("A2", "1Y Asset", 100000, 0.04, now.plusYears(1), "USD", true, "Investment"));
//        assets.add(new Asset("A3", "3Y Asset", 150000, 0.05, now.plusYears(3), "USD", true, "Investment"));
//        liabilities.add(new Liability("L1", "90D Liability", 75000, 0.02, now.plusDays(90), "USD", true, "Debt"));
//        liabilities.add(new Liability("L2", "2Y Liability", 125000, 0.03, now.plusYears(2), "USD", true, "Debt"));
//        LiquidityRiskService service = new LiquidityRiskService();
//        var ladder = service.calculateMaturityLadder(assets, liabilities);
//        System.out.println("Maturity Ladder: " + ladder);
//        List<Asset> hqla = new ArrayList<>();
//        hqla.add(new Asset("HQ1", "Cash", 50000, 0.0, now.plusDays(1), "USD", true, "Cash"));
//        hqla.add(new Asset("HQ2", "Gov Bond", 100000, 0.02, now.plusDays(30), "USD", true, "Bond"));
//        List<Liability> outflows = new ArrayList<>();
//        outflows.add(new Liability("OF1", "Withdrawal", 100000, 0.0, now.plusDays(1), "USD", true, "Deposit"));
//        double lcr = service.calculateLiquidityCoverageRatio(hqla, outflows);
//        System.out.println("Liquidity Coverage Ratio: " + lcr);
//        System.out.println("   ✓ Liquidity Risk Service tests passed\n");
//    }
//
//    public static void testCreditRiskService() {
//        System.out.println("7. Testing Credit Risk Service");
//        List<Loan> loans = new ArrayList<>();
//        LocalDate futureDate = LocalDate.now().plusYears(3);
//        loans.add(new Loan("L1", "Loan A", 100000, 0.06, futureDate, "USD", true, "Company A", 8, 0.01, 0.3));
//        loans.add(new Loan("L2", "Loan B", 200000, 0.08, futureDate, "USD", true, "Company B", 5, 0.05, 0.5));
//        loans.add(new Loan("L3", "Loan C", 150000, 0.07, futureDate, "USD", true, "Company C", 3, 0.10, 0.7));
//        CreditRiskService service = new CreditRiskService();
//        double totalExpectedLoss = service.calculateTotalExpectedLoss(loans);
//        System.out.println("Total Expected Loss: " + totalExpectedLoss);
//        List<Asset> assets = new ArrayList<>(loans);
//        double rwa = service.calculateRiskWeightedAssets(assets);
//        System.out.println("Risk Weighted Assets: " + rwa);
//        double capital = 50000;
//        double car = service.calculateCapitalAdequacyRatio(capital, rwa);
//        System.out.println("Capital Adequacy Ratio: " + car);
//        System.out.println("   ✓ Credit Risk Service tests passed\n");
//    }
//
//    public static void testScenarioAnalysisService() {
//        System.out.println("8. Testing Scenario Analysis Service");
//        List<Asset> assets = new ArrayList<>();
//        List<Liability> liabilities = new ArrayList<>();
//        LocalDate futureDate = LocalDate.now().plusYears(2);
//        assets.add(new Asset("A1", "Variable Asset", 100000, 0.04, futureDate, "USD", false, "Investment"));
//        liabilities.add(new Liability("L1", "Fixed Liability", 80000, 0.03, futureDate, "USD", true, "Debt"));
//        Scenario scenario = new Scenario("Rate Increase", "1% rate hike");
//        scenario.addRateChange("USD", 0.01);
//        ScenarioAnalysisService service = new ScenarioAnalysisService();
//        var result = service.analyzeScenario(scenario, assets, liabilities);
//        System.out.println("Base NII: " + result.getBaseNii());
//        System.out.println("Scenario NII: " + result.getScenarioNii());
//        System.out.println("NII Change: " + result.getNiiChange());
//        System.out.println("   ✓ Scenario Analysis Service tests passed\n");
//    }
//
//    public static void testDataRepository() {
//        System.out.println("9. Testing Data Repository");
//        DataRepository repository = new DataRepository();
//        List<Asset> assets = new ArrayList<>();
//        List<Liability> liabilities = new ArrayList<>();
//        List<Scenario> scenarios = new ArrayList<>();
//        LocalDate futureDate = LocalDate.now().plusYears(1);
//        assets.add(new Asset("A1", "Test Asset", 100000, 0.05, futureDate, "USD", true, "Investment"));
//        liabilities.add(new Liability("L1", "Test Liability", 50000, 0.03, futureDate, "USD", false, "Debt"));
//        Scenario scenario = new Scenario("Test Scenario", "Test description");
//        scenarios.add(scenario);
//        repository.saveAssets(assets);
//        repository.saveLiabilities(liabilities);
//        repository.saveScenarios(scenarios);
//        List<Asset> loadedAssets = repository.loadAssets();
//        List<Liability> loadedLiabilities = repository.loadLiabilities();
//        List<Scenario> loadedScenarios = repository.loadScenarios();
//        System.out.println("Loaded Assets: " + loadedAssets);
//        System.out.println("Loaded Liabilities: " + loadedLiabilities);
//        System.out.println("Loaded Scenarios: " + loadedScenarios);
//        System.out.println("   ✓ Data Repository tests passed\n");
//    }
