package com.martinpacheco.beerdispatcher.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpendingEvent {
    public static Logger logger = LoggerFactory.getLogger(SpendingEvent.class);

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("opened_at")
    public Date openedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonProperty("closed_at")
    public Date closedAt;

    @JsonProperty("flow_volume")
    public Float flowVolume;

    @JsonProperty("total_spent")
    public Float totalSpent;

    public SpendingEvent(Date openedAt, Float flowVolume){
        this.openedAt = openedAt;
        this.closedAt = null;
        this.flowVolume = flowVolume;
        this.totalSpent = 0f;
    }

    public boolean isOpen(){
        return closedAt == null;
    }

    public boolean isClosed(){
        return closedAt != null;
    }

    public void calculateTotalSpent( Float beerPrice){

        long seconds = ( this.closedAt.getTime() - this.openedAt.getTime()) / 1000;

        this.totalSpent = this.flowVolume * seconds * beerPrice;

    }



    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Float getFlowVolume() {
        return flowVolume;
    }

    public void setFlowVolume(Float flowVolume) {
        this.flowVolume = flowVolume;
    }

    public Float getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(Float totalSpent) {
        this.totalSpent = totalSpent;
    }
}
