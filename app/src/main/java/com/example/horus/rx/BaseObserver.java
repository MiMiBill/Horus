package com.example.horus.rx;

import com.example.baselib.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lognyun on 2018/8/15 14:24:32
 *
 * 使用RespObserver
 */
@Deprecated
public class BaseObserver<T> implements Observer<T> {

    // 这个Disposable到底怎么用，一直没看到比较好的讲解
    protected Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        onSucceed(t);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onComplete() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void onSucceed(T t) {
    }

    public void onFailed(int code, String message) {
        ToastUtil.showShortMessage(code + message);
    }

    public void onFinish() {
    }
}
