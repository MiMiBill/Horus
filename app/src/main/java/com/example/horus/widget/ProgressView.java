package com.example.horus.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.horus.R;


public class ProgressView {

    public ProgressDialog mProgressDialog;
    private boolean mCancelable=true;

    private DialogInterface.OnCancelListener mOnCancelListener;


    public ProgressView() {
    }


    /**
     * 用于沉浸模式
     * @param activity
     */
    public void showImmersiveSticky(Activity activity) {
        // 先关闭软键盘否则会出现dialog下落的效果
        closeKeyBorad(activity);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity, R.style.LoadingDialog);
        }
        if (mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.getWindow().setWindowAnimations(R.style.LoadingDialogAnim);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(mCancelable);

        mProgressDialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        // NavigationBarUtil.useImmersiveStickyMode(mProgressDialog.getWindow().getDecorView());
        try {
            mProgressDialog.show();
            mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            mProgressDialog.setContentView(R.layout.progress_dialog);

            mProgressDialog.setOnCancelListener(mOnCancelListener);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void show(Activity activity) {
        // 先关闭软键盘否则会出现dialog下落的效果
        closeKeyBorad(activity);

        //activity 结束了则直接退出
        if(activity.isDestroyed()){
           return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity, R.style.LoadingDialog);
        }
        if (mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog.getWindow().setWindowAnimations(R.style.LoadingDialogAnim);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(mCancelable);
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);

        mProgressDialog.setOnCancelListener(mOnCancelListener);
    }

    public void hide() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // 关闭软键盘
    private void closeKeyBorad(Activity activity){
        if (activity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() && activity.getCurrentFocus()!=null) {
            if (activity.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 谷歌支付的时候,任务不能同时执行两个
     * 要不关闭dialog
     */
    public void setCancelAble(boolean cancelable){
        mCancelable=cancelable;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        mOnCancelListener = listener;
    }
}
