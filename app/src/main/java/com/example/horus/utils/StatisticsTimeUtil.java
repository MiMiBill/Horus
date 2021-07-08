package com.example.horus.utils;

import com.example.baselib.utils.LogUtil;



public class StatisticsTimeUtil {

    private static long preTime = 0L;
    private static long initTime = 0L;

    public static void initTime(){
        initTime = System.currentTimeMillis();
    }

    public static void showDiffTime(String name){
        float DiffTime = (System.currentTimeMillis() - preTime) / 1000f;
        float fromInitTime = (System.currentTimeMillis() - initTime)/ 1000f;
        LogUtil.d("StatisticsTimeUtil",name + " DiffTime:" + DiffTime + " fromInitTime:" + fromInitTime);
        preTime = System.currentTimeMillis();
    }

}
