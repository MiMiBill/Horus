package com.example.horus.rx;

import com.example.horus.retrofit.BaseResponse;

/**
 * <p>
 * 普通请求覆写onSucceed()即可
 * 列表请求或需指定加载失败状态的，需覆写onFailed()
 */
public class RxCacheRespObserver<T> extends RespObserver<T> {


    @Override public void onSucceedProcess(BaseResponse<T> response) {
        onSucceed(response.getData(),response.isNoEndLoading());
    }

    /**
     * 产生结果
     *
     * @param noEndLoading 不停止 loading
     */
    public void onSucceed(T t, boolean noEndLoading) {

    }


    /**
     * 请使用
     * @see RxCacheRespObserver#onSucceed(Object, boolean)
     * 第一页且有网且刷新会返回两次
     */
    @Deprecated @Override public void onSucceed(T t) {
    }
}
