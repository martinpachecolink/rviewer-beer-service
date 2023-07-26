package com.martinpacheco.beerdispatcher.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"beerPrice"})
public class Dispenser {

    @Getter
    @Setter
    @JsonProperty("id")
    private String id;

    @Getter
    @Setter
    @JsonProperty("flow_volume")
    private Float flowVolume;

    @Getter
    @Setter
    @JsonProperty("amount")
    private Float amount;

    @Getter
    @Setter
    @JsonProperty("beer_price")
    private Float beerPrice;

    @Getter
    @Setter
    @JsonProperty("usages")
    public ArrayList<SpendingEvent> spendingEvents;



    public Dispenser() {
    }
    public Dispenser(Float flowVolume, Float beerPrice){

        this.id = UUID.randomUUID().toString();
        this.flowVolume = flowVolume;
        this.beerPrice = beerPrice;
        this.spendingEvents = new ArrayList<>();
        this.amount = 0f;

    }

    public void calculateAmount(){
        Float amount = 0f;
        for(SpendingEvent event : this.spendingEvents){
            amount = amount + event.getTotalSpent();
        }
        this.amount = amount;
    }

    @JsonIgnore
    public boolean isOpen(){

        if( this.spendingEvents.size() == 0){
            return false;
        } else {
            SpendingEvent lastSpendingEvent = this.spendingEvents.get( this.spendingEvents.size() - 1 );
            return lastSpendingEvent.isOpen();
        }
    }

    @JsonIgnore
    public boolean isClosed(){
        if( this.spendingEvents.size() == 0) {
            return false;
        } else {
            SpendingEvent lastSpendingEvent = this.spendingEvents.get( this.spendingEvents.size() - 1 );
            return lastSpendingEvent.isClosed();
        }
    }



}
