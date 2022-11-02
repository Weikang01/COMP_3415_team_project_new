package com.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string);
    }

    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
