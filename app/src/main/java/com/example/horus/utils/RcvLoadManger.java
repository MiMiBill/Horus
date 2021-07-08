package com.example.horus.utils;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.horus.widget.EmptyContentView;
import com.example.horus.widget.LoadDataErrorView;
import com.example.horus.widget.LoadDataFlyView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * des: RCV loading 状态管理
 */
public class RcvLoadManger {

    private EmptyContentView mEmptyContentView;
    private LoadDataErrorView mLoadDataErrorView;
    private LoadDataFlyView mLoadDataFlyView;

    private BaseQuickAdapter mBaseQuickAdapter;
    private View mViewNull;
    private Context mContext;

    private View.OnClickListener mOnReloadListener;

    private int mColorBGRes=-1;
    private int mEmptyRes=-1;

    public RcvLoadManger(BaseQuickAdapter baseQuickAdapter, Context context) {
        this(baseQuickAdapter, context, null);
    }

    public RcvLoadManger(BaseQuickAdapter baseQuickAdapter, Context context, View.OnClickListener onReloadListener) {
        mBaseQuickAdapter = baseQuickAdapter;
        mContext = context;
        mOnReloadListener = onReloadListener;
    }

    public void showLoading() {
        showLoading(false);
    }

    public void showLoading(boolean force) {
        initFlyView();

        mBaseQuickAdapter.setEmptyView(mLoadDataFlyView);

        if (force) {
            mBaseQuickAdapter.notifyDataSetChanged();
        }
    }

    private void initFlyView() {
        if (mLoadDataFlyView == null) {
            mLoadDataFlyView = new LoadDataFlyView(mContext);
            if(mColorBGRes!=-1){
                mLoadDataFlyView.setBackgroundResource(mColorBGRes);
            }
        }
    }

    public void showLoadError() {
        if (mLoadDataErrorView == null) {
            mLoadDataErrorView = new LoadDataErrorView(mContext);
            if(mColorBGRes!=-1){
                mLoadDataErrorView.setBackgroundResource(mColorBGRes);
            }
            if (mOnReloadListener != null) {
                mLoadDataErrorView.setReloadListener(v->{
                    showLoading(true);

                    mOnReloadListener.onClick(v);
                });
            }
        }

        mBaseQuickAdapter.setEmptyView(mLoadDataErrorView);
        mBaseQuickAdapter.notifyDataSetChanged();

    }

    public void showNull() {
        if (mViewNull == null) {
            mViewNull = new View(mContext);
        }
        mBaseQuickAdapter.setEmptyView(mViewNull);


    }

    public void showEmpty() {
        if (mEmptyContentView == null) {
            mEmptyContentView = new EmptyContentView(mContext);
        }

        if(mColorBGRes!=-1){
            mEmptyContentView.setBackgroundResource(mColorBGRes);
        }
        if(mEmptyRes!=-1){
            mEmptyContentView.setImageRes(mEmptyRes);
        }
        mBaseQuickAdapter.setEmptyView(mEmptyContentView);
        mBaseQuickAdapter.notifyDataSetChanged();

    }

    public void setOnReloadListener(View.OnClickListener onReloadListener) {
        mOnReloadListener = onReloadListener;

        if (mLoadDataErrorView != null) {
            mLoadDataErrorView.setReloadListener(v->{
                showLoading(true);
                mOnReloadListener.onClick(v);
            });
        }
    }

    public void setEmptyRes(@DrawableRes int res){
        mEmptyRes=res;
    }

    public void setColorBG(@ColorRes int colorBG) {
        mColorBGRes = colorBG;
    }

    public void setAnimationDrawable(int res){
        initFlyView();
        mLoadDataFlyView.setAnimationDrawable(res);
    }

}
