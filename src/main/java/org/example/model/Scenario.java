package org.example.model;

import java.io.Serializable;
import java.util.Collection;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRateChanges(Map<String, Double> rateChanges) {
        this.rateChanges = rateChanges;
    }
    public double getAverageRateChange() {
        // Calculate average rate change across all currencies
        Collection<Double> rateChanges = getRateChanges().values();
        return rateChanges.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    // Getters and setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getLiquidityShockFactor() { return liquidityShockFactor; }
    public void setLiquidityShockFactor(double liquidityShockFactor) { this.liquidityShockFactor = liquidityShockFactor; }
    public double getDefaultRateIncrease() { return defaultRateIncrease; }
    public void setDefaultRateIncrease(double defaultRateIncrease) { this.defaultRateIncrease = defaultRateIncrease; }
    public Map<String, Double> getRateChanges() {
        return rateChanges;
    }

    @Override
    public String toString() {
        return String.format("Scenario: %s - %s", name, description);
    }
}
