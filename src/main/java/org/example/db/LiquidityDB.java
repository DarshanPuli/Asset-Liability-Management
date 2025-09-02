package org.example.db;

import org.example.entity.Liquidity;

import java.util.HashMap;
import java.util.Map;

public class LiquidityDB {
    private static LiquidityDB instance;
    private final Map<String, Liquidity> liquidityTable;

    // private constructor to enforce singleton
    private LiquidityDB() {
        liquidityTable = new HashMap<>();
    }

    // Singleton accessor
    public static LiquidityDB getInstance() {
        if (instance == null) {
            instance = new LiquidityDB();
        }
        return instance;
    }

    // Add a liquidity record
    public void addLiquidity(Liquidity liquidity) {
        liquidityTable.put(liquidity.getLiquidityId(), liquidity);
    }

    // Get a liquidity record by ID
    public Liquidity getLiquidity(String liquidityId) {
        return liquidityTable.get(liquidityId);
    }

    // Update existing record (just re-add)
    public void updateLiquidity(Liquidity liquidity) {
        liquidityTable.put(liquidity.getLiquidityId(), liquidity);
    }

    // Remove record by ID
    public void removeLiquidity(String liquidityId) {
        liquidityTable.remove(liquidityId);
    }

    // Get all liquidity records
    public Map<String, Liquidity> getAllLiquidity() {
        return liquidityTable;
    }
}
