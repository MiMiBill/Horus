package com.example.horus.utils;

import android.graphics.Rect;
import android.os.Build;
import android.util.LayoutDirection;
import android.widget.TextView;

import java.util.Locale;

import androidx.core.text.TextUtilsCompat;


public class RTLUtils {


    /**
     * 是否是从右到左
     */
    public static boolean isRight2Left() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL;
        }
        return false;
    }


    /**
     * text view set bounds
     */
    public static void setCompoundDrawablesWithIntrinsicBounds(TextView textView, int start, int top, int end, int bottom) {
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom);

    }


    public static void setRectStart(Rect rect, int start) {
        setRect(rect, start, null);
    }

    public static void setRectEnd(Rect rect, int end) {
        setRect(rect, null, end);
    }


    /**
     * 设置 rect
     */
    public static void setRect(Rect rect, Integer start, Integer end) {
        if (isRight2Left()) {
            if (start != null)
                rect.right = start;
            if (end != null)
                rect.left = end;

        } else {
            if (start != null)
                rect.left = start;
            if (end != null)
                rect.right = end;
        }
    }
}
