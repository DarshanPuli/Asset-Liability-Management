package org.example.util.MainMenuControls;

import org.example.services.SystemService;

import java.sql.SQLException;

public class SystemOperations {

    public static final SystemService systemService;

    static {
        try {
            systemService = new SystemService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getPortfolioValue() throws SQLException {
        systemService.getPortfolioValue();
    }

    public static void runScenarioSimulation() {
        System.out.println("Running Scenario Simulation - Not yet implemented.");
    }

    public static void viewLiquidityPosition() {
        System.out.println("Viewing Liquidity Position - Not yet implemented.");
    }

    public static void generateReports() {
        System.out.println("Generating Reports - Not yet implemented.");
    }
}