package com.example.baselib.utils;

import android.util.Log;

import com.example.baselib.BuildConfig;

import java.lang.reflect.Field;




/**
 * Log信息
 */
public class LogUtil {

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    private static final int MAX_LENGTH = 4 * 1024;

    public static void d(String msg) {
        d("-->>", msg);
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            if (msg.length() > 4000) {
                for (int i = 0; i < msg.length(); i += MAX_LENGTH) {
                    if (i + MAX_LENGTH < msg.length()) {
                        Log.e("-->>", msg.substring(i, i + MAX_LENGTH));
                    } else {
                        Log.e("-->>", msg.substring(i, msg.length()));
                    }
                }
            } else {
                Log.e("-->>", msg);
            }
        }
    }

    public static String getFields(Object object) {
        if (object == null) {
            return "Null in LogUtil.getFields()";
        }
        String result = "-------- " + object.getClass().getName() + " --------\n";

        Class<?> c = null;
        try {
            c = Class.forName(object.getClass().getName());
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                // 设为可访问
                f.setAccessible(true);
                String field = f.toString().substring(f.toString().lastIndexOf(".") + 1);
                result += field + ":  " + f.get(object) + "\n";
            }
            result += "-------- finish --------\n";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            result += "-------- class not found --------\n";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            result += "-------- illegal access exception --------\n";
        }

        return result;
    }
}
