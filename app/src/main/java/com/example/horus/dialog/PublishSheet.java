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
 * Created by Liao on 2018/9/21 16:51:29
 *
 * 发布内容选框
 */
public class PublishSheet extends CustomDialog {
    public static final int FROM_CAMERA = 1;
    public static final int FROM_ALBUM = 2;

    private TextView tvCancel;

    private TextView tvType;// 如果已确定视频还是相册，就隐藏

    private TextView tvAlbum;

    private View viewCamera;

    private boolean hasImage = false;

    private OnPublishSelectListener mOnPublishSelectListener;

    public interface OnPublishSelectListener {
        void onPublishSelection(int type);
    }

    public void setOnPublishSelectListener(OnPublishSelectListener onPublishSelectListener) {
        mOnPublishSelectListener = onPublishSelectListener;
    }

    public void setHasImage(boolean has) {
        hasImage = has;
    }

    public PublishSheet(Activity activity, String tag) {
        super(activity, tag);
    }

    private void initView(View view) {
        viewCamera = view.findViewById(R.id.ll_dialog_publish_camera);
//        viewCamera.setOnClickListener(this);
        bindOnClickListener(viewCamera);

        tvAlbum = view.findViewById(R.id.tv_dialog_publish_album);
//        tvAlbum.setOnClickListener(this);
        bindOnClickListener(tvAlbum);

        tvCancel = view.findViewById(R.id.tv_dialog_publish_cancel);
//        tvCancel.setOnClickListener(this);
        bindOnClickListener(tvCancel);

        tvType = view.findViewById(R.id.tv_dialog_publish_media_type);
        showType(!hasImage);
    }

    @Override
    protected View createView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_publish, parent, false);
        initView(view);
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
        if (view == tvAlbum) {
            if (mOnPublishSelectListener != null) {
                mOnPublishSelectListener.onPublishSelection(FROM_ALBUM);
            }
        } else if (view == viewCamera) {
            if (mOnPublishSelectListener != null) {
                mOnPublishSelectListener.onPublishSelection(FROM_CAMERA);
            }
        }
    }

    private void showType(boolean show) {
        if (show) {
            tvType.setVisibility(View.VISIBLE);
        } else {
            tvType.setVisibility(View.GONE);
        }
    }
}
