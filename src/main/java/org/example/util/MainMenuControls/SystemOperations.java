package org.example.util.MainMenuControls;

import org.example.services.LiabilityService;
import org.example.services.RiskService;
import org.example.services.SystemService;

import java.sql.SQLException;

public class SystemOperations {

    public static final SystemService systemService;
    public static final RiskService riskService;

    static {
        try {
            riskService = new RiskService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            systemService = new SystemService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getPortfolioValue() throws SQLException {
        systemService.getPortfolioValue();
    }

    public static void runScenarioSimulation() throws SQLException {
        int rate =  8;
        System.out.println("if interest rate increases to "+rate+"% bond prices will be as follows : ");
        riskService.longTermBondRisk(rate);
    }

    public static void viewLiquidityPosition() {
        System.out.println("Viewing Liquidity Position - Not yet implemented.");
    }

    public static void generateReports() {
        System.out.println("Generating Reports - Not yet implemented.");
    }
}