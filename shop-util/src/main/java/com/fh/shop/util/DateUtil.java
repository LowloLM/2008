package com.fh.shop.util;

import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String FULL_YEAR = "yyyy-MM-dd HH:mm:ss";

    public static final String Y_M_D = "yyyy-MM-dd";

    public static final String Y_M = "yyyy-MM";

    public static Date addMinute(Date date,int minutes){
        DateTime dateTime = new DateTime(date);
        DateTime newDateTime = dateTime.plusMillis(minutes);
        return newDateTime.toDate();
    }

    public static Date addMM(Date date,int i){
        DateTime dateTime = new DateTime(date);
        DateTime dateTime1 = dateTime.plusMonths(i);
        Date date1 = dateTime1.toDate();
        return date1;
    }


    public static String date2str(Date date,String pattern){
        if(date == null){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


    public static Date str2Date(String date,String pattern){
        if (StringUtils.isEmpty(date)){
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
