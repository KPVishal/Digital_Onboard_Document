package com.bijlipay.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {
    public static String parseJson(Object input) {
        String output = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert user object to json string and return it
            output = mapper.writeValueAsString(input);
        }
        catch (JsonProcessingException e) {
            // catch various errors
            e.printStackTrace();
        }
        return output;
    }
}
