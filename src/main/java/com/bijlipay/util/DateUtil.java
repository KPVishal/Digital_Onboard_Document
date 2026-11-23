package com.bijlipay.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    // convert long to date format
    public static Date longToDate(long longDate){
        Date date = new Date(TimeUnit.SECONDS.toMillis(longDate));

        return date;
    }

    // get current date
    public static Date currentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return date;
    }
    public static String currentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        String formattedDate =dateFormat.format(date);
        return formattedDate;
    }
    public static String formattedDate(Date memberSince, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        Date date1 = new Date();
        String formattedDate = dateFormat.format(memberSince);
        return formattedDate;
    }
    public static String formattedTime(Date memberSince, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
        Date date1 = new Date();
        String formattedDate = dateFormat.format(memberSince);
        return formattedDate;
    }
    public static int getPreviousYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);
        return prevYear.get(Calendar.YEAR);
    }

    public static String currentDateAndMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
        Date date = new Date();
        String formattedDate =dateFormat.format(date);
        return formattedDate;
    }

    public static int getUpComingYear() {
        Calendar comingYear = Calendar.getInstance();
        comingYear.add(Calendar.YEAR, 1);
        return comingYear.get(Calendar.YEAR);
    }
}
