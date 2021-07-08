package com.example.horus.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;

import java.io.File;
import java.util.UUID;


/**
 * des: 取IMEI号
 * author: lognyun
 * date: 2018/11/7 19:51
 */
public class IMEIUtils {
    private static String sIMEI;

    /**
     * 如果拿不到IMEI就用别的字段
     */
    public static String getIMEI(Context context) {
         return "111111111111111111111";
    }


    /**
     * 如果拿不到IMEI就用别的字段
     */
    public String getIMEIBak(Context context) {
        if (!TextUtils.isEmpty(sIMEI)) {
            return sIMEI;
        }


        try {

            File file = Installation.getIMEIFile();

            if (file.exists()) {
                sIMEI = Installation.readInstallationFile(file);
            }

            if (TextUtils.isEmpty(sIMEI)) {
                //实例化TelephonyManager对象
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                //获取IMEI号
                @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
                //在次做个验证，也不是什么时候都能获取到的啊
                if (imei == null) {
                    imei = "";
                }
                sIMEI = imei;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(TextUtils.isEmpty(sIMEI)){
                sIMEI=getUUID();
            }

            if (TextUtils.isEmpty(sIMEI)) {
                sIMEI = getAndroidId(context);
            }
            if (TextUtils.isEmpty(sIMEI)) {
                sIMEI = getInstallationId();

            }
            File file = Installation.getIMEIFile();


            if (!file.exists()) {//保存到文件里面
                file.createNewFile();

                if (!TextUtils.isEmpty(sIMEI)) {
                    Installation.writeInstallationFile(file, sIMEI);
                } else {
                    sIMEI = Installation.writeInstallationFile(file);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        LogUtil.e("IMEIUtils", "IMEI  " + sIMEI);
        if (TextUtils.isEmpty(sIMEI)) {
            sIMEI = "";
        }
        return sIMEI;
    }

    private static String getAndroidId(Context context) {
        String id = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if ("9774d56d682e549c".equals(id)) {
            return "";
        }
        return id;
    }

    private static String getInstallationId() {
        return Installation.id();
    }


    /**
     * https://juejin.im/post/5cad5b7ce51d456e5a0728b0
     *
     * 设备唯一标识符
     */
    @SuppressLint("MissingPermission") public static String getUUID() {

        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
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
