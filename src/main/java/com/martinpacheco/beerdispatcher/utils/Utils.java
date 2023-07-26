package com.martinpacheco.beerdispatcher.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinpacheco.beerdispatcher.model.Dispenser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static Logger logger = LoggerFactory.getLogger(Utils.class);


    public static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Dispenser asDispenser(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Dispenser d = objectMapper.readValue(json, Dispenser.class);
            return d;
        } catch (Exception e){
            logger.info( e.getMessage() );
            return null;
        }
    }


    public static Date convertStringToDate(String stringDate){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try{
            return formatter.parse(stringDate);
        }catch (Exception e){
            logger.error("Error while trying to convert date from string to date. Details." +  e.getMessage());
        }

        return null;

    }


}
