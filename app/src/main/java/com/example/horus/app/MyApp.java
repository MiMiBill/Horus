package com.example.horus.app;

import android.app.Activity;
import android.app.Application;
import android.os.Build;

import com.example.horus.R;
import com.example.horus.data.UserInfo;
import com.example.horus.ui.BaseActivity;
import com.example.horus.utils.ActivityUtils;

public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();

    private static MyApp sInstance;
    private static int sStatusBarHeight = -1;
    private static int mToolBarHeight;
    private static int mStatusBarHeight;

    private String userId;
    private UserInfo userInfo;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApp getInstance(){
        return sInstance;
    }


    /**
     * 从系统属性里获取状态栏的高度
     *
     * @return
     */
    public static int getStatusBarBgHeight() {

        if (sStatusBarHeight == -1) {
            int result = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int resourceId = sInstance.getResources().getIdentifier("status_bar_height",
                        "dimen", "android");
                if (resourceId > 0) {
                    result = sInstance.getResources().getDimensionPixelSize(resourceId);
                }
            }
            sStatusBarHeight = result;
        }
        return sStatusBarHeight;
    }

    /**
     * 展示 系统通知的pop
     */
    public void showSystemNoticePop(String content, int infoType) {

        Activity activity = ActivityUtils.currentActivity();
        if (activity == null) {
            return;
        }

        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;

            if (mToolBarHeight == 0) {
                mToolBarHeight = activity.getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
                int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    mStatusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
                }

            }
            baseActivity.showSystemMessage(content, infoType, mToolBarHeight + sStatusBarHeight);
        }


    }
}
