package org.example.repository;

// src/com/alm/repository/DataRepository.java

import org.example.model.Asset;
import org.example.model.Liability;
import org.example.model.Loan;
import org.example.model.Scenario;

import java.util.List;

public class DataRepository {
    private final AssetDAO assetDAO = new AssetDAOImpl();
    private final LiabilityDAO liabilityDAO = new LiabilityDAOImpl();
    private final ScenarioDAO scenarioDAO = new ScenarioDAOImpl();
    private final LoanDAO loanDAO = new LoanDAOImpl();

    public void saveAsset(Asset asset) {
        assetDAO.saveAsset(asset);
        if (asset instanceof org.example.model.Loan) {
            loanDAO.saveLoan((org.example.model.Loan) asset);
        }
    }

    public List<Loan> getLoans(){
        return loanDAO.getAllLoans();
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
        for (Scenario scenario : scenarios) {
            scenarioDAO.saveScenario(scenario);
        }
    }

    public List<Scenario> loadScenarios() {
        return scenarioDAO.getAllScenarios();
    }

    public void updateAsset(Asset asset) {
        assetDAO.updateAsset(asset);
    }

    public void deleteAsset(String assetId) {
        assetDAO.deleteAsset(assetId);
    }

    public void updateLiability(Liability liability) {
        liabilityDAO.updateLiability(liability);
    }

    public void deleteLiability(String liabilityId) {
        liabilityDAO.deleteLiability(liabilityId);
    }
}