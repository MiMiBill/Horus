package com.example.horus.retrofit;

import android.text.TextUtils;

import com.example.horus.BuildConfig;
import com.example.horus.app.Constant;
import com.example.horus.app.MyApp;
import com.example.horus.data.UserInfo;
import com.example.horus.utils.converterfactory.StringConverterFactory;


import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;


public class RetrofitManager {
    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    public static final String READ_TIMEOUT = "READ_TIMEOUT";
    public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";
    private RetrofitService mService;

    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitService getService() {
        return getInstance().mService;
    }

    private static String BASE_URL = BuildConfig.DEBUG ? Constant.BASE_URL_DEBUG : Constant.BASE_URL_RELEASE;

    /**
     * 切换base url
     */
    public static RetrofitService newService() {
        SingletonHolder.INSTANCE.mService = createRetrofitService(BASE_URL);
        return getInstance().mService;
    }


    private static RetrofitService createRetrofitService(String baseUrl) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //非Debug模式,不允许代理
        if (!BuildConfig.DEBUG) {
            builder.proxy(Proxy.NO_PROXY);
        }

//        if (UMShareManager.isGooglePlayMarket()){
//            okHttpClient = builder
//                    .readTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
//                    .connectTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
//                    .writeTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
////                .addInterceptor(loggingInterceptor)
//                    .addNetworkInterceptor(loggingInterceptor)  //可打印Headers信息
//                    .addInterceptor(timeoutInterceptor)
//                    .addInterceptor(sTokenInterceptor)
//                    .sslSocketFactory(
//                            SSLContextUtil.getSSLContext().getSocketFactory(),
//                            SSLContextUtil.getX509TrustManager())
//                    .hostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER)
//                    .build();
//        }else {
            okHttpClient = builder
                    .readTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
                    .connectTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
                    .writeTimeout(BuildConfig.DEBUG ? Constant.TIME_OUT_SECOND_DEBUG : Constant.TIME_OUT_SECOND, TimeUnit.SECONDS)
//                .addInterceptor(loggingInterceptor)
                    .addNetworkInterceptor(loggingInterceptor)  //可打印Headers信息
                    .addInterceptor(timeoutInterceptor)
                    .addInterceptor(sTokenInterceptor)
                    .build();
//        }


        Retrofit mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();

        return mRetrofit.create(RetrofitService.class);
    }

    /**
     * 构造方法私有
     */
    private RetrofitManager() {
        // 结合KLog的日志拦截器
//        HttpLoggingInterceptorM loggingInterceptor = new HttpLoggingInterceptorM(new LogInterceptor());
//        loggingInterceptor.setLevel(HttpLoggingInterceptorM.Level.BODY);
        // 默认的日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mService = createRetrofitService(BASE_URL);
    }

    //https://www.jianshu.com/p/436cb79eace5?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
    private static final Interceptor sTokenInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            UserInfo userInfo = MyApp.getInstance().getUserInfo();
            final Request request = chain.request().newBuilder()
                    .addHeader("ut", TextUtils.isEmpty(MyApp.getInstance().getToken()) ? "" : MyApp.getInstance().getToken())
                    .addHeader("system", "1")
                    .addHeader("talkId", userInfo == null ? "" : userInfo.getUserId())
                    .build();
            return chain.proceed(request);
        }
    };

    static Interceptor timeoutInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            int connectTimeout = chain.connectTimeoutMillis();
            int readTimeout = chain.readTimeoutMillis();
            int writeTimeout = chain.writeTimeoutMillis();

            String connectNew = request.header(CONNECT_TIMEOUT);
            String readNew = request.header(READ_TIMEOUT);
            String writeNew = request.header(WRITE_TIMEOUT);

            if (!TextUtils.isEmpty(connectNew)) {
                connectTimeout = Integer.valueOf(connectNew);
            }
            if (!TextUtils.isEmpty(readNew)) {
                readTimeout = Integer.valueOf(readNew);
            }
            if (!TextUtils.isEmpty(writeNew)) {
                writeTimeout = Integer.valueOf(writeNew);
            }

            Request.Builder builder = request.newBuilder();
            builder.removeHeader(CONNECT_TIMEOUT);
            builder.removeHeader(READ_TIMEOUT);
            builder.removeHeader(WRITE_TIMEOUT);

            return chain
                    .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .proceed(builder.build());
        }
    };
}
