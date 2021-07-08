package com.example.horus.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Method;


public class NavigationBarUtil {


    //获取虚拟按键的高度
    public static int getNavigationBottomBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }




    /**
     * 在布局填充完成后调用
     */
    public static int getNavBarHeight(Activity activity) {
        int height = 0;

        try {

            if (isMiuiOpenFullScreenGesture(activity)) {
                return 0;
            }

            Window window = activity.getWindow();

            // FIXME 应该判断出导航栏具体方位（上下左右都应计算margin）
            View root = window.getDecorView();

            Point point = new Point();
            window.getWindowManager().getDefaultDisplay().getSize(point);// 小米MIX2/3上会空出导航栏高

//            mRootView.getWindowVisibleDisplayFrame(rect);// 会受软键盘影响

//            int realHeight = mRootView.getHeight();// 和getRealMetrics得到值相同

//            View contentView = mRootView.findViewById(android.R.id.content);// 受软键盘影响


            height = root.getHeight() - point.y;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return height < 0 ? 0 : height;
    }

    /**
     * 使用沉浸模式 需要在resume中设置
     *
     * 弹窗抢占焦点会导致沉浸模式退出 如果界面用到弹窗需特殊处理
     * 参考 VideoPayDialog.show() 方法前后的处理
     * 或 https://www.pocketdigi.com/20180704/1617.html
     *
     * 在华为6.0系统测试机上会报 requestCall() 异常
     *
     * @param view decorView
     */
    public static void useImmersiveStickyMode(View view) {
        int oldFlags = view.getSystemUiVisibility();
        // 两个layout标签作用应该是让内容延申到其下方，但使用IMMERSIVE_STICKY后体会不到
        int newFlags = oldFlags | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            newFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        view.setSystemUiVisibility(newFlags);
    }


    /**
     * 判断小米是否开启全面屏手势
     * 小米在开启全面屏手势时仍能查询到导航栏高度 所以要特殊处理
     */
    public static boolean isMiuiOpenFullScreenGesture(Context context) {
//        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

        if (RomUtil.isMiui()) {
            if (Settings.Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0) {
                return true;
            }
        }

        return false;
    }
}
