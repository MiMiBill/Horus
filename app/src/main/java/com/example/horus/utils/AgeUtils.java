package com.example.horus.utils;

/**
 * des:
 * author: lognyun
 * date: 2018/11/13 19:00
 */
public class AgeUtils {
    /**
     * 1 年
     */
    private static final long ONE_YEAR=365*24*60*60*1000;

    public static String getAge(Long age){
        if (age == null) {
            return null;
        }
        long between = System.currentTimeMillis() - age;
        long year = between / 31536000000L;
        return String.valueOf(year) ;

    }


    public static String getAgeWithY(Long age) {
        String ageStr = getAge(age);

        if (ageStr != null) {
            return ageStr + "y";
        }
        return "";
    }
}
