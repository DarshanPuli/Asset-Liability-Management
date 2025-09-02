package org.example.Dao;

import org.example.entity.CounterParty;

import java.util.UUID;

public interface CounterPartyDao {

    public CounterParty getCounterParty(UUID counterPartyId);
    public CounterParty addCounterParty(CounterParty counterParty);
    public CounterParty updateCounterParty(CounterParty counterParty);
    public CounterParty deleteCounterParty(UUID counterPartyId);
    public int updateCount(UUID assetId, UUID counterPartyId);
}
