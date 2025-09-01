package db;

import entity.Asset;
import entity.CounterParty;

import java.util.ArrayList;

public class CounterPartyDB {

    ArrayList<CounterParty> counterParties = new ArrayList<CounterParty>();

    CounterPartyDB(){};

    private static CounterPartyDB instance;

    public static CounterPartyDB getInstance(){
        if(instance==null){
            instance = new CounterPartyDB();
            return  instance;
        }else{
            return instance;
        }
    }

    public void addCounterParty(CounterParty counterParty){
        counterParties.add(counterParty);
    }

}
