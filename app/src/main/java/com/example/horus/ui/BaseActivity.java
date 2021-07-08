package com.example.horus.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.BuildConfig;
import com.example.horus.R;
import com.example.horus.app.MyApp;
import com.example.horus.utils.NetUtil;
import com.example.horus.widget.ProgressView;
import com.example.horus.widget.dialog.CommonDialog;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class BaseActivity extends AppCompatActivity {

    protected static String TAG;

    //双击退出
    private long exitTime = 0;
    private boolean doubleBack = false;


    /**
     * 记录 progress 显示的次数
     * 每次show 会加+1 ,hide -1
     * 只有在 mShowProgressCount==1的时候,调隐藏才会隐藏
     * 都是在主线程里面调的show 和 hide ,应该不用处理多线程
     */
    private int mShowProgressCount;

    private ProgressView mProgressView;

    private CompositeDisposable mCompositeDisposable;

    private PopupWindow mSystemPop;

    private View grapChatContainer;

//    /**
//     * 设置语言
//     */
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(LanguageUtils.setLocal(newBase));
//
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        checkRevert();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unSubscribe();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            closeKeyboard();
        }
        return super.onTouchEvent(event);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                LogUtil.e("MotionEvent.ACTION_DOWN");
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            {
                LogUtil.e("MotionEvent.ACTION_UP");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 2s内再点一次退出Activity
     */
    @Override
    public void onBackPressed() {
        if (!doubleBack) {
            super.onBackPressed();
            return;
        }
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShortMessage(R.string.double_press_to_exit);
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 关闭当前Activity显示的软键盘
     */
    public void closeKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void doubleClickBack() {
        doubleBack = true;
    }

    public synchronized void showProgressView() {
        if (!isDestroyed()) {
            mShowProgressCount = mShowProgressCount + 1;

            if (mProgressView == null) {
                mProgressView = new ProgressView();
                mProgressView.setOnCancelListener(dialog -> onProgressViewCanceled());
            }
            mProgressView.show(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }



    public synchronized void hideProgressView() {
        mShowProgressCount = mShowProgressCount - 1;
        if (!isDestroyed()) {

            if (mProgressView != null) {
                mProgressView.hide();
            }
        }

    }

    public void setProgressViewCancelable(boolean cancelable) {
        if (mProgressView == null) {
            mProgressView = new ProgressView();
        }
        mProgressView.setCancelAble(cancelable);
    }

    /**
     * 子类重写即可处理加载框取消回调
     */
    public void onProgressViewCanceled() {

    }

    public void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }


    private void checkRevert() {
        if (!BuildConfig.DEBUG) {
//            checkProxy();
            checkDebug();
        }

    }

    /**
     * 检测是否使用了代理
     * 不允许抓包
     */
    private void checkProxy() {
        int tip = -1;

        if (NetUtil.isWifiProxy(this)) {
            tip = R.string.proxy_tip_wifi;

        } else if (NetUtil.checkPorxyHost()) {
            tip = R.string.proxy_tip;
        }


        if (tip == -1) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog.Builder(this)
                .setTitle(getString(tip))
                .setPositiveClickListener(dialog -> {
                    dialog.dismiss();
                    finish();
                }).create();
        commonDialog.setCancelable(false);
        commonDialog.show();
        commonDialog.getNegativeButton().setEnabled(false);


    }

    /**
     * 防止 动态调试
     */
    private void checkDebug() {
        if (!BuildConfig.DEBUG && 0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            CommonDialog commonDialog = new CommonDialog.Builder(this)
                    .setTitle(getString(R.string.revert_not_safe))
                    .setPositiveClickListener(dialog -> {
                        dialog.dismiss();
                        finish();
                    }).create();
            commonDialog.setCancelable(false);
            commonDialog.show();
            commonDialog.getNegativeButton().setEnabled(false);
        }

    }

    /**
     * 初始化状态栏，高度 背景
     *
     * @param view
     */
    public void initStatusBar(ImageView view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = MyApp.getStatusBarBgHeight();
        }
    }



    /**
     * 在当前Activity顶部显示系统消息弹窗
     *
     * @param content
     * @param height
     */
    public void showSystemMessage(String content, int infoType, int height) {
        TextView view = (TextView) View.inflate(this, R.layout.pop_system_notice, null);
        view.setText(content);
        if (mSystemPop != null && mSystemPop.isShowing()) {
            mSystemPop.dismiss();
        }

        mSystemPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);

        //当前Activity未销毁则显示系统消息PopupWindow
        if (!isFinishing()) {
            try {
                mSystemPop.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content),
                        Gravity.TOP, 0, height);

                //5s后弹窗消失
                new Handler().postDelayed(() -> {
                    if (!isFinishing() && mSystemPop.isShowing()) {
                        mSystemPop.dismiss();
                    }
                }, 5000);
            } catch (Exception e) {
                e.printStackTrace();
                mSystemPop.dismiss();
            }

        }
    }



}

