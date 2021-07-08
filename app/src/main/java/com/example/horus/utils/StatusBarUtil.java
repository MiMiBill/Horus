package com.example.horus.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.horus.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

/**
 * <p>
 * 状态栏相关类
 */
public class StatusBarUtil {

    public static void setStatusBarFont(View view, boolean black) {
        Activity activity= ActivityUtils.getActivityFromView(view);
        if(activity==null){
            return;
        }
       setStatusBarFont(activity,black);
    }

    /**
     * 设置状态栏白底黑字
     */
    public static void setStatusBarFont(Activity view, boolean black) {
        Window window =view.getWindow();
        if (window!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    (black?View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR:View.SYSTEM_UI_FLAG_LAYOUT_STABLE));

            if (FlymeSetStatusBarLightMode(window, black)) {
                return;
            }
            MIUISetStatusBarLightMode(window, black);
        }
    }


        public static void setLightMode(Activity activity) {
        // 调整为暗色文字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                return;
            }
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        }
    }

    public static void setColorWhite(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(0xFFFFFFFF);

            // 调整为暗色文字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setColorRes(Activity activity, int colorId) {
        int color = activity.getResources().getColor(colorId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);

            if (ColorUtils.calculateLuminance(color) > 0.5) {

                //小于23 设置 黑体是无效的
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    //状态栏颜色改为,白加黑
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.status_bar_android_21_22));
                }

                setLightMode(activity);
            }
        }
    }


    /**
     * 魅族状态栏
     * 白底黑字
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 小米状态栏白底黑字
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    public static int mixTwoColors( int color1, int color2, float amount) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL   = 16;
        final byte GREEN_CHANNEL =  8;
        final byte BLUE_CHANNEL  =  0;

        final float inverseAmount = 1.0f - amount;

        int a = ((int)(((float)(color1 >> ALPHA_CHANNEL & 0xff )*amount) +
                ((float)(color2 >> ALPHA_CHANNEL & 0xff )*inverseAmount))) & 0xff;
        int r = ((int)(((float)(color1 >> RED_CHANNEL & 0xff )*amount) +
                ((float)(color2 >> RED_CHANNEL & 0xff )*inverseAmount))) & 0xff;
        int g = ((int)(((float)(color1 >> GREEN_CHANNEL & 0xff )*amount) +
                ((float)(color2 >> GREEN_CHANNEL & 0xff )*inverseAmount))) & 0xff;
        int b = ((int)(((float)(color1 & 0xff )*amount) +
                ((float)(color2 & 0xff )*inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
    }
}

