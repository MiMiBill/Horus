package com.example.horus.rx;

import android.app.Activity;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxCommon {

    public static <T> ObservableTransformer<T, T> rxCommon() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


//    /**
//     * string to BaseResponse<T>
//     */
//    public static <T> BaseResponse<T> convert2BaseResponseT(RxCacheResult result, Class<T> t) {
//        Type type = new ParameterizedTypeImpl(BaseResponse.class, new Class[]{t});
//        BaseResponse baseResponse=MyApp.getInstance().getGson().fromJson(result.getContent(), type);
//
//        if (baseResponse != null) {
//            baseResponse.setCache(result.isCache());
//            baseResponse.setNoEndLoading(result.isNoEndLoading());
//        } else {
//            // 好友列表出现过result.getContent为空的情况导致无法继续加载
//            LogUtil.e("BaseResponse 为空 - in convert2BaseResponseT() of RxCommon.class");
//            baseResponse = new BaseResponse();
//            baseResponse.setNoEndLoading(true);
//        }
//
//        return baseResponse;
//    }
//
//
//    /**
//     * string to BaseResponse<T>
//     */
//    public static <T> BaseResponse<T> convert2BaseResponseT(String input, Class<T> t) {
//        Type type = new ParameterizedTypeImpl(BaseResponse.class, new Class[]{t});
//        return MyApp.getInstance().getGson().fromJson(input, type);
//    }
//
//
//    /**
//     * string to BaseResponse<List<T>>
//     */
//    public static <T> BaseResponse<T> convert2BaseResponseListT(String input, Class<T> t) {
//        // 生成List<T> 中的 List<T>
//        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{t});
//        // 根据List<T>生成完整的Result<List<T>>
//        Type type = new ParameterizedTypeImpl(BaseResponse.class, new Type[]{listType});
//        return MyApp.getInstance().getGson().fromJson(input, type);
//    }

    /**
     * 切换线程并绑定生命周期
     * 在doFinally操作符里面的时候需要判断当前界面是否被销毁
     */
    public static <T> ObservableTransformer<T, T> rxCommonWithLifeCycle(LifecycleOwner owner) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(AndroidLifecycle.createLifecycleProvider(owner)
                                .bindUntilEvent(Lifecycle.Event.ON_DESTROY));
            }
        };
    }

    /**
     * 切换线程并绑定生命周期
     * 在doFinally操作符里面的时候需要判断当前界面是否被销毁
     */
    public static <T> ObservableTransformer<T, T> withLifeCycle(LifecycleOwner owner) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(AndroidLifecycle.createLifecycleProvider(owner)
                                .bindUntilEvent(Lifecycle.Event.ON_DESTROY));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> rxCommon(RxFragment rxFragment) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(rxFragment.bindToLifecycle())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 目前Activity没有和RxLifecycle绑定
     *
     * @param activity
     * @param showProgress
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxCommon(Activity activity, boolean showProgress) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(RxProgress.apply(activity))
//                        .compose(activity.bindToLifecycle()) // FIXME
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }



    public static <T> ObservableTransformer<T, T> throttleFirst() {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(Observable<T> upstream) {

                return upstream.throttleFirst(500, TimeUnit.MILLISECONDS);

            }
        };
    }


}
