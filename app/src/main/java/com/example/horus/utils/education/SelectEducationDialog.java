package com.example.horus.utils.education;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.horus.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * des:
 * author: lognyun
 * date: 2018/10/26 15:28
 */
public class SelectEducationDialog extends Dialog {
    @BindView(R.id.img_cancel) ImageView mImgCancel;
    @BindView(R.id.rcv) RecyclerView mRcv;
    private String[] mContents;

    private Context mContext;
    private Unbinder mUnbinder;
    private CompositeDisposable mCompositeDisposable;
    private OnSelectEducationListener mOnSelectEducationListener;

    public SelectEducationDialog(@NonNull Context context, OnSelectEducationListener onSelectEducationListener) {
        super(context);
        mContents = context.getResources().getStringArray(R.array.personal_info_education_list);
        mContext = context;
        mOnSelectEducationListener = onSelectEducationListener;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initEvent();
    }


    private void initView() {
        View view = View
                .inflate(mContext, R.layout.dialog_select_education, null);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        mUnbinder = ButterKnife.bind(this, view);
        mRcv.setLayoutManager(new LinearLayoutManager(mContext));


        SelectEducationAdapter adapter = new SelectEducationAdapter(Arrays.asList(mContents));
        mRcv.setAdapter(adapter);
        mCompositeDisposable.add(adapter.getsSubject()
                .subscribe(i -> {
                    mOnSelectEducationListener.onSelectEduction(i, mContents[i]);
                    dismiss();
                }));


    }

    private void initEvent() {
        mImgCancel.setOnClickListener(v -> dismiss());
    }


    @Override public void dismiss() {
        super.dismiss();
        if (mUnbinder != null)
            mUnbinder.unbind();
        mCompositeDisposable.dispose();
    }

    public interface OnSelectEducationListener {
        void onSelectEduction(int position, String name);
    }
}
