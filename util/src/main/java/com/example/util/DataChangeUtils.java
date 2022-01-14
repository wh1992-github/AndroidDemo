package com.example.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataChangeUtils {

    //获取现在时间
    public static String getNowStringShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //获取现在时间
    public static String getNowStringLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //获取时间 HH:mm:ss
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(strDate, pos);
        return strToDate;
    }

    //将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    //将短时间格式字符串转换为时间 yyyy-MM-dd
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(strDate, pos);
        return strToDate;
    }

    //将短时间格式时间转换为字符串 yyyy-MM-dd
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    //得到现在时间
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    //提取一个月中的最后一天
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    //得到现在时间
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //得到现在小时
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    //得到现在分钟
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    //根据用户传入的时间表示格式,返回当前时间的格式 如果是yyyyMMdd,注意字母y不能大写。
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat, Locale.getDefault());
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    //二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式,返回字符型的分钟
    public static String getTwoHour(String st1, String st2) {
        String[] kk;
        String[] jj;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
            return "0";
        else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
                    / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
                    / 60;
            if ((y - u) > 0)
                return y - u + "";
            else
                return "0";
        }
    }

    //得到二个日期间的间隔天数
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        long day;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    //时间前推或后推分钟,其中JJ表示分钟.
    public static String getPreTime(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String mydate1 = "";
        try {
            Date date1 = format.parse(sj1);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception ignored) {
        }
        return mydate1;
    }

    //得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
    public static String getNextDay(String nowdate, String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String mdate;
            Date d = strToDate(nowdate);
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    //得到现在距离0点的时间差
    public static String getTimeToZero() {
        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat formatShort = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat formatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String strToday = formatShort.format(now) + " 00:00:00";
            Date date = formatLong.parse(strToday);
            long tomorrow = date.getTime() + 24 * 60 * 60 * 1000;
            return getTimeString(tomorrow - now);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取时间
    public static String getTimeString(long timeMills) {
        int timeSecond = (int) (timeMills / 1000);
        int second = timeSecond % 60;
        int minute = timeSecond / 60 % 60;
        int hour = timeSecond / 60 / 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    //判断是否润年
    public static boolean isLeapYear(String ddate) {
        //详细设计：
        //1.被400整除是闰年
        //2.不能被4整除则不是闰年
        //3.能被4整除同时能被100整除则不是闰年
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            return (year % 100) != 0;
        } else {
            return false;
        }
    }

    //返回美国时间格式 26 Apr 2006
    public static String getEDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        Date strToDate = formatter.parse(str, pos);
        String j = strToDate.toString();
        String[] k = j.split(" ");
        return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
    }

    //获取一个月的最后一天
    public static String getEndDateOfMonth(String date) {//yyyy-MM-dd
        String str = date.substring(0, 8);
        String month = date.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(date)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    //判断二个时间是否在同一个周
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);
        } else if (1 == subYear && Calendar.DECEMBER == cal2.get(Calendar.MONTH)) {
            //如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);
        } else if (-1 == subYear && Calendar.DECEMBER == cal1.get(Calendar.MONTH)) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);
        }
        return false;
    }

    //产生周序列,即得到当前时间所在的年度是第几周
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1)
            week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }
}
