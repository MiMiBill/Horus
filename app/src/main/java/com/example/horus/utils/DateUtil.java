package com.example.horus.utils;

import android.text.TextUtils;

import com.example.horus.R;
import com.example.horus.app.MyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by lognyun on 2017/12/6 0006.
 * <p>
 * 处理时间日期相关的工具类
 */

public class DateUtil {
    public static final String YYMM = "yyyy-MM";
    public static final String HHMM = "HH:mm"; //24小时格式
    public static final String hhMM = "hh:mm"; //12小时格式
    public static final String HHMMSS = "HH:mm:ss";
    public static final String YYMMDD = "yyyy/MM/dd";
    public static final String YYMMDD_HORIZONTAL = "yyyy-MM-dd";
    public static final String YYMMDD_ACTIVITY_HORIZONTAL = "yyyy.MM.dd";
    public static final String YYMMDDHHMM_HORIZONTAL = "yyyy-MM-dd HH:mm";
    public static final String YYMMDDHHMMSS_HORIZONTAL = "yyyy-MM-dd HH:mm:ss";
    public static final String YYMMDDHHMM_NOTIFiCATION = "yyyyMMddHHmm";

    /**
     * 给定出生日期毫秒数，返回当前年龄
     *
     * @param mills 日期毫秒数
     * @return 年龄字符串
     */
    public static String getAgeFromMills( Long mills) {
        if (mills == null) {
            return null;
        }
        long between = System.currentTimeMillis() - mills;
        long year = between / 31536000000L;
        return String.valueOf(year);
    }

    public static String millsToFormat(Long mills, String format) {
        if (mills == null || TextUtils.isEmpty(format)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(mills));
    }

    public static String parseDate(String dateIn, String formatIn, String formatOut) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatIn, Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateIn);
            dateFormat = new SimpleDateFormat(formatOut, Locale.getDefault());
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateIn;
    }

    public static String getTodayStr(String format) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 给定年龄，返回时间戳
     * @param age
     * @return
     */
    public static long getMillsFromAgeStart(int age)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if (year - age > 0)
        {
            calendar.clear();
            calendar.set(Calendar.YEAR, year - age);

        }
        year = calendar.get(Calendar.YEAR);
        return calendar.getTimeInMillis();
    }

    /**
     * 给定年龄，返回时间戳
     * @param age
     * @return
     */
    public static long getMillsFromAgeEnd(int age)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        if (year - age > 0)
        {
            calendar.clear();
            calendar.set(Calendar.YEAR, year - age);
            calendar.roll(Calendar.DAY_OF_YEAR, -1);
        }
        return calendar.getTimeInMillis();
    }


    public static String getDateStr(Calendar calendar, String formatOut) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getTodayCalendar() {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA);
    }

    public static Calendar getMaxTime(Calendar srcCal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(srcCal.getTimeInMillis());

        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar;
    }

    public static Calendar getMinTime(Calendar srcCal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(srcCal.getTimeInMillis());

        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar;
    }

    public static String getChineseWeekday(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            case Calendar.SUNDAY:
                return "星期日";
            default:
                break;
        }
        return null;
    }

    public static String getTimeWithStype(long date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        return format.format(date);
    }



    public static String getYYMM(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMM);
        return format.format(date);
    }

    public static String getYYMMDD(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDD);
        return format.format(date);
    }

    public static String getYYMMDDHorizontalLine(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDD_HORIZONTAL);
        return format.format(date);
    }

    //获取今天的日期字符串
    public static String getYYMMDDHorizontalLineToday(){
        return getYYMMDDHorizontalLine(System.currentTimeMillis());
    }

    //获取今天的日期字符串
    public static String getYYMMDDTodayFromCalendar(){
        final Calendar c = Calendar.getInstance(Locale.US);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return "" + year + month + day;
    }

    //获取昨天的日期字符串
    public static String getYYMMDDHorizontalLineYesterday(){
        return getYYMMDDHorizontalLine(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
    }

    /**
     * 得到对应时区的当前时间，并按照系统格式取时间字符串
     *
     * @param date
     * @return
     */
    public static String getSystemHHMM(long date, TimeZone timeZone) {
        SimpleDateFormat format;
//        if (is24HourMode()){
            format = new SimpleDateFormat(HHMM); //统一24H格式
//        }else {
//            format = new SimpleDateFormat(hhMM, LanguageUtils.getCurrentLocale());
//        }

        format.setTimeZone(timeZone);
        return format.format(date);
    }

    /**
     * @return true if clock is set to 24-hour mode
     */
    public static boolean is24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(MyApp.getInstance());
    }


    //美国格式格式化日期字符串
    public static String getYYMMDDHorizontalLineUS(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDD_HORIZONTAL, Locale.US);
        return format.format(date);
    }

    public static String getYYMMDDHHMMHorizontalLine(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDDHHMM_HORIZONTAL);
        return format.format(date);
    }

    public static String getYYMMDDHHMMActivityHorizontalLine(long date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDD_ACTIVITY_HORIZONTAL);
        return format.format(date);
    }

    public static String getCurrentYYMMDDHHMMH() {
        SimpleDateFormat format = new SimpleDateFormat(YYMMDDHHMM_HORIZONTAL);
        return format.format(System.currentTimeMillis());
    }

    public static String long2String(Long date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }


    public static String getYYMM(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(YYMM);
        return format.format(date);
    }

    public static long str2Long(String strDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date date = dateFormat.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }

    public static long str2LongUS(String strDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            Date date = dateFormat.parse(strDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return System.currentTimeMillis();
    }


    /**
     * 判定日期是否相等
     */
    public static boolean equalDate(long timeMills1, long timeMills2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTimeInMillis(timeMills1);
        date2.setTimeInMillis(timeMills2);
        if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
                && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
                && date1.get(Calendar.DATE) == date2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }


    public static String getYearMonthDate(int year, int month) {
        return MyApp.getInstance().getString(R.string.wallet_recharge_record_year_month_input, String.valueOf(year), getMonthDate(month));

    }

    public static String getMonthDate(int month) {
        return MyApp.getInstance().getResources().getStringArray(R.array.month)[month - 1];
    }

    public static String getYearDate(int year) {
        return MyApp.getInstance().getString(R.string.wallet_recharge_record_year_input, year);
    }


}
