package org.my.Test.utils;

/**
 * Created by yangj on 2016/8/28.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 *
 * @author shimiso
 */

public class DateUtil {
    private static final String FORMAT = "yyyy-MM-dd";

    public static Date getFirstDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        Date firstDate = null;
        try {
            String year = sdf.format(date).substring(0,4);
            firstDate = sdf.parse(String.format("%1$s-01-01",year));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return firstDate;
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }

    public static Date parse(String date){
        if(date == null || date.isEmpty()) return null;
        Date d = null;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);

        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date parse(String date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static long diff(Date start,Date end){
        if(start == null){
            return 0;
        }else{
            return end.compareTo(start);
        }
    }

    public static String getWeek(Date date) {
        String Week = "";
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 1:
                Week += "天";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
        }
        return Week;
    }
}
