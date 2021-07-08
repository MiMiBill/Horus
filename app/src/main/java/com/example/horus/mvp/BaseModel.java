package com.example.horus.mvp;

import com.example.baselib.utils.LogUtil;
import com.example.horus.retrofit.BaseResponse;



public class BaseModel<T> {

    private OnRequestListener<T> mOnRequestListener;

    public interface OnRequestListener<T> {

        void onRequestSucceed(String tag, T t);

        void onRequestFailed(String tag, int code, String errorMsg);
    }

    public BaseModel() {
    }

    public BaseModel(OnRequestListener<T> onRequestListener) {
        mOnRequestListener = onRequestListener;
    }

    protected void requestSucceed(String tag, T t) {
        if (mOnRequestListener != null) {
            mOnRequestListener.onRequestSucceed(tag, t);
        }
    }

    protected void requestFailed(String tag, int code, String errorMsg) {
        LogUtil.e(tag, errorMsg);
        if (mOnRequestListener != null) {
            mOnRequestListener.onRequestFailed(tag, code, errorMsg);
        }
    }

    protected void requestDataNull(String tag) {
        if (mOnRequestListener != null) {
            mOnRequestListener.onRequestFailed(tag, -1, "Error: data is null");
        }
    }

    protected void requestSucceed(String tag, BaseResponse<T> response) {
        if (response.getCode() == RespCode.SUCCESS) {
            requestSucceed(tag, response.getData());
        } else {
            requestFailed(tag, response.getCode(), response.getInfo());
        }
    }

    protected void requestFailed(String tag, Throwable throwable) {
        requestFailed(tag, -1, throwable.getLocalizedMessage());
    }

}
