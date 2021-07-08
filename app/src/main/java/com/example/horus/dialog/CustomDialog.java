package com.example.horus.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.view.WindowCallbackWrapper;

/**
 * Created by Liao on 2018/9/21 16:01:58
 *
 * 自定义窗口
 */
public abstract class CustomDialog {

    protected static String TAG = CustomDialog.class.getSimpleName();

    protected Activity mActivity;

    protected FrameLayout mRootView;
    protected RelativeLayout mBackView;// 放置背景
    protected RelativeLayout mMainView;// 放置内容
    protected RelativeLayout mMaskView;// 遮罩层
    protected View mContentView;// 内容

    private View mClickedView;// 当前点击内容

    private Window mWindow;
    private BackKeyWindowCallbackWrapper mCallbackWrapper;

    private Object data;// 有时需要传递数据在回调中使用
    private String mTag;

    private boolean isShowing;
    private boolean cancelable;
    private boolean cancelableTouchOutside;

    private int mShowDuration = 200;// ms
    private int mHideDuration = 100;// ms

    private int mBgColor = 0x4C000000;

    private int mNavHeight = 0;

    /**
     * 初始化控件
     * @param parent
     * @return
     */
    protected abstract View createView(ViewGroup parent);

    protected abstract RelativeLayout.LayoutParams addViewLayoutParams();

    protected abstract Animator showAnimator(View containerView);

    protected abstract Animator hideAnimator(View containerView);

    protected abstract void onClickAfterHideAnim(View view);

    public CustomDialog(Activity activity, String tag) {
        mActivity = activity;
        mTag = tag;
        TAG = getClass().getSimpleName();

        isShowing = false;
        cancelable = true;
        cancelableTouchOutside = true;
    }

    public CustomDialog setCancelable(boolean cancelable, boolean touchOutsideCancelable) {
        this.cancelable = cancelable;
        this.cancelableTouchOutside = touchOutsideCancelable;

        // cancelable优先级应当更高，如果设置不能取消，则外触取消属性失效
        if (!cancelable) {
            this.cancelableTouchOutside = false;
        }
        return this;
    }

    public CustomDialog setAnimDuration(int showDuration, int hideDuration) {
        this.mShowDuration = showDuration;
        this.mHideDuration = hideDuration;
        return this;
    }

