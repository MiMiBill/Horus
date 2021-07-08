package com.example.horus.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.horus.R;
import com.example.horus.app.MyApp;


public class CommonDialog extends Dialog {
    private TextView mTxtTitle;
    private FrameLayout mFytCustomer;
    private TextView mTxtConfirm;
    private TextView mTxtCancel;
    private TextView mDes;

    private Context mContext;
    private View mLineDivider;
    private Builder mBuilder;
    private View mLyt;
    private View mViewContainer;
    private TextView mTvINnow;
    private View btnTopLine;

    public void setBuilder(Builder builder) {
        mBuilder = builder;
    }

    public CommonDialog(@NonNull Context context) {
        super(context, R.style.SelectEducationDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        ViewGroup.LayoutParams p = getWindow().getAttributes();
        p.width = MyApp.getInstance().getResources().getDisplayMetrics().widthPixels;


    }

    private void initView() {
        mContext = getContext();

        View view = View
                .inflate(mContext, R.layout.dialog_common, null);
        setContentView(view);


        mFytCustomer = view.findViewById(R.id.fyt_customer);
        mViewContainer = view.findViewById(R.id.lyt_container);
        mTxtTitle = view.findViewById(R.id.txt_title);
        mTxtConfirm = view.findViewById(R.id.txt_confirm);
        mTxtCancel = view.findViewById(R.id.txt_cancel);
        mLineDivider = view.findViewById(R.id.line_divider);
        mLyt = view.findViewById(R.id.lyt_button_panel);
        mDes = view.findViewById(R.id.txt_des);
        mTvINnow = view.findViewById(R.id.tv_i_know);//知道了  用在警告页面
        btnTopLine = view.findViewById(R.id.btn_top_line);

    }


    private void initEvent() {
        setCancelable(mBuilder.cancelAble);
        setCanceledOnTouchOutside(mBuilder.cancelAble);

        mTxtCancel.setOnClickListener(v -> {
            if (mBuilder.negativeClickListener == null) {
                dismiss();
            } else {
                mBuilder.negativeClickListener.onClick(CommonDialog.this);
            }
        });


        mTvINnow.setOnClickListener(v -> {
            mBuilder.iKnowClickListener.onClick(CommonDialog.this);
        });

        if (mBuilder.positiveClickListener != null) {
            mTxtConfirm.setOnClickListener(v -> {
                mBuilder.positiveClickListener.onClick(CommonDialog.this);
            });
        }

        if (!TextUtils.isEmpty(mBuilder.title)) {
            mTxtTitle.setText(mBuilder.title);
        }

        if (!TextUtils.isEmpty(mBuilder.negativeText)) {
            mTxtCancel.setText(mBuilder.negativeText);
        } else if (mBuilder.positiveClickListener != null) {
            mTxtCancel.setVisibility(View.GONE);
            mLineDivider.setVisibility(View.GONE);

            mTxtConfirm.setTextColor(ContextCompat.getColor(MyApp.getInstance(),
                    R.color.system_message_unread));
        }

        if (!TextUtils.isEmpty(mBuilder.positiveText)) {
            mTxtConfirm.setText(mBuilder.positiveText);
        } else if (mBuilder.positiveClickListener == null) {
            mLyt.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(mBuilder.iKnowText)) {
            mTvINnow.setText(mBuilder.iKnowText);
        }

        if (mBuilder.iKnowClickListener != null) {
            mLyt.setVisibility(View.GONE);
            btnTopLine.setVisibility(View.GONE);
            mTvINnow.setVisibility(View.VISIBLE);
        }


        if (mBuilder.contentView != null) {
            mFytCustomer.removeAllViews();
            mBuilder.contentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mFytCustomer.addView(mBuilder.contentView);
        }





        mDes.setVisibility(TextUtils.isEmpty(mBuilder.des) ? View.GONE :View.VISIBLE);
        mDes.setText(mBuilder.des);
    }

    public TextView getNegativeButton() {
        return mTxtCancel;
    }

    public TextView getPositiveButton() {
        return mTxtConfirm;
    }


    public interface OnDialogOptionClickListener {
        void onClick(Dialog dialog);
    }


    public static class Builder {
        public String title;
        public View contentView;
        public String positiveText;
        public String negativeText;
        public String iKnowText;
        public String des;
        public OnDialogOptionClickListener positiveClickListener;
        public OnDialogOptionClickListener negativeClickListener;
        public OnDialogOptionClickListener iKnowClickListener;
        public Context context;
        public boolean cancelAble = true;


        public Builder(Context context) {
            this.context = context;
            negativeText = context.getString(R.string.cancel);
        }

        public Builder setPositiveButton(int text, OnDialogOptionClickListener listener) {
            setPositiveClickListener(listener);
            positiveText = context.getString(text);
            return this;
        }

        public Builder setIKnowButton(int text, OnDialogOptionClickListener listener) {
            setIKnowListener(listener);
            iKnowText = context.getString(text);
            return this;
        }

        public Builder setNegativeButton(int text, OnDialogOptionClickListener listener) {
            setNegativeListener(listener);
            negativeText = context.getString(text);
            return this;
        }

        public Builder setDes(int desIdx){
            des = context.getString(desIdx);
            return this;
        }

        public Builder setPositiveButton(String text, OnDialogOptionClickListener listener) {
            setPositiveClickListener(listener);
            positiveText = text;

            return this;
        }

        public Builder setCancelAble(boolean cancelAble) {
            this.cancelAble = cancelAble;
            return this;
        }

        public Builder setNegativeButton(String text, OnDialogOptionClickListener listener) {
            setNegativeListener(listener);
            negativeText = text;
            return this;
        }

        public Builder setPositiveClickListener(OnDialogOptionClickListener listener) {
            positiveClickListener = listener;
            return this;
        }

        public Builder setNegativeListener(OnDialogOptionClickListener listener) {
            negativeClickListener = listener;
            return this;
        }

        public Builder setIKnowListener(OnDialogOptionClickListener listener) {
            iKnowClickListener = listener;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public CommonDialog create() {
            CommonDialog dialog = new CommonDialog(context);
            dialog.setBuilder(this);
            return dialog;
        }

        public void show() {
            create().show();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mBuilder.contentView = null;
    }


    public void setDialogBackgroundDrawable(int drawable) {
        mViewContainer.setBackground(mContext.getResources().getDrawable(drawable));
    }
}
