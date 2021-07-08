package com.example.horus.mvp;

import io.reactivex.disposables.Disposable;


public class SimplePresenter extends BasePresenter<SimpleView> {

    public SimplePresenter(SimpleView view) {
        super(view);
    }

    public void addSubscribe(Disposable disposable) {
        super.addSubscribe(disposable);
    }
}
