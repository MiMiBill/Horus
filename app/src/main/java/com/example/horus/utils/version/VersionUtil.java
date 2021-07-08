package com.example.horus.utils.version;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.baselib.utils.LogUtil;


/**
 * Created by Administrator on 2016/4/28 0028.
 * 用于版本显示与检测更新等
 */
public class VersionUtil {
    private static final String TAG = "VersionUtil";

    // 将版本号分隔开
    public static int[] versionToArray(String version) {
        try {
            String[] verStr = version.split("\\.",4);
            int[] ver = new int[3];
            ver[0] = Integer.valueOf(verStr[0]);
            ver[1] = Integer.valueOf(verStr[1]);
            ver[2] = Integer.valueOf(verStr[2]);
            return ver;
        } catch (Exception e) {
            LogUtil.e(TAG, "Method versionToArray error version");
            return new int[]{1,0,0};
        }
    }

    // 比对版本号大小
    public static int compareVersion(String oldVer, String newVer) {
        int oldVerArr[] = versionToArray(oldVer);
        int newVerArr[] = versionToArray(newVer);
        for (int i = 0; i < oldVerArr.length; i++) {
            if (newVerArr[i] > oldVerArr[i]) {
                return 1;
            } else if (newVerArr[i] < oldVerArr[i]) {
                return -1;
            }
        }
        return 0;
    }

    // 版本名
    public static String getVersionName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi != null) {
            return pi.versionName;
        }
        return "1.0.0";
    }

    // 版本号
    public static int getVersionCode(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (pi != null) {
            return pi.versionCode;
        }
        return 100;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
