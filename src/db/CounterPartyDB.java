package db;

import entity.Asset;
import entity.CounterParty;

import java.util.ArrayList;

public class CounterPartyDB {

    ArrayList<CounterParty> assets = new ArrayList<CounterParty>();

    CounterPartyDB(){};

    private static CounterPartyDB instance;

    public static CounterPartyDB getInstance(){
        return CounterPartyDB.instance!=null?CounterPartyDB.instance:new CounterPartyDB();
    }

}
