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
 * 聊天页设置项
 */
public class ChatSettingDialog extends CustomDialog {

    private TextView tvMale;
    private TextView tvFemale;

    private OnChatSettingListener mOnChatSettingListener;

    public ChatSettingDialog(Activity activity) {
        super(activity, "ChatSettingDialog");
    }

    @Override
    protected View createView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_chat_settings, parent, false);
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
        int action = 0;

        if (view == tvMale) {
            //
        } else if (view == tvFemale) {
            //
        }

        mOnChatSettingListener.onSettingSelected(action);
    }




    public void setOnChatSettingListener(OnChatSettingListener onChatSettingListener) {
        mOnChatSettingListener = onChatSettingListener;
    }

    public interface OnChatSettingListener {
        /**
         * @param action
         */
        void onSettingSelected(int action);
    }
}
