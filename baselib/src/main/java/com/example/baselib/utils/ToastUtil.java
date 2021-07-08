package com.example.baselib.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    private static final String TAG = ToastUtil.class.getSimpleName();

    private static Context mContext;
    private static Handler handler = new Handler(Looper.getMainLooper());

    /** Toast对象 */
    private static Toast toast = null ;
    private  static  int mScreenHeight;

    //在application中调用init方法进行初始化，传入全局context
    public final static void init(Context context){
        mContext = context;
        mScreenHeight=mContext.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 显示Toast（单例）
     * duration为0或1，不能作为时间间隔判断依据
     * @param context
     * @param message
     * @param duration
     */
    private static void showToast(Context context, String message, int duration) {
        // 如果应用不在前台不显示toast
        if (!SysUtil.isAppForeground(mContext)) {
            return;
        }

        if (message == null) {
            LogUtil.e(TAG, "Message is null");
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context, message, duration);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    toast.setText(message);
                    toast.setDuration(duration);
                    toast.show();
                }
            }
        });
    }

//    public static void showShortMessageCenter(Context context,int messageId)
//    {
//        // 如果应用不在前台不显示toast
//        if (!SysUtil.isAppForeground(mContext)) {
//            return;
//        }
//        String message = context.getString(messageId);
//        if (message == null) {
//            LogUtil.e(TAG, "Message is null");
//            return;
//        }
//
//        if (toast == null) {
//            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, mScreenHeight / 2);
//            toast.show();
//        } else {
//            toast.setText(message);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    private static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    //短时间显示
    public static void showShortMessage(String message){
        showToast(mContext, message, Toast.LENGTH_SHORT);
    }

    public static void showShortMessage(int message) {
        showToast(mContext, message, Toast.LENGTH_SHORT);
    }

    //长时间显示
    public static  void showLongMessage(String message){
        showToast(mContext, message, Toast.LENGTH_LONG);
    }

    public static  void showLongMessage(int message){
        showToast(mContext, message, Toast.LENGTH_LONG);
    }

}