package com.example.horus.utils;

import com.example.horus.R;
import com.example.horus.app.MyApp;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 */
public class TimeUtil {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;


    /**
     * 评论时间
     *
     * @param timeStamp mills
     * @return 当天显示 15:02 本年度显示 03-22 去年显示2018-03-22
     */
    public static String getCircleTimeStr(Long timeStamp) {
        if (timeStamp == null || timeStamp <= 0) return "";

        Date inputDate = new Date(timeStamp);

        Calendar nowTime = Calendar.getInstance();

        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);

        int yearInput = inputTime.get(Calendar.YEAR);
        int monthInput = inputTime.get(Calendar.MONTH);
        int dateInput = inputTime.get(Calendar.DATE);

        if (yearInput != nowTime.get(Calendar.YEAR)) {
            return String.format(Locale.getDefault(), "%tF", inputDate);// 2019-03-21
        } else if (monthInput != nowTime.get(Calendar.MONTH) || dateInput != nowTime.get(Calendar.DATE)) {
            return String.format(Locale.getDefault(), "%tm-%td", inputDate, inputDate);
        } else {
            return String.format(Locale.getDefault(), "%tR", inputDate);// HH:MM（24时制）
        }

    }


    public static String getTimeStr(Long timeStamp) {
        return getTimeStr(timeStamp, false);
    }
    // 来自IM sample

    /**
     * 时间转化为显示字符串
     */
    public static String getTimeStr(Long timeStamp, boolean moments) {
        if (timeStamp == null || timeStamp == 0) return "";

        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();

        int hour = (int) ((System.currentTimeMillis() - timeStamp) / ONE_HOUR);

        if (hour <= 24) {
            if (hour <= 0) {
                int minute = (int) ((System.currentTimeMillis() - timeStamp) / ONE_MINUTE);

                if (minute <= 0) {
                    return MyApp.getInstance().getString(R.string.moments_new);
//                    return MyApp.getInstance().getString(moments?R.string.moments_new:R.string.now);
                } else {
                    return MyApp.getInstance().getString(R.string.other_minute_ago, minute);

                }
            }

            return MyApp.getInstance().getString(R.string.other_hour_ago, hour);

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(currenTimeZone);
        }


    }

    /**
     * 时间转化为聊天界面显示字符串
     * 调试发现入参时间戳可能比Calendar.getInstance()所得时间大 暂时不明白为什么
     *
     * @param timeStamp 单位毫秒
     */
    public static String getChatTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();

        // 调试发现入参时间戳可能比Calendar.getInstance()所得时间大
        // 原因未知 先注释掉这一段
//        if (!calendar.after(inputTime)){
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            return sdf.format(currenTimeZone);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return MyApp.getInstance().getResources().getString(R.string.chat_time_yesterday) + " " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M-d HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdf.format(currenTimeZone);
            }

        }

    }


    // 下面的方法来自Lawu
    public static String getTimeShowString(long milliseconds, boolean abbreviate) {
        String dataString;
        String timeStringBy24;

        Date currentTime = new Date(milliseconds);
        Date today = new Date();
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date todaybegin = todayStart.getTime();
        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);

        if (!currentTime.before(todaybegin)) {
            dataString = MyApp.getInstance().getString(R.string.chat_time_today);
        } else if (!currentTime.before(yesterdaybegin)) {
            dataString = MyApp.getInstance().getString(R.string.chat_time_yesterday);
        } else if (isSameWeekDates(currentTime, today)) {
            dataString = getWeekOfDate(currentTime);
        } else {
            SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
            dataString = dateformatter.format(currentTime);
        }

        SimpleDateFormat timeformatter24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeStringBy24 = timeformatter24.format(currentTime);

        if (abbreviate) {
            if (!currentTime.before(todaybegin)) {
                return getTodayTimeBucket(currentTime, null);
            } else {
                return dataString;
            }
        } else {
            return dataString + " " + timeStringBy24;
        }
    }

    /**
     * 根据不同的时区，显示不同时间段的字符串
     *
     * @param date
     * @return
     */
    public static String getTodayTimeBucket(Date date, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeformatter0to11 = new SimpleDateFormat("KK:mm", Locale.getDefault());
        SimpleDateFormat timeformatter1to12 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        if (timeZone != null) {
            timeformatter0to11.setTimeZone(timeZone);
            timeformatter1to12.setTimeZone(timeZone);
            calendar.setTimeZone(timeZone);
        }
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 5) {
            return MyApp.getInstance().getString(R.string.chat_time_early_morning) + timeformatter0to11.format(date);
        } else if (hour >= 5 && hour < 12) {
            return MyApp.getInstance().getString(R.string.chat_time_morning) + timeformatter0to11.format(date);
        } else if (hour >= 12 && hour < 18) {
            return MyApp.getInstance().getString(R.string.chat_time_afternoon) + timeformatter1to12.format(date);
        } else if (hour >= 18 && hour < 24) {
            return MyApp.getInstance().getString(R.string.chat_time_night) + timeformatter1to12.format(date);
        }
        return "";
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {MyApp.getInstance().getString(R.string.chat_time_sun),
                MyApp.getInstance().getString(R.string.chat_time_mon),
                MyApp.getInstance().getString(R.string.chat_time_tues),
                MyApp.getInstance().getString(R.string.chat_time_wed),
                MyApp.getInstance().getString(R.string.chat_time_thur),
                MyApp.getInstance().getString(R.string.chat_time_fri),
                MyApp.getInstance().getString(R.string.chat_time_sat)};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }




    /**
     * 根据时间毫秒数返回时长字符串（1:12:20）
     */
    public static String getTimeString(long duration) {
        int time = (int) (duration / 1000);
        String hh = new DecimalFormat("00").format(time / 3600);
        String mm = new DecimalFormat("00").format(time % 3600 / 60);
        String ss = new DecimalFormat("00").format(time % 60);

        StringBuilder sb = new StringBuilder();
        sb.append(hh).append(":").append(mm).append(":").append(ss);

        return sb.toString();
    }

    /**
     * 根据时间毫秒数返回分钟秒数字符串
     *
     * @param duration
     * @return
     */
    public static String getmmssStr(long duration) {
        int time = (int) (duration / 1000);
        String mm = new DecimalFormat("00").format(time / 60);
        String ss = new DecimalFormat("00").format(time % 60);

        StringBuilder sb = new StringBuilder();
        sb.append(mm).append(":").append(ss);

        return sb.toString();
    }
}
