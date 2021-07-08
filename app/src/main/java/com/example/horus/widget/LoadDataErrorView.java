package com.example.horus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.horus.R;


/**
 * des: 接口请求失败
 * 展示
 */
public class LoadDataErrorView extends LinearLayout {

    private OnClickListener mReloadListener;

    private View mViewReload;
    private View mViewCheckNet;

    public void setReloadListener(OnClickListener reloadListener) {
        mReloadListener = reloadListener;
        if (mViewReload != null) {
            setOnReloadListener();
        }
    }

    public LoadDataErrorView(Context context) {
        this(context, null);
    }

    public LoadDataErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadDataErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_load_data_error, this, true);
        mViewReload = findViewById(R.id.lyt_reload);
        mViewCheckNet = findViewById(R.id.txt_check_net);
        if (mReloadListener != null) {
            setOnReloadListener();
        }

        mViewCheckNet.setVisibility(VISIBLE);
        setBackgroundResource(R.color.white);

    }


    /**
     * 重新加载的点击事件
     */
    public void setOnReloadListener() {
        mViewReload.setOnClickListener(v -> mReloadListener.onClick(v));
    }

    public void setOnReloadListener(OnClickListener onReloadListener) {
        mReloadListener = onReloadListener;
        setOnReloadListener();
    }

}
