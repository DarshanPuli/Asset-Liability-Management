package db;

import entity.Asset;
import entity.CounterParty;

import java.util.ArrayList;

public class CounterPartyDB {

    ArrayList<CounterParty> counterParties = new ArrayList<CounterParty>();

    CounterPartyDB(){};

    private static CounterPartyDB instance;

    public static CounterPartyDB getInstance(){
        return CounterPartyDB.instance!=null?CounterPartyDB.instance:new CounterPartyDB();
    }

    public void addCounterParty(CounterParty counterParty){
        counterParties.add(counterParty);
    }

}
