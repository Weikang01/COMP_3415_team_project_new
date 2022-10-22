package com.utils;

import com.bean.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.ECMAException;

public class MessageUtils {
    public static String getMessage(boolean isSystem, String fromUsername, Object message) {
        try {
            ResultMessage result = new ResultMessage();
            result.setSystem(isSystem);
            result.setMessage(message);
            if (fromUsername != null) {
                result.setFromName(fromUsername);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
