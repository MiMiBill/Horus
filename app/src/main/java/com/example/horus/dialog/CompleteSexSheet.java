package com.example.horus.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.horus.R;


/**
 * 完善个人信息 - 选择性别
 */
public class CompleteSexSheet extends CustomDialog {

    private TextView tvMale;
    private TextView tvFemale;

    private OnSexSelectListener mOnSexSelectListener;

    public CompleteSexSheet(Activity activity) {
        super(activity, "sex");
    }

    @Override
    protected View createView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_complete_sex, parent, false);
        tvMale = view.findViewById(R.id.tv_complete_sheet_male);
        tvFemale = view.findViewById(R.id.tv_complete_sheet_female);
        bindOnClickListener(tvMale);
        bindOnClickListener(tvFemale);
        return view;
    }

    @Override
    protected RelativeLayout.LayoutParams addViewLayoutParams() {
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        return layoutParams;
    }

    @Override
    protected Animator showAnimator(View containerView) {
        return ObjectAnimator.ofFloat(containerView, "translationY", containerView.getHeight(), 0);
    }

    @Override
    protected Animator hideAnimator(View containerView) {
        return ObjectAnimator.ofFloat(containerView, "translationY", 0, containerView.getHeight());
    }

    @Override
    protected void onClickAfterHideAnim(View view) {
        if (mOnSexSelectListener == null) {
            return;
        }
        if (view == tvMale) {
            mOnSexSelectListener.onSexSelected(1);
        } else if (view == tvFemale) {
            mOnSexSelectListener.onSexSelected(0);
        }
    }

    public void setOnSexSelectListener(OnSexSelectListener onSexSelectListener) {
        mOnSexSelectListener = onSexSelectListener;
    }

    public interface OnSexSelectListener {
        /**
         * 0女 1男
         * @param sex 性别代码
         */
        void onSexSelected(int sex);
    }
}
