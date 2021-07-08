package com.example.horus.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;


import com.example.horus.R;
import com.example.horus.utils.StatusBarUtil;

import io.reactivex.disposables.Disposable;

/**
 * <p>
 * 自定义的Toolbar控件
 */
public class ToolbarView extends RelativeLayout {
    private static final String TAG = ToolbarView.class.getSimpleName();

    private ImageView ivBack;
    private TextView tvTitle;
    /**
     * 右边的文字
     */
    private TextView tvRight;

    /**
     * 右边的图片
     */
    private ImageView ivRight;

    private FrameLayout mFytContainer;
    private Disposable mRightTextDisposable;


    public ToolbarView(Context context) {
        this(context, null);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar, this, true);

        ivBack = findViewById(R.id.iv_toolbar_back);
        tvTitle = findViewById(R.id.tv_toolbar_title);
        tvRight = findViewById(R.id.tv_toolbar_right);
        ivRight = findViewById(R.id.iv_toolbar_right);
        mFytContainer = findViewById(R.id.fyt_container);
    }

    public void init(Activity activity, int titleResId) {
        String title = titleResId == 0 ? null : getResources().getString(titleResId);
        init(activity, title, true, true, true);
    }

    public void init(Activity activity, int titleResId, boolean setStatusBar) {
        String title = titleResId == 0 ? null : getResources().getString(titleResId);
        init(activity, title, true, true, setStatusBar);
    }

    /**
     * 调用该方法进行初始化设置
     *
     * @param activity
     * @param title
     * @param withLine
     * @param canBack
     */
    public void init(final Activity activity, String title, boolean withLine, boolean canBack) {
        init(activity, title, withLine, canBack, true);
    }

    public void init(final Activity activity, String title, boolean withLine, boolean canBack,
                     boolean setStatusBar) {
        tvTitle.setText(title);

        if (!withLine) {
            setBackground(null);
        }

        if (canBack) {
            ivBack.setVisibility(VISIBLE);
            ivBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mLeftListener != null){
                        mLeftListener.onClick(view);

                    }else {
                        activity.onBackPressed();
                    }
                }
            });
        }

        if (setStatusBar)
            StatusBarUtil.setColorRes(activity, R.color.bg_toolbar);
    }


    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTitleTextColor(int resId) {
        tvTitle.setTextColor(resId);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * 设置返回键的图标
     */
    public void setBackIcon(@DrawableRes int res) {
        ivBack.setImageResource(res);
    }

    /**
     * 右边文字的颜色
     */
    public void setRightTextColor(int color) {
        tvRight.setTextColor(color);
    }

    /**
     * 右边的文字
     */
    public void setRightText(String text) {
        tvRight.setVisibility(VISIBLE);
        tvRight.setText(text);
    }

    public void setRightText(int resId) {
        tvRight.setText(resId);
    }

    private OnClickListener mLeftListener;

    //设置左边返回按键点击事件，可覆盖原来退出Activity事件
    public void setLeftClickListener(OnClickListener onClickListener){
        this.mLeftListener = onClickListener;
    }

    /**
     * 右边按钮的点击事件
     */
    public void setRightTextClickListener(OnClickListener onClickListener) {
        tvRight.setOnClickListener(onClickListener);
    }

    /**
     * 显示或隐藏右边的text
     */
    public void toggleRightTextVisible(boolean visible) {
        tvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /**
     * 右边的图标
     */
    public void setRightIcon(int res) {
        ivRight.setImageResource(res);
        ivRight.setVisibility(VISIBLE);
    }

    /**
     * 右边的图标
     */
    public void setRightVisibility(int visibility) {
        ivRight.setVisibility(visibility);
    }

    /**
     * 右边按钮的点击事件
     */
    public void setRightIconClickListener(OnClickListener onClickListener) {
        ivRight.setOnClickListener(onClickListener);
    }

    /**
     * 显示或隐藏右边的text
     */
    public void toggleRightIconVisible(boolean visible) {
        ivRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    public ImageView getIvRight() {
        return ivRight;
    }


    @Override
    public void setBackground(Drawable background) {
        mFytContainer.setBackground(background);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mRightTextDisposable != null){
            mRightTextDisposable.dispose();
        }
    }
}
