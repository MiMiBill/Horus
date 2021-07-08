package com.example.horus.rx;

import com.example.baselib.utils.ToastUtil;
import com.example.horus.BuildConfig;
import com.example.horus.app.MyApp;
import com.example.horus.retrofit.BaseResponse;
import com.example.horus.retrofit.RespCode;
import com.example.horus.ui.EnterActivityUtils;
import com.example.horus.utils.BuglyUtils;
import com.example.horus.utils.NetUtil;
import com.example.horus.utils.UserUtil;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;


/**
 * <p>
 * 普通请求覆写onSucceed()即可
 * 列表请求或需指定加载失败状态的，需覆写onFailed()
 */
public class RespObserver<T> extends DisposableObserver<BaseResponse<T>> {

    // 这个Disposable到底怎么用，一直没看到比较好的讲解
    protected Disposable mDisposable;

    @Override
    public void onNext(BaseResponse<T> response) {
        parseResp(response);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            BuglyUtils.postCatchedException(e);
        }
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        int code = RespCode.NET_ERROR;
        if (e instanceof HttpException) {
            code = ((HttpException) e).code();
        } else if (e instanceof ConnectException) {
            code = RespCode.NET_DISABLE;// 请确保网络可用
            requestApolloConfig();
        } else if (e instanceof SocketTimeoutException) {
            code = RespCode.NET_TIMEOUT;// 超时
            requestApolloConfig();
        } else if (e instanceof UnknownHostException && !NetUtil.isNetworkConnected(MyApp.getInstance())){ //网络断开，无法解析主机
            code = RespCode.NET_DISABLE;
        }

        onFailed(code, e.getMessage());
        onFinish();
    }

    /**
     * 配置中心
     */
    private void requestApolloConfig() {
//        ApolloModel model = new ApolloModel();
//        model.updateConfigs();
    }


    @Override
    public void onComplete() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    private void parseResp(BaseResponse<T> response) {
        if (response == null) {
            onFailed(RespCode.RESP_NULL, "BaseResponse is null.");
        } else {
            if (response.getCode() == RespCode.SUCCESS) {
                onSucceedProcess(response);
            } else {
                onFailed(response.getCode(), "ErrorCode:" + response.getCode());
                //图形验证码
                onFailedWithData(response.getCode(), "ErrorCode:" + response.getCode(),
                        response.getData());
            }
        }
    }

    public void onFinish() {
        // 备用
    }

    public void onSucceedProcess(BaseResponse<T> response) {
        onSucceed(response.getData());
    }

    public void onSucceed(T t) {
    }

    public void onFailed(int code, String message) {
        defaultProcessFailed(code, message);
    }

    public void onFailedWithData(int code, String message, T t) {
    }

    public static void defaultProcessFailed(BaseResponse response) {
        defaultProcessFailed(response.getCode(), "ErrorCode:" + response.getCode());
    }

    public static void defaultProcessFailed(int code, String message) {
        if (code == -3 || code == 1)//有一些特殊的code，不需要显示Toast
            return;

        String errorMsg = RespCode.getErrorMessage(code, message);

        //errorMsg没变说明不是服务器升级提示语，不需要显示长时间的Toast
        if (errorMsg.equals(checkServerPrompt(errorMsg, code))){
            ToastUtil.showShortMessage(errorMsg);
        }else {
            errorMsg = checkServerPrompt(errorMsg, code);
            ToastUtil.showLongMessage(errorMsg);
        }

        if (code == -2){ //说明token已经过期了
            UserUtil.clearUserInfo(true);
//            ImUtils.logoutIM();
//            IMMenuListUnreadUtils.reset();
            EnterActivityUtils.toNextStep(MyApp.getInstance());
        }
    }

    /**
     * 若服务器异常，则提示服务器升级提示语
     */
    private static String checkServerPrompt(String msg, int code) {
        //java.net.ConnectException且此时有网络连接 则判断服务器异常
        if (code == RespCode.NET_DISABLE && NetUtil.isNetworkConnected(MyApp.getInstance())){
//            String languageCode = RegionUtil.getDefaultLanguage();
//            for (ServerPrompt serverPrompt : ApolloConfig.serverPromptList){
//
//                if (serverPrompt.language.equals(languageCode)){
//                    return serverPrompt.content;
//                }
//            }
        }

        return msg;
    }
}
