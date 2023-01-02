package com.campfire.campafar.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateProcessor含有日期处理相关的工具方法
 */
public class DateProcessor {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //基本日期模板
    private static final Calendar calendar = Calendar.getInstance(); //获取Calendar实例
    private static final long MILLIS_IN_DAY = 86400000; //一天的毫秒数

    /**
     * getInitialTime 可以获取时间戳0代表的时间
     * @return 1970-1-1 00:00:00
     */
    public static Date getInitialTime(){
        //return date-typed value of 1970-1-1
        calendar.set(1970, Calendar.JANUARY, 1, 0, 0, 0);//Note that month starts from 0 in Calendar

        return calendar.getTime();
    }

    public static Date getDateFromString(String newDate){
        //get a date obj from a string
        Date parsedNow;
        try{
            parsedNow = dateFormat.parse(newDate);
        }catch(ParseException e){
            //when parsing failed, set time to 1970-1-1
            parsedNow = getInitialTime();
        }

        return parsedNow;
    }

    public static Calendar getCalendarOfToday(){
        Date now = new Date();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return (Calendar) calendar.clone();
    }

    public static String getDateStringFromTimestamp(Date date){
        if(date == null){
            return "null";
        }

        return dateFormat.format(date);
    }

    private static long millisToDays(Date millis,TimeZone timeZone){
        return (((long)timeZone.getOffset(millis.getTime())) + millis.getTime()) / MILLIS_IN_DAY;
    }

    public static boolean isSameDate(Date date1, Date date2, TimeZone timeZone){
        if(date1 == null){
            date1 = new Date(0);
        }
        if(date2 == null){
            date2 = new Date(0);
        }

        long interval = date1.getTime() - date2.getTime();

        return interval < MILLIS_IN_DAY
                && interval > -MILLIS_IN_DAY
                && millisToDays(date1,timeZone) == millisToDays(date2,timeZone);
    }

}
