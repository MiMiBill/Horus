package com.example.horus.utils;

import com.example.horus.R;

import butterknife.OnClick;

/**
 * Created by lognyun on 2016/6/7 0007.
 *
 * 防止点击过快
 */
public class ClickUtil {
    private static final long DEFALT_DURATION = 500L;
    private static long lastClickTime;

    // 防止重复点击
    public static boolean cantClick() {
        return cantClick(DEFALT_DURATION);
    }

    public static boolean cantClick(long duration) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < duration) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public interface clickListener{
        void onSingleClick();
        void onDoubleClick();
    }


}
