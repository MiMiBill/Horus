package com.example.horus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.horus.R;
import com.example.horus.utils.RTLUtils;


/**
 * <p>
 * 空数据页面
 */
public class EmptyContentView extends FrameLayout {

    private TextView tvMsg;
    private Button button;
    private ImageView mImageView;

    public EmptyContentView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyContentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_empty_content, this, true);
        mImageView = findViewById(R.id.iv_layout_empty_content);

        if (RTLUtils.isRight2Left()) {
            ConstraintLayout.LayoutParams layoutParams =
                    (ConstraintLayout.LayoutParams) mImageView.getLayoutParams();
            layoutParams.horizontalBias = 0.35f;
        }

        setBackgroundResource(R.color.white);
    }

    public void setMessage(int msgStrId) {
        tvMsg = findViewById(R.id.tv_layout_empty_content);
        tvMsg.setVisibility(VISIBLE);
        tvMsg.setText(msgStrId);
    }

    public void setMessage(String msg) {
        tvMsg = findViewById(R.id.tv_layout_empty_content);
        tvMsg.setVisibility(VISIBLE);
        tvMsg.setText(msg);
    }

    public void setImageRes(@DrawableRes int res) {
        mImageView.setImageResource(res);
    }

    /**
     * 按钮设置点击事件
     *
     * @param btnTextStrId 按钮文字
     * @param listener     点击事件
     */
    public void setButtonListener(int btnTextStrId, OnEmptyButtonClickListener listener) {
        setButtonListener(btnTextStrId, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEmptyClick(v);
                }
            }
        });
    }

    /**
     * 如果调用该方法设置按钮
     * 需要使用对应id设置监听 R.id.btn_layout_empty_content
     * 建议使用 OnEmptyButtonClickListener
     *
     * @param btnTextStrId    按钮文字
     * @param onClickListener 点击事件
     */
    public void setButtonListener(int btnTextStrId, OnClickListener onClickListener) {
        button = findViewById(R.id.btn_layout_empty_content);
        button.setVisibility(VISIBLE);
        button.setText(btnTextStrId);
        button.setOnClickListener(onClickListener);
    }

    public interface OnEmptyButtonClickListener {
        void onEmptyClick(View view);
    }
}
