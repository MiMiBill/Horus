package com.example.horus.utils;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * des:
 * author: lognyun
 * date: 2019/2/27 14:07
 */
public class NestedScrollUtils {

    public static void setBehavior(View view, CoordinatorLayout.Behavior behavior){
        CoordinatorLayout.LayoutParams paramsAvatar=
                (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        paramsAvatar.setBehavior(behavior);
    }
}
