package com.example.baselib.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.UUID;

/**
 * 判断应用是否在前台
 */
public class SysUtil {

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    public static boolean isAppForeground(Context context) {
        try {
            String packageName = context.getPackageName();
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> appProcesses = manager.getRunningAppProcesses();
            if (appProcesses != null && !appProcesses.isEmpty()) {
                for (RunningAppProcessInfo appProcess : appProcesses) {
                    if (appProcess.processName.equals(packageName)
                            && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // 判断应用是否存活
    public static boolean isAppAlive(Context context) {
        //
        if (context == null) {
            return false;
        }
        String packageName = context.getPackageName();

        ActivityManager manager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            try {
                List<RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
                if (processInfos != null && !processInfos.isEmpty()) {
                    for (RunningAppProcessInfo info : processInfos) {
                        if (packageName.equals(info.processName)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +

                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +

                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +

                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +

                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +

                Build.TAGS.length()%10 + Build.TYPE.length()%10 +

                Build.USER.length()%10 ; //13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

}
