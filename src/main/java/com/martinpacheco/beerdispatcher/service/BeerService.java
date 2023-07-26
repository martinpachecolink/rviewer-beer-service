package com.martinpacheco.beerdispatcher.service;

import com.martinpacheco.beerdispatcher.controller.BeerController;
import com.martinpacheco.beerdispatcher.model.Dispenser;
import com.martinpacheco.beerdispatcher.model.SpendingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BeerService {

    public static Logger logger = LoggerFactory.getLogger(BeerController.class);

    public Map<String, Dispenser> dispensersById;

    public BeerService(){
        this.dispensersById = new HashMap<>();

    }

    public Dispenser getDispenserById(String id){

        if( this.dispensersById.containsKey(id) ){
            return this.dispensersById.get(id);
        }

        return null;

    }

    public void addDispenser(Dispenser dispenser){
        this.dispensersById.put(dispenser.getId(), dispenser);
    }

    // Add a new event to the event list
    public void createDispenserEvent(String dispenserId, Date openedAt) {
        Dispenser dispenser = this.dispensersById.get(dispenserId);

        dispenser.spendingEvents.add( new SpendingEvent( openedAt, dispenser.getFlowVolume() ) );
    }



    public void updateDispenserEvent(String id, Date closedAt){
        Dispenser dispenser = this.dispensersById.get(id);
        SpendingEvent lastSpendingEvent = dispenser.spendingEvents.get( dispenser.spendingEvents.size() - 1 );
        lastSpendingEvent.setClosedAt( closedAt );

        lastSpendingEvent.calculateTotalSpent( dispenser.getBeerPrice() );

        this.dispensersById.get(id).calculateAmount();
    }

}
