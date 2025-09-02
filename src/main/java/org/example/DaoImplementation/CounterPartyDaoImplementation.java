package org.example.DaoImplementation;

import org.example.Dao.CounterPartyDao;
import org.example.entity.CounterParty;

import java.util.UUID;

public class CounterPartyDaoImplementation implements CounterPartyDao {
    @Override
    public CounterParty getCounterParty(UUID counterPartyId) {
        return null;
    }

    @Override
    public CounterParty addCounterParty(CounterParty counterParty) {
        return null;
    }

    @Override
    public CounterParty updateCounterParty(CounterParty counterParty) {
        return null;
    }

    @Override
    public CounterParty deleteCounterParty(UUID counterPartyId) {
        return null;
    }

    @Override
    public int updateCount(UUID assetId, UUID counterPartyId) {
        return 0;
    }
}
