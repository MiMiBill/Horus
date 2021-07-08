package com.example.horus.utils;

import android.text.TextUtils;

import com.example.horus.R;
import com.example.horus.widget.ProgressView;

/**
 * @Author: longyun
 * @CreateDate: 2020/11/19 16:46
 */
public class ProgressViewUtil {


    private static String preActivityName;

    private static ProgressView mProgressView;// 解决加载圈退出沉浸模式的BUG
    public synchronized static void showProgressView() {
        try {
            String curActivityName = ActivityUtils.currentActivity().getClass().getName();
            if (mProgressView == null || !TextUtils.equals(curActivityName,preActivityName)) {
                mProgressView = new ProgressView();
                mProgressView.showImmersiveSticky(ActivityUtils.currentActivity());
            }
            preActivityName = curActivityName;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static synchronized void hideProgressView() {
        try {
            if (mProgressView != null){
                mProgressView.hide();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }





}
