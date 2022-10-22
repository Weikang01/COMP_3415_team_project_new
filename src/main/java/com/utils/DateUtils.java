package com.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static Date parseDate(String strDate) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        java.util.Date date3 = null;
        try {
            date3 = sdf2.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Date(date3.getTime());
    }
}
