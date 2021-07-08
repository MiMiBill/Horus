package com.example.horus.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import com.example.baselib.utils.LogUtil;
import com.example.horus.BuildConfig;


/**
 * Created by lognyun on 2019/5/29 10:33:42
 *
 * 获取设备信息
 */
public class DeviceUtils {



    public static void testDeviceInfo() {
        LogUtil.e("DeviceUtils", "---------- Display: " +Build.DISPLAY);// 版本号 Flyme 6.3.0.3A / VKY-AL00 9.0.1.179(C00E65R1P12)
        LogUtil.e("DeviceUtils", "---------- Product: " +Build.PRODUCT);// 产品名称 M3s / VKY-AL00
        LogUtil.e("DeviceUtils", "----------- Device: " +Build.DEVICE);// 设备驱动名称？ M3s / HWVKY
        LogUtil.e("DeviceUtils", "------------ Board: " +Build.BOARD);// 设备基板名称 M3s / VKY
        LogUtil.e("DeviceUtils", "----- Manufacturer: " +Build.MANUFACTURER);//设备制造商 Meizu / HUAWEI
        LogUtil.e("DeviceUtils", "------------ Brand: " +Build.BRAND);//设备品牌 Meizu / HUAWEI
        LogUtil.e("DeviceUtils", "------------ Model: " +Build.MODEL);// 手机型号 M3s / VKY-AL00

        LogUtil.e("DeviceUtils", "------ Android Ver: " +Build.VERSION.RELEASE);
    }







    /**
     * 获取设备机型（制造商不一定代表品牌）
     * @return 品牌 + 型号
     */
    public static String getDeviceModel() {

        return Build.BRAND + " " + Build.MODEL;
    }







    /**
     * Android 系统版本
     * @return 4.2 / 5.1 / 7.0 / 9 等
     */
    public static String getAndroidVersion() {

        return Build.VERSION.RELEASE;
    }


    /**
     * 系统版本号
     * @return 例如 Flyme 6.3.0.3A / VKY-AL00 9.0.1.179(C00E65R1P12) 等
     */
    public static String getSystemVersion() {

        return Build.DISPLAY;
    }

    public static void turnScreenOn(Activity activity) {
        // 下面这段代码用于来电亮屏（除小米外似乎都不需要权限（反正有铃声应该没太大问题））
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            KeyguardManager km = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
            km.requestDismissKeyguard(activity, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                activity.setShowWhenLocked(true);
                activity.setTurnScreenOn(true);
            } else {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            }
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }
}
