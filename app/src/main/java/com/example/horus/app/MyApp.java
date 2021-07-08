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
     * 初始化自己的类或者数据
     */
    private void initData() {
        Config.init(this);
        ToastUtil.init(this);
        Config.setHasCleanSP(true); //已清除过数据
        //App从打开开始到在前台的时间的统计
        unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    /**
     * 第三方sdk初始化
     */
    public void init3SDK(){
        IpTool.checkIp();
        AppCrashUtils.init();// 在Bugly之前调用
        initGSYVideoPlay();
        initBugly();

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                if (throwable == null)return;
                //异常处理
                //https://www.jianshu.com/p/436cb79eace5?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
                BuglyUtils.postCatchedException(new RxjavaNoCatcahException(throwable));
                throwable.printStackTrace();
                LogUtil.e("RxjavaNoCatcahException",throwable.getMessage());
            }
        });

    }


    /**
     * 异常上报
     * <p>
     * 非Debug 且主进程
     */
    private void initBugly() {
        if (BuglyUtils.OPEN_REPORT) {
            Context context = getApplicationContext();
            // 获取当前包名
            String packageName = context.getPackageName();
            // 获取当前进程名
            String processName = getProcessName(android.os.Process.myPid());
            // 设置是否为上报进程
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            CrashReport.initCrashReport(this, Constant.BUGLY_APP_ID, false);
        }
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
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
            //内存不足,被杀死,直接重启app
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
            LogUtil.d("lifecircle","onActivityDestroyed：" + activity);
        }
    };

    /**
     * 数据恢复
     */
    public int getPID() {
        return (int) SPUtil.get(this, "PID", -1);
    }

    public static MyApp getInstance(){
        return sInstance;
    }

    /**
     * 关闭ijK的log
     */
    private void initGSYVideoPlay() {
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);
        //代理缓存模式，支持所有模式，不支持m3u8等，默认
        CacheFactory.setCacheManager(ProxyCacheManager.class);
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
