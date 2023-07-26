package com.martinpacheco.beerdispatcher;

import com.martinpacheco.beerdispatcher.controller.BeerController;
import com.martinpacheco.beerdispatcher.dto.CreateDispenser;
import com.martinpacheco.beerdispatcher.model.Dispenser;
import com.martinpacheco.beerdispatcher.dto.PutDispenserStatus;
import com.martinpacheco.beerdispatcher.utils.Utils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BeerControllerTest {
    public static Logger logger = LoggerFactory.getLogger(BeerControllerTest.class);

    @Autowired
    private MockMvc mockMvc;



    public MvcResult createNewDispenser() throws Exception{

        // Create a new dispenser
        MvcResult mvcResult = this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/dispenser")
                                .content( Utils.asJsonString( new CreateDispenser(0.0653f) ) )
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect( status().isOk() )
                .andDo(print())
                .andReturn();

        return mvcResult;
    }

    public MvcResult openDispenser( String dispenserId ) throws Exception{

        PutDispenserStatus openEvent = new PutDispenserStatus();
        openEvent.status = "open";
        openEvent.updatedAt = Utils.convertStringToDate("2020-01-10T00:00:00Z" );


        MvcResult openDispenserResponse = this.mockMvc.perform(
                    MockMvcRequestBuilders.put("/dispenser/" + dispenserId + "/spending")
                            .content( Utils.asJsonString(openEvent) )
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andReturn();

        return openDispenserResponse;
    }

    public MvcResult closeDispenser( String dispenserId ) throws Exception{

        PutDispenserStatus openEvent = new PutDispenserStatus();
        openEvent.status = "close";
        openEvent.updatedAt = Utils.convertStringToDate("2020-01-10T00:00:50Z" );


        MvcResult closeDispenserResponse = this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/dispenser/" + dispenserId + "/spending")
                                .content( Utils.asJsonString(openEvent) )
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andReturn();

        return closeDispenserResponse;
    }




    public MvcResult getDispenserInfo(String dispenserId) throws  Exception {

        // Make get call
        MvcResult mvcResult = this.mockMvc.perform(
                    MockMvcRequestBuilders.get("/dispenser/" + dispenserId + "/spending")
                )
                .andDo(print())
                .andReturn();

        return mvcResult;

    }





    @Test
    public void itShouldCreateOneDispenser() throws Exception {

        // Create a new dispenser
        MvcResult mvcResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( mvcResult.getResponse().getContentAsString() );

        // Dispenser UUID assigned
        assert (dispenser.getId().length() == 36);

        // Status code equals 200
        assert(mvcResult.getResponse().getStatus() == HttpStatus.OK.value());

    }


    @Test
    public void itShouldOpenDispenser() throws Exception {

        // Create a new dispenser
        MvcResult dispenserCreationResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( dispenserCreationResult.getResponse().getContentAsString() );

        // Open dispenser
        MvcResult openDispenserResult = openDispenser( dispenser.getId() );

        // Status code equals 202
        assert(openDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());
    }


    @Test
    public void itShouldCloseDispenser() throws Exception {

        // Create a new dispenser
        MvcResult dispenserCreationResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( dispenserCreationResult.getResponse().getContentAsString() );

        // Open dispenser
        MvcResult openDispenserResult = openDispenser( dispenser.getId() );

        // Opened Status code equals 202
        assert(openDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());



        // Close dispenser
        MvcResult closeDispenserResult = closeDispenser( dispenser.getId() );

        // Closed Status code equals 202
        assert(closeDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());

    }

    @Test
    public void itShouldFailWhenTriesOpenAfterOpenEvent() throws Exception {

        // Create a new dispenser
        MvcResult dispenserCreationResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( dispenserCreationResult.getResponse().getContentAsString() );

        // Open dispenser
        MvcResult openDispenserResult = openDispenser( dispenser.getId() );

        // First Opened Status code equals 202
        assert(openDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());


        // Open dispenser
        MvcResult openDispenserResult2 = openDispenser( dispenser.getId() );

        // Second opened Status code equals 409
        assert(openDispenserResult2.getResponse().getStatus() == HttpStatus.CONFLICT.value());

    }

    @Test
    public void itShouldFailWhenTriesClosedEventWithoutOpeningFirst() throws Exception{
        // Create a new dispenser
        MvcResult dispenserCreationResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( dispenserCreationResult.getResponse().getContentAsString() );

        // Close dispenser
        MvcResult closeDispenserResult = closeDispenser( dispenser.getId() );

        // Closed Status code equals 500
        assert(closeDispenserResult.getResponse().getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

    @Test
    public void itShouldGetDispenserInformation() throws Exception {

        // Create a new dispenser
        MvcResult dispenserCreationResult = createNewDispenser();
        Dispenser dispenser = Utils.asDispenser( dispenserCreationResult.getResponse().getContentAsString() );


        // Open dispenser
        MvcResult openDispenserResult = openDispenser( dispenser.getId() );
        // Opened Status code equals 202
        assert(openDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());


        // Close dispenser
        MvcResult closeDispenserResult = closeDispenser( dispenser.getId() );
        // Closed Status code equals 202
        assert(closeDispenserResult.getResponse().getStatus() == HttpStatus.ACCEPTED.value());


        // Open dispenser
        MvcResult openDispenserResult2 = openDispenser( dispenser.getId() );
        // Opened Status code equals 202
        assert(openDispenserResult2.getResponse().getStatus() == HttpStatus.ACCEPTED.value());


        // Close dispenser
        MvcResult closeDispenserResult2 = closeDispenser( dispenser.getId() );
        // Closed Status code equals 202
        assert(closeDispenserResult2.getResponse().getStatus() == HttpStatus.ACCEPTED.value());


        // Get dispenser info
        MvcResult getDispenserInfo = getDispenserInfo( dispenser.getId() );
        assert(getDispenserInfo.getResponse().getStatus() == HttpStatus.OK.value());

    }


    @Test
    public void itShouldNotFindDispenser() throws Exception {
        String falseId = "AAAAAAA-BBBBBBB-CCCCCCCC-DDDDDDDD";

        // Get dispenser info
        MvcResult getDispenserInfo = getDispenserInfo( falseId );
        logger.info( getDispenserInfo.getResponse().getStatus() + "" );
        assert(getDispenserInfo.getResponse().getStatus() == HttpStatus.NOT_FOUND.value());

    }







}
