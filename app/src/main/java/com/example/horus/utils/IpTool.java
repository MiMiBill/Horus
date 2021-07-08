package com.example.horus.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.baselib.utils.LogUtil;
import com.example.horus.app.Constant;
import com.example.horus.app.MyApp;
import com.example.horus.retrofit.RetrofitManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lognyun on 2019/5/28 14:38:46
 * <p>
 * 切换翻译方案时 需要校验IP是否是中国
 * <p>
 * 由于查询接口来源于某宝IP库 这个库有访问限制 而且经常返回失败/长耗时
 * 所以需要对某次成功的结果做持久化处理 同时降低请求频率以提高成功率
 */
public class IpTool {

    private static final long CHECK_DURATION = 60 * 60 * 1000L;// 每小时查询一次即可

    private static final String KEY_IN_MAINLAND = "Key_InMainland";// 是否在国内
    private static final String KEY_IP = "Key_IP"; //ip地址

    private static final String KEY_LAST_SUCCESS_TIME = "Key_LastSuccessTime";// 上一次请求成功时间

    private static boolean isInMainland;
    private static String mCurrentIP; //当前网络地址

    public static boolean isInMainland() {
        return isInMainland;
    }

    public static String getCurrentIP() {
        return mCurrentIP;
    }

    private static void saveInMainland(boolean isInMainland) {
        SPUtil.put(MyApp.getInstance(), KEY_IN_MAINLAND, isInMainland);
        SPUtil.put(MyApp.getInstance(), KEY_LAST_SUCCESS_TIME, System.currentTimeMillis());
    }

    private static void saveIP(String ip) {
        SPUtil.put(MyApp.getInstance(), KEY_IP, ip);
        SPUtil.put(MyApp.getInstance(), KEY_LAST_SUCCESS_TIME, System.currentTimeMillis());
    }


    private static boolean loadInMainland() {
        Boolean ret = (Boolean) SPUtil.get(MyApp.getInstance(), KEY_IN_MAINLAND,
                SPUtil.SHARE_BOOLEAN);
        return ret == null ? false : ret;
    }

    public static String loadIP() {
        return (String) SPUtil.get(MyApp.getInstance(), KEY_IP, "");
    }

    private static long loadLastSuccessTime() {
        Long ret = (Long) SPUtil.get(MyApp.getInstance(), KEY_LAST_SUCCESS_TIME, SPUtil.SHARE_LONG);
        return ret == null ? 0L : ret;
    }


    /**
     * 用淘宝ip库查地址
     * 这个接口不知道间隔多久可以成功一次（总是返回1）
     * subscribe方法必须实现 onError 否则请求失败会报 OnErrorNotImplementedException
     */
    public static void checkIp() {

        isInMainland = loadInMainland();
        mCurrentIP = loadIP();

        if (System.currentTimeMillis() - loadLastSuccessTime() > CHECK_DURATION) {

//            Disposable disposable = RetrofitManager.getService().checkIpLocale()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            IpTool::parseIP,
//                            throwable -> {
//                            }
//                    );
//            getIPWithAli();
//            getIPWithSohu();
        }
    }


    private static void parseIP(String response) {
        try {
            JSONObject o = JSONObject.parseObject(response);
            String code = o.getString("code");

            if ("0".equals(code)) {
                JSONObject data = o.getJSONObject("data");
                String country = data.getString("country");
                String ip = data.getString("ip");

                isInMainland = "中国".equals(country);
                saveInMainland(isInMainland);
                saveIP(ip);
                LogUtil.d("淘宝--ip:" + ip + "; country:" + country);

                if (isInMainland) {
                    LogUtil.e("IpTool", "Your IP is in Mainland.");
                }
            } else {
                LogUtil.e("IpTool", "CheckIp ret:" + code);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


//    /**
//     * 绑定自己的ip
//     * 同时查两个 只要有一个可以用就行
//     */
//    @SuppressLint("CheckResult")
//    public static void bindIP(String talkID,String country,String systemNum) {
////        getSubscribe(talkID, country, systemNum, getIPWithAli());
//        getSubscribe(talkID, country, systemNum ,getIPWithSohu());
//    }
//
//    @NotNull
//    private static Disposable getSubscribe(String talkID,String country,String systemNum, Observable<String> ipWithSohu) {
//        return ipWithSohu
//                .filter(ip -> !TextUtils.isEmpty(ip))
//                .subscribeOn(Schedulers.computation())
//                .observeOn(Schedulers.computation())
//                .flatMap(ip -> RetrofitManager.getService().setUserSystem(ReleaseUtils.getChannel(), talkID,
//                        Constant.SYSTEM_TYPE_ANDROID, AppCountInfoManage.getVersionName(), ip, RegionUtil.getTimeZoneId(), country,systemNum,android.os.Build.MODEL,android.os.Build.VERSION.RELEASE))
//                .subscribe(result -> LogUtil.d("绑定用户信息成功"),
//                        error -> LogUtil.d("绑定用户信息 失败"));
//    }


//    private static Observable<String> getIPWithAli() {
//        return RetrofitManager.getService()
//                .checkIpLocale()
//                .subscribeOn(Schedulers.computation())
//                .map(result -> {
//                    try {
//                        JSONObject o = JSONObject.parseObject(result);
//                        String code = o.getString("code");
//
//                        if ("0".equals(code)) {
//                            JSONObject data = o.getJSONObject("data");
//                            String ip = data.getString("ip");
//                            if (!TextUtils.isEmpty(ip)){
//                                saveIP(ip);
//                                mCurrentIP = ip;
//                            }
//                            return ip;
//                        } else {
//                            LogUtil.e("IpTool", "CheckIp ret:" + code);
//                        }
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//                    }
//                    return "";
//                });
//    }
//
//    private static Observable<String> getIPWithSohu() {
//        return RetrofitManager.getService()
//                .checkIpLocaleSohu()
//                .subscribeOn(Schedulers.computation())
//                .map(result -> {
//                    int start = result.indexOf("{");
//                    int end = result.indexOf("}");
//                    String json = result.substring(start, end + 1);
//                    if (!TextUtils.isEmpty(json)) {
//                        try {
//                            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
//                            String ip = jsonObject.getString("cip");
//                            if (!TextUtils.isEmpty(ip)){
//                                saveIP(ip);
//                                mCurrentIP = ip;
//                            }
//                            return ip;
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return "";
//                });
//    }

    /**
     * 将字符串表示的ip地址转换为long表示.
     *
     * @param ip ip地址
     * @return 以32位整数表示的ip地址
     */
    public static final long ip2Long(final String ip) {
//        if (!RegexpUtils.isExactlyMatches("(\\d{1,3}\\.){3}\\d{1,3}", ip)) {
//            throw new IllegalArgumentException("[" + ip + "]不是有效的ip地址");
//        }
        //默认为有效的ip地址
        if (TextUtils.isEmpty(ip))return 0;
        final String[] ipNums = ip.split("\\.");
        return (Long.parseLong(ipNums[0]) << 24)
                + (Long.parseLong(ipNums[1]) << 16)
                + (Long.parseLong(ipNums[2]) << 8)
                + (Long.parseLong(ipNums[3]));
    }

    /**
     * 判断某ip是否在ip地址段内
     */
    public static boolean isBetweenIPs(String ip1, String ip2, String mineIp) {
        long num1 = ip2Long(ip1);
        long num2 = ip2Long(ip2);
        long mineNum = ip2Long(mineIp);

        if (num1 >= num2) {
            return mineNum >= num2 && mineNum <= num1;
        } else {
            return mineNum >= num1 && mineNum <= num2;
        }
    }
}
