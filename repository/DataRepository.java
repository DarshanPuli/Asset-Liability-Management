package org.example.repository;

// src/com/alm/repository/DataRepository.java

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.model.Scenario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private final AssetDAO assetDAO = new AssetDAOImpl();
    private final LiabilityDAO liabilityDAO = new LiabilityDAOImpl();
    private static final String SCENARIOS_FILE = "data/scenarios.dat";

    static {
        // Create data directory if it doesn't exist
        new File("data").mkdirs();
    }

    public void saveAsset(Asset asset) {
        assetDAO.saveAsset(asset);
    }

    public void saveLiability(Liability liability) {
        liabilityDAO.saveLiability(liability);
    }

    public void saveAssets(List<Asset> assets) {
        for (Asset asset : assets) {
            assetDAO.saveAsset(asset);
        }
    }

    public List<Asset> loadAssets() {
        return assetDAO.getAllAssets();
    }

    public void saveLiabilities(List<Liability> liabilities) {
        for (Liability liability : liabilities) {
            liabilityDAO.saveLiability(liability);
        }
    }

    public List<Liability> loadLiabilities() {
        return liabilityDAO.getAllLiabilities();
    }

    public void saveScenarios(List<Scenario> scenarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCENARIOS_FILE))) {
            oos.writeObject(scenarios);
        } catch (IOException e) {
            System.err.println("Error saving scenarios: " + e.getMessage());
        }
    }


    public List<Scenario> loadScenarios() {
        File file = new File(SCENARIOS_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Scenario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading scenarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}