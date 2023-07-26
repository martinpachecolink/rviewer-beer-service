
package com.martinpacheco.beerdispatcher.controller;

import com.martinpacheco.beerdispatcher.dto.CreateDispenser;
import com.martinpacheco.beerdispatcher.model.Dispenser;
import com.martinpacheco.beerdispatcher.dto.PutDispenserStatus;
import com.martinpacheco.beerdispatcher.service.BeerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class BeerController {

    public static BeerService beerService = new BeerService();
    public static Logger logger = LoggerFactory.getLogger(BeerController.class);

    // Creates a new dispenser
    @PostMapping("/dispenser")
    public static Dispenser addNewDispenser(@RequestBody CreateDispenser dispenserDto){
        try {
            Dispenser dispenser = new Dispenser( dispenserDto.getFlowVolume(), 12.25f );
            beerService.addDispenser( dispenser );
            return dispenser;
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error happened. The dispenser was not created.", e);
        }
    }


    @PutMapping("/dispenser/{id}/spending")
    public static ResponseEntity<String> setDispenserStatus(@PathVariable String id, @RequestBody PutDispenserStatus putDispenserStatus){


        try {

            Dispenser dispenser = beerService.getDispenserById(id);

            // If status == open : Create a new event
            if( putDispenserStatus.status.equals("open")){

                // Is already opened ?
                if( dispenser.isOpen() ){
                    return  ResponseEntity.status(409).body("This dispenser is already open");
                }

                beerService.createDispenserEvent(id, putDispenserStatus.updatedAt);

            }

            // If status == closed : Update the last event in the event list
            if(putDispenserStatus.status.equals("close")){

                //Is already closed
                if( dispenser.isClosed() ){
                    return  ResponseEntity.status(409).body("This dispenser is already open");
                }

                beerService.updateDispenserEvent(id, putDispenserStatus.updatedAt);

            }

            return ResponseEntity.accepted().build();

        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error happened. The dispenser update was not made.", e);
        }
    }



    @GetMapping("/dispenser/{id}/spending")
    public static Dispenser getDispenser(@PathVariable String id){

        try{
            Dispenser dispenser = beerService.getDispenserById(id);

            if( dispenser != null){
                return  dispenser;
            }

        } catch (Exception ex){

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while fetching the dispenser information.", ex);
        }


        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This dispenser does not exist.");


    }


}
