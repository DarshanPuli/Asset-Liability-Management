package org.example.services;

import java.sql.SQLException;

public class SystemService {
    AssetService assetService;
    LiabilityService liabilityService;

    public SystemService() throws SQLException {
        this.assetService = new AssetService();
        this.liabilityService = new LiabilityService();
    }
    public void getPortfolioValue() throws SQLException {
        assetService.getAllAssetsValue();
        liabilityService.getAllLiabilitiesValue();
    }
}
