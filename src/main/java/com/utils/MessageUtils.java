package com.utils;

import com.bean.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {
    public static String getMessage(boolean isSystem, Integer fromId, Object message) {
        try {
            ResultMessage result = new ResultMessage();
            result.setSystem(isSystem);
            result.setMessage(message);
            if (fromId != null) {
                result.setFromId(fromId);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
