package com.example.horus.mvp;

import androidx.lifecycle.LifecycleOwner;

import com.example.horus.rx.RxCommon;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.example.horus.app.MyApp;
import com.example.horus.retrofit.RetrofitManager;
import com.example.horus.retrofit.RetrofitService;
import com.example.horus.rx.RxCommon;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;

/**
 * 封装的一个p层
 */
public class WrapMvpBasePresenter<V extends IBaseView> extends MvpBasePresenter<V> {

    protected RetrofitService mApi;
    protected LifecycleOwner mProvider;

    private List<MvpBasePresenter> mMvpBasePresenters;

    private CompositeDisposable mCompositeDisposable;

    public WrapMvpBasePresenter(LifecycleOwner provider) {
        this(RetrofitManager.getService(), provider);
    }

    public WrapMvpBasePresenter(RetrofitService api, LifecycleOwner provider) {
        mApi = api;
        mProvider = provider;
        mMvpBasePresenters = new ArrayList<>();
        mCompositeDisposable = new CompositeDisposable();
    }


    protected void addRequestPresenter(MvpBasePresenter mvpBasePresenter) {
        mMvpBasePresenters.add(mvpBasePresenter);
    }

    protected void addRequestPresenter(MvpBasePresenter... mvpBasePresenter) {
        for (MvpBasePresenter item : mvpBasePresenter) {
            mMvpBasePresenters.add(item);
        }
    }


    @Override
    public void attachView(V view) {
        super.attachView(view);
        for (MvpBasePresenter item : mMvpBasePresenters) {
            item.attachView(view);
        }

    }

    @Override
    public void detachView() {
        super.detachView();
        for (MvpBasePresenter item : mMvpBasePresenters) {
            item.detachView();
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }


    /**
     * 加载中和结束之后的状态改变
     */
    public <T> ObservableTransformer<T, T> processLoadAndComplete() {
        return tObservable -> tObservable
                .doOnSubscribe(disposable ->
                        ifViewAttached(IBaseView::showProgressView))
                .doFinally(() -> {
                    ifViewAttached(IBaseView::hideProgressView);
                });
    }

    /**
     * 加载中和结束之后的状态改变
     */
    public <T> ObservableTransformer<T, T> processLoadAndCompleteAndLife() {
        return tObservable -> tObservable
                .compose(RxCommon.rxCommonWithLifeCycle(mProvider))
                .doOnSubscribe(disposable ->
                        ifViewAttached(IBaseView::showProgressView))
                .doFinally(() -> {
                    ifViewAttached(IBaseView::hideProgressView);
                });
    }


    protected void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
        Request request = new Request.Builder()
                .url("http://www.123.com")
                .get()
                .addHeader("keep-alive", "true")
                .build();

    }


    /**
     * 阿波罗的baseUrl改变了
     * 这里需要跟着刷新
     */
    public void refreshService() {
        mApi=RetrofitManager.getService();
    }
}