    public CustomDialog setBackgroundColor(int color) {
        this.mBgColor = color;
        return this;
    }

    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 所有需要dismiss的点击事件都使用该方法进行绑定
     * 以保证窗口消失动画顺利执行
     * @param v
     */
    protected void bindOnClickListener(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedView = v;
                dismiss();
            }
        });
    }

    /**
     * 供外界调用
     */
    public void dismiss() {
        isShowing = false;
        if (mRootView != null && mMainView != null && mContentView != null) {
            hideAnim(mContentView);
        }
    }

    /**
     * 供外界调用
     */
    public void show() {
        closeKeyBoard();
        if (!initBase()) {
            return;
        }

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                showBase();
                // inflate会对之前的addView造成较高的延迟
                // 例如快速双击按钮时大概率弹出两个窗口
                View view = createView(mMainView);
                showView(view);
            }
        });
    }

    protected void show(View view) {
        showBase();
        showView(view);
    }

    protected void showBase() {
        mNavHeight = calculateNavHeight();

        int fullSize = ViewGroup.LayoutParams.MATCH_PARENT;
        FrameLayout.LayoutParams fullLayoutParams = new FrameLayout.LayoutParams(fullSize, fullSize);
        fullLayoutParams.setMargins(0, 0, 0, mNavHeight);

        mRootView.addView(mBackView, fullLayoutParams);
        mRootView.addView(mMainView, fullLayoutParams);
        mRootView.addView(mMaskView, fullLayoutParams);

        isShowing = true;
    }

    protected void showView(View view) {
        if (view == null) {
            Log.e(TAG, "ContainerView is null, dialog show error.");
            return;
        }
        mContentView = view;
        RelativeLayout.LayoutParams lp = addViewLayoutParams();
        if (lp == null) {
            int wrap = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp = new RelativeLayout.LayoutParams(wrap, wrap);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        }
        mMainView.addView(mContentView, lp);
        showAnim(mContentView);
    }

    private boolean initBase() {
        if (mActivity == null || mActivity.isFinishing()) {
            Log.e(TAG, "Activity is null or finishing, dialog show error.");
            return false;
        }

        // 这个回调是为了拦截Activity的onBackPressed()以获取返回键的处理权
        if (mWindow == null) {
            mWindow = mActivity.getWindow();
            mCallbackWrapper = new BackKeyWindowCallbackWrapper(mWindow.getCallback());
            mWindow.setCallback(mCallbackWrapper);
        }

        if (mRootView == null) {
            mRootView = (FrameLayout) mWindow.getDecorView().findViewById(android.R.id.content);
        }

        if (mActivity == null || mActivity.isFinishing()) {
            Log.e(TAG, "Activity is null or finishing, dialog show error.");
            return false;
        }

        // 添加背景
        if (mBackView == null) {
            mBackView = new RelativeLayout(mActivity);
            mBackView.setBackgroundColor(mBgColor);
            mBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelableTouchOutside) {
                        dismiss();
                    }
                }
            });
        }

        // 添加内容层
        if (mMainView == null) {
            mMainView = new RelativeLayout(mActivity);
        }

        // 添加遮罩层
        if (mMaskView == null) {
            mMaskView = new RelativeLayout(mActivity);
            mMaskView.setClickable(true);
        }

        mBackView.setVisibility(View.INVISIBLE);

        return !isShowing;
    }

    protected void showViewStart() {
        mBackView.setVisibility(View.VISIBLE);
    }

    protected void hideViewStart() {
    }

    protected void showViewEnd() {
        mMaskView.setVisibility(View.INVISIBLE);
        mCallbackWrapper.setCancelable(cancelable);
    }

    protected void hideViewEnd() {
        if (mClickedView != null) {
            onClickAfterHideAnim(mClickedView);
        }
        mClickedView = null;
        mRootView.removeView(mMainView);
        mRootView.removeView(mBackView);
        mRootView.removeView(mMaskView);
        isShowing = false;
    }

    protected void showAnim(View view) {
        mMaskView.setVisibility(View.VISIBLE);
        // 请求焦点可避免按 Home 后返回前台时 EditText 会触发软键盘
        mMainView.setFocusableInTouchMode(true);
        mMainView.setFocusable(true);
        mMainView.requestFocus();

        Animator viewAnim = showAnimator(view);
        if (viewAnim == null) {
            viewAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        }

        ObjectAnimator bgAnim = ObjectAnimator.ofFloat(mBackView, "alpha", 0f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.play(bgAnim).with(viewAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                showViewStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                showViewEnd();
            }
        });
        set.setDuration(mShowDuration).start();
    }

    protected void hideAnim(View view) {
        mMaskView.setVisibility(View.VISIBLE);

        Animator viewAnim = hideAnimator(view);
        if (viewAnim == null) {
            viewAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        }

        ObjectAnimator bgAnim = ObjectAnimator.ofFloat(mBackView, "alpha", 1f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.play(bgAnim).with(viewAnim);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                hideViewStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideViewEnd();
            }
        });
        set.setDuration(mHideDuration).start();
    }

    // 关闭软键盘
    protected void closeKeyBoard(){
        if (mActivity == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null && imm.isActive() && mActivity.getCurrentFocus()!=null) {
            if (mActivity.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 重写按键分发事件的窗口回调修饰类
     */
    private class BackKeyWindowCallbackWrapper extends WindowCallbackWrapper {

        private boolean cancelable = true;

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        @SuppressLint("RestrictedApi")
        public BackKeyWindowCallbackWrapper(Window.Callback wrapped) {
            super(wrapped);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (isShowing) {
                if (cancelable) {
                    dismiss();
                }
                return true;
            } else {
                return super.dispatchKeyEvent(event);
            }
        }
    }


    /**
     * 计算导航栏高度
     * @return
     */
    private int calculateNavHeight() {

        int navHeight = 0;

        // 根视图
        if (mActivity != null) {
//            navHeight = NavigationBarUtil.getNavBarHeight(mActivity);
        }

        return navHeight;
    }

}
