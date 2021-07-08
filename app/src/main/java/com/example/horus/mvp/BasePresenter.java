package com.example.horus.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class BasePresenter<V extends BaseView> {
    protected static String TAG;

    protected V mView;

    private CompositeDisposable mCompositeDisposable;

    public BasePresenter(V view) {
        mView = view;
        TAG = getClass().getSimpleName();
    }

    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    public void detach() {
        if (mView != null) {
            mView = null;
        }
        unSubscribe();
    }
}
