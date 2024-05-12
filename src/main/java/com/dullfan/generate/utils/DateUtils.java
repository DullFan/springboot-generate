package com.dullfan.generate.utils;

import com.dullfan.generate.utils.extremely.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYYMMDD = "yyyy/MM/dd";

    private static String format(Date date,String patten){
        return new SimpleDateFormat(patten).format(date);
    }

    public static Date parse(String date,String patten){
        try {
            return new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            throw new ServiceException(e);
        }
    }

}
