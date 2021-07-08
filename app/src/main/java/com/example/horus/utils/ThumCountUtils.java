package com.example.horus.utils;

/**
 * des: 把数字转字符串
 * 根据语言 确定拼接的字符串
 */
public class ThumCountUtils {


    /**
     *
     * 中文 拼接 W
     * 其他拼接K
     */
    public static String getCount(int count){

        if(count>10000){
            return count/10000+"W";
        }

        if(count>1000){
            return count/1000+"K";
        }
        return String.valueOf(count);


    }
}
