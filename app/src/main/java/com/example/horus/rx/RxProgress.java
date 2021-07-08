package com.example.horus.rx;

import android.app.Activity;

import androidx.annotation.NonNull;


import com.example.horus.widget.ProgressView;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 *
 * 参考自：https://blog.csdn.net/qq_20521573/article/details/70991850
 * 使用：.compose(ProgressUtils.< T >applyProgressBar(this))
 */
public class RxProgress {

    public static <T> ObservableTransformer<T, T> apply(@NonNull Activity activity, String msg) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);

        ProgressView progressView = new ProgressView();
        progressView.show(activityWeakReference.get());

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Activity act = activityWeakReference.get();
                        if (act != null && !act.isFinishing()) {
                            progressView.hide();
                        }
                    }
                }).doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // ?
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> apply(@NonNull Activity activity) {
        return apply(activity, "loading...");
    }

}
