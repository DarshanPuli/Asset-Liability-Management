package org.example.repository;

import org.example.model.Scenario;
import java.sql.*;
import java.util.*;

public class ScenarioDAOImpl implements ScenarioDAO {
    @Override
    public void saveScenario(Scenario scenario) {
        try (Connection conn = DBConnection.getConnection()) {
            // Insert or update scenario
            String upsertScenario = "MERGE INTO scenario s USING (SELECT ? AS name FROM dual) d ON (s.name = d.name) " +
                    "WHEN MATCHED THEN UPDATE SET description=?, liquidity_shock_factor=?, default_rate_increase=? " +
                    "WHEN NOT MATCHED THEN INSERT (name, description, liquidity_shock_factor, default_rate_increase) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(upsertScenario)) {
                ps.setString(1, scenario.getName());
                ps.setString(2, scenario.getDescription());
                ps.setDouble(3, scenario.getLiquidityShockFactor());
                ps.setDouble(4, scenario.getDefaultRateIncrease());
                ps.setString(5, scenario.getName());
                ps.setString(6, scenario.getDescription());
                ps.setDouble(7, scenario.getLiquidityShockFactor());
                ps.setDouble(8, scenario.getDefaultRateIncrease());
                ps.executeUpdate();
            }
            // Delete old rate changes
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM scenario_rate_change WHERE scenario_name = ?")) {
                ps.setString(1, scenario.getName());
                ps.executeUpdate();
            }
            // Insert new rate changes
            if (scenario.getRateChanges() != null) {
                for (Map.Entry<String, Double> entry : scenario.getRateChanges().entrySet()) {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO scenario_rate_change (scenario_name, currency, rate_change) VALUES (?, ?, ?)")) {
                        ps.setString(1, scenario.getName());
                        ps.setString(2, entry.getKey());
                        ps.setDouble(3, entry.getValue());
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Scenario> getAllScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM scenario");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double liquidityShock = rs.getDouble("liquidity_shock_factor");
                double defaultRate = rs.getDouble("default_rate_increase");
                Scenario scenario = new Scenario(name, description);
                scenario.setLiquidityShockFactor(liquidityShock);
                scenario.setDefaultRateIncrease(defaultRate);
                // Load rate changes
                try (PreparedStatement ps2 = conn.prepareStatement("SELECT currency, rate_change FROM scenario_rate_change WHERE scenario_name = ?")) {
                    ps2.setString(1, name);
                    try (ResultSet rs2 = ps2.executeQuery()) {
                        while (rs2.next()) {
                            scenario.addRateChange(rs2.getString("currency"), rs2.getDouble("rate_change"));
                        }
                    }
                }
                scenarios.add(scenario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scenarios;
    }

    @Override
    public void deleteScenario(String name) {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM scenario_rate_change WHERE scenario_name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM scenario WHERE name = ?")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateScenario(Scenario scenario) {
        saveScenario(scenario); // Upsert logic
    }
}

