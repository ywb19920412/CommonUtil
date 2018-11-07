package com.strongit.android.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * 日期转换工具类
 * @author yangwb
 * @date 2017-7-19 上午11:51:12
 */
public class BADateUtil {

    public static final String DATE_FORMAT_BIRTHDAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_LASTOFFDATE = "yyyy-MM-dd HH:mm:ss";

    /**
     * 转换 long对象为String
     * @author yangwb
     * @date 2017-7-19 上午11:50:22
     * @param time
     * @return
     */
    public static String showLongTimeToString(long time) {
        Date dTime = new Date(time);
        Date nowTime = new Date();
        if (dateToString(nowTime, "yyyy/MM/dd").equals(dateToString(dTime, "yyyy/MM/dd"))) {
            return dateToString(dTime, "HH:mm");
        } else if (dateToString(nowTime, "yyyy").equals(dateToString(dTime, "yyyy"))) {
            return dateToString(dTime, "MM/dd");
        } else {
            return dateToString(dTime, "yyyy/MM/dd");
        }
    }

    /**
     * 转换 Date对象为String
     *
     * @param date
     * @param model
     * @return
     */
    public static String dateToString(Date date, String model) {
        SimpleDateFormat sdf = new SimpleDateFormat(model, Locale.getDefault());
        return sdf.format(date);
    }
    /**
     * 转换long为String
     * @author yangwb
     * @date 2017-7-19 上午11:49:10
     * @param time
     * @param format
     * @return
     */
    public static String longToString(long time, String format) {
        Date date = new Date(time);
        return dateToString(date, format);
    }
    /**
     * 转换 String为Date对象
     * @author yangwb
     * @date 2017-7-19 上午11:49:35
     * @param dateStr
     * @param model
     * @return
     */
    public static Date stringToDate(String dateStr, String model) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(model, Locale.getDefault());
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 转换 Date对象为long
     * @author yangwb
     * @date 2017-7-19 上午11:49:57
     * @param time
     * @param model
     * @return
     */
    public static long strDateToLong(String time, String model) {
        Date date = stringToDate(time, model);
        return date.getTime();
    }
    /**
     * 计算指定日期与当前日期之间相差的天数
     * @param endDate
     * @return
     */
    public static String daysBetween(String endDate){
        String text = "";
        long currentTime=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat(DATE_FORMAT_BIRTHDAY);
        long endTime = 0;
        long between_days=0;
        try {
            endTime = format.parse(endDate).getTime();
            if (currentTime >=endTime){
                between_days=(currentTime-endTime)/(1000*3600*24);
                text="超时" + between_days + "天";
            } else {
                between_days=(endTime-currentTime)/(1000*3600*24)+1;
                text = "剩余" + between_days + "天超时";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return text;
    }
}
