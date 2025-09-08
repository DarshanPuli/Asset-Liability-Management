package org.example.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Scenario implements Serializable {
    private String name;
    private String description;
    private Map<String, Double> rateChanges; // Currency -> rate change
    private double liquidityShockFactor;
    private double defaultRateIncrease;

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
        this.rateChanges = new HashMap<>();
        this.liquidityShockFactor = 0.0;
        this.defaultRateIncrease = 0.0;
    }

    public void addRateChange(String currency, double change) {
        rateChanges.put(currency, change);
    }

    public double getRateChangeForCurrency(String currency) {
        return rateChanges.getOrDefault(currency, 0.0);
    }

    // Getters and setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getLiquidityShockFactor() { return liquidityShockFactor; }
    public void setLiquidityShockFactor(double liquidityShockFactor) { this.liquidityShockFactor = liquidityShockFactor; }
    public double getDefaultRateIncrease() { return defaultRateIncrease; }
    public void setDefaultRateIncrease(double defaultRateIncrease) { this.defaultRateIncrease = defaultRateIncrease; }

    @Override
    public String toString() {
        return String.format("Scenario: %s - %s", name, description);
    }
}
