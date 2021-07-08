package com.example.horus.utils;


import com.example.horus.BuildConfig;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * des: 错误上报工具类
 * Debug 模式下 未初始化 Bugly
 */
public class BuglyUtils {

    public static final boolean OPEN_REPORT = !BuildConfig.DEBUG;
//    public static final boolean OPEN_REPORT = true;// 调试用

    /**
     * 当前设备的userID
     * 报错的时候上传
     */
    public static void setUserID(String userID) {
        if (OPEN_REPORT) {
            CrashReport.setUserId(userID);  //该用户本次启动后的异常日志用户ID都将是userID
        }
    }


    /**
     * 主动上报异常
     */
    public static void postCatchedException(Throwable throwable) {
        if (OPEN_REPORT) {
            CrashReport.postCatchedException(throwable);
        }
    }

}
