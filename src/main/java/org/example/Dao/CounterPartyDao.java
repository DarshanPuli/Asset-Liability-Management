package org.example.Dao;

import org.example.entity.CounterParty;

import java.sql.SQLException;
import java.util.UUID;

public interface CounterPartyDao {

    public CounterParty getCounterParty(UUID counterPartyId);
    public void addCounterParty(CounterParty counterParty) throws SQLException;
    public CounterParty updateCounterParty(CounterParty counterParty);
    public CounterParty deleteCounterParty(UUID counterPartyId);
    public int updateCount(UUID assetId, UUID counterPartyId);
}
