package com.example.horus.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.example.horus.BuildConfig;
import com.example.horus.mvp.WrapMvpBasePresenter;
import com.example.horus.ui.SplashActivity;
import com.example.horus.ui.main.MainActivity;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;



import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 *
 */

public class ActivityUtils {

    private static Stack<Activity> activityStack;

    /**
     * 添加Activity 到栈
     */
    public static synchronized void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
       Activity activity1 = currentActivity();
        if (activity1 != null && TextUtils.equals(activity1.getClass().getName(),activity.getClass().getName()) && !(activity1 instanceof MainActivity)){
            activity1.finish();
        }
        activityStack.add(activity);
    }


    /**
     * 需要在当前界面 onDestroy的时候 从栈内移除
     */
    public static synchronized void removeActivity(Activity activity) {
        if (activityStack != null) {
            if (activityStack.contains(activity)) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    public static Activity currentActivity() {
        if(activityStack==null){
            return null;
        }

        if(activityStack.empty()){
            return null;
        }

        return activityStack.lastElement();
    }


    /**
     * 结束所有的Activity
     */
    public static void finishAllActivity() {
        if(activityStack==null){
            return;
        }

        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }



    /**
     * 结束所有的Activity
     */
    public static MainActivity finishAllActivityWithOutMainActivity() {
        MainActivity mainActivity = null;
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                if(activityStack.get(i) instanceof MainActivity){
                    mainActivity= (MainActivity) activityStack.get(i);
                    continue;
                }
                activityStack.get(i).finish();
            }
        }
        return mainActivity;
    }

    /**
     * 通过类名销毁Activity
     * @param classes
     */
    public static void finishActivityWithClass(Class ...classes) {

        if(activityStack!=null){
            int size = activityStack.size();
            if(size>0){
                for (int i = size-1; i >0 ; i--) {
                    if (null != activityStack.get(i)) {
                        for(Class item:classes){
                            if (activityStack.get(i).getClass().equals(item)) {
                                activityStack.get(i).finish();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static Activity getActivity(Class<?> clazz){
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(clazz)) {
                    return activity;
                }
            }

        }

        return null;
    }


    public static int getActivityStackIndex(Activity activity){
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.search(activity) - 1;
        }

        return -1;
    }

    public static Activity getIndexActivityStack(int index){
        if (activityStack == null)return null;
        int size = activityStack.size();
        if (activityStack != null && size > 0 && index <= size - 1) {
            return activityStack.get(index);
        }
        return null;
    }

    public static Activity getLast2ActivityStack(){
        if (activityStack == null)return null;
        int size = activityStack.size();
        int last2Index = size -1 -1;
        if (activityStack != null && size > 0 && last2Index >= 0) {
            return activityStack.get(last2Index);
        }
        return null;
    }




    /**
     * 是否存在某个Activity
     */
    public static synchronized boolean containsActivity(Class<?> clazz) {
        if (activityStack != null && activityStack.size() > 0) {
            Iterator<Activity> iterator = activityStack.iterator();
            while (iterator.hasNext()){
                Activity activity = iterator.next();
                if (activity.getClass().equals(clazz)) {
                    return true;
                }
            }
        }

        return false;


    }

    public static Activity getActivityFromView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 判断应用是否已经启动
     *
     * @param context     一个context
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void luanchCurrentApp(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
//            //前提：知道要跳转应用的包名、类名
            ComponentName componentName = new ComponentName(BuildConfig.APPLICATION_ID,
                    SplashActivity.class.getName());
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
    }


    /**
     * 刷新所有p层的
     */
    public static void refreshPresenter() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (activity instanceof MvpActivity){
                    MvpActivity mvpActivity= (MvpActivity) activity;
                    MvpPresenter mvpPresenter= mvpActivity.getPresenter();
                    if(mvpPresenter instanceof WrapMvpBasePresenter){
                        WrapMvpBasePresenter wrapMvpBasePresenter= (WrapMvpBasePresenter) mvpPresenter;
                        wrapMvpBasePresenter.refreshService();
                    }

                }
            }

        }

    }
}
