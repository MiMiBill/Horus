package com.example.horus.utils.logcat;

import android.os.Build;

import com.example.horus.BuildConfig;
import com.example.horus.app.MyApp;
import com.example.horus.utils.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lognyun on 2019/6/17 19:20:35
 */
public class AppCrashUtils {

    private static final String LOG_FOLDER_NAME = "log";

    private static boolean open = BuildConfig.DEBUG;


    public static void init() {

        if (open) {

            // 检测缓存log数 避免文件堆积
            clearLogs();

            // 需要在Bugly之前调用
            Thread.setDefaultUncaughtExceptionHandler(new AppCrashHandler());

        }
    }


    public static void clearLogs() {
        File file = new File(getLogFilePath());
        if (file.exists() && file.isDirectory()) {
            String[] logs = file.list();
            if (logs != null && logs.length > 100) {
                file.delete();
            }
        }
    }



    /**
     * 生成文件名
     * @return fileName
     */
    public static String getLogFileName() {
        String fileName = ".txt";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault());
        String date = df.format(new Date());

        String key = MyApp.getInstance().getUserId();
        if (key == null || key.isEmpty()) {
            key = Build.BRAND + "_" + Build.MODEL;
        }

        return date + "_" + key + fileName;
    }


    /**
     * 生成文件路径
     * @return 文件目录
     */
    public static String getLogFilePath() {
        return FileUtil.getCacheFilePath(LOG_FOLDER_NAME);
    }

}
