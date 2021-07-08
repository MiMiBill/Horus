package com.example.horus.utils.kLog.okHttp;

import com.example.horus.utils.kLog.KLog;

/**
 * author: baiiu
 * date: on 16/8/31 19:40
 * description:
 */
public class LogInterceptor implements HttpLoggingInterceptorM.Logger {

    public static String INTERCEPTOR_TAG_STR = "OkHttp";

    public LogInterceptor() {
    }

    public LogInterceptor(String tag) {
        INTERCEPTOR_TAG_STR = tag;
    }

    @Override public void log(String message, @KLog.LogType int type) {
        KLog.printLog(false, type, INTERCEPTOR_TAG_STR, message);
    }
}
