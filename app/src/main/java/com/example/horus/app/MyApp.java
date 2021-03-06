package com.example.horus.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.data.UserInfo;
import com.example.horus.bugly.RxjavaNoCatcahException;
import com.example.horus.ui.BaseActivity;
import com.example.horus.utils.ActivityUtils;
import com.example.horus.utils.BuglyUtils;
import com.example.horus.utils.IpTool;
import com.example.horus.utils.SPUtil;
import com.example.horus.utils.logcat.AppCrashUtils;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();

    private static MyApp sInstance;
    private static int sStatusBarHeight = -1;
    private static int mToolBarHeight;
    private static int mStatusBarHeight;

    private String userId;
    private UserInfo userInfo;
    private String token;
    private int mActivityLifecycleCount;
    private boolean sForeground;

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
        initData();
        init3SDK();
    }

    /**
     * ?????????????????????????????????
     */
    private void initData() {
        Config.init(this);
        ToastUtil.init(this);
        Config.setHasCleanSP(true); //??????????????????
        //App?????????????????????????????????????????????
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    /**
     * ?????????sdk?????????
     */
    public void init3SDK(){
        IpTool.checkIp();
        AppCrashUtils.init();// ???Bugly????????????
        initGSYVideoPlay();
        initBugly();

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                if (throwable == null)return;
                //????????????
                //https://www.jianshu.com/p/436cb79eace5?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
                BuglyUtils.postCatchedException(new RxjavaNoCatcahException(throwable));
                throwable.printStackTrace();
                LogUtil.e("RxjavaNoCatcahException",throwable.getMessage());
            }
        });

    }


    /**
     * ????????????
     * <p>
     * ???Debug ????????????
     */
    private void initBugly() {
        if (BuglyUtils.OPEN_REPORT) {
            Context context = getApplicationContext();
            // ??????????????????
            String packageName = context.getPackageName();
            // ?????????????????????
            String processName = getProcessName(android.os.Process.myPid());
            // ???????????????????????????
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            CrashReport.initCrashReport(this, Constant.BUGLY_APP_ID, false);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param pid ?????????
     * @return ?????????
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtil.d("lifecircle","onActivityCreated" + ":" + activity.getClass().getSimpleName());
            ActivityUtils.addActivity(activity);
            //????????????,?????????,????????????app
            if (savedInstanceState != null) {
                int oldPid = getPID();
                if (oldPid != 0) {
                    Intent i =
                            getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext()
                                    .getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }

        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtil.d("lifecircle","onActivityStarted" + ":" + activity.getClass().getSimpleName());
            mActivityLifecycleCount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtil.d("lifecircle","onActivityResumed" + ":" + activity.getClass().getSimpleName());
            sForeground = true;
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtil.d("lifecircle","onActivityPaused" + ":" + activity.getClass().getSimpleName());
//                BuriedPointUtils.pushAppLiveUpdateEvent();
            sForeground = false;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtil.d("lifecircle","onActivityStopped" + ":" + activity.getClass().getSimpleName());
            mActivityLifecycleCount--;

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            ActivityUtils.removeActivity(activity);
            LogUtil.d("lifecircle","onActivityDestroyed???" + activity);
        }
    };

    /**
     * ????????????
     */
    public int getPID() {
        return (int) SPUtil.get(this, "PID", -1);
    }

    public static MyApp getInstance(){
        return sInstance;
    }

    /**
     * ??????ijK???log
     */
    private void initGSYVideoPlay() {
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
        //???????????????????????????????????????????????????m3u8????????????
        CacheFactory.setCacheManager(ProxyCacheManager.class);
    }

    /**
     * ??????????????????????????????????????????
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
     * ?????? ???????????????pop
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
