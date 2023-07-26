package com.martinpacheco.beerdispatcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateDispenser {
    @JsonProperty("flow_volume")
    private Float flowVolume;

    public CreateDispenser(){}

    public CreateDispenser(Float flowVolume){
        this.flowVolume = flowVolume;
    }

    public Float getFlowVolume() {
        return flowVolume;
    }

    public void setFlowVolume(Float flowVolume) {
        this.flowVolume = flowVolume;
    }
}
