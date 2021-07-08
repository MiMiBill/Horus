package com.example.horus.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.example.baselib.utils.LogUtil;
import com.example.horus.BuildConfig;
import com.example.horus.app.Config;
import com.example.horus.app.MyApp;
import com.example.horus.data.UserInfo;

/**
 */
public class UserUtil {
    private static final String TAG = UserUtil.class.getSimpleName();

    public static final String SIZE_APPEND = "imageView2/0/w/";
    public static final String TOKEN_KEY_RONG="token";

    /**
     * 注册完成时只返回talkId和token，需要自行取参拼接
     * 腾讯云使用UserSign 融云使用token
     *
     * @param data 请求反参
     * @param userInfo 注册时传入的userInfo
     * @return 是否保存成功
     */
    public static boolean saveRegisteredUserInfo(JSONObject data, UserInfo userInfo) {
        if (userInfo == null) {
            LogUtil.e(TAG, "saveRegisteredUserInfo() error, userInfo is null.");
            return false;
        }
        //使用融云的 token
        String token = data.getString(TOKEN_KEY_RONG);
        String talkId = data.getString("talkId");
        if (TextUtils.isEmpty(talkId) || TextUtils.isEmpty(token)) {
            String msg = "Token:" + token + "  TalkId:" + talkId ;
            LogUtil.e(TAG, "saveRegisteredUserInfo() error, " + msg);
            return false;
        }

        userInfo.setUserId(talkId);
        // 保存
        MyApp.getInstance().setUserInfo(userInfo);
        MyApp.getInstance().setUserId(talkId);
        MyApp.getInstance().setToken(token);
        Config.saveUserInfo(userInfo);
        Config.setUserToken(token);
        return true;
    }

    /**
     * 修改密码后保存数据
     * @param data
     * @param withUserInfo
     * @return
     */
    public static boolean parseUserWithPwd(JSONObject data, boolean withUserInfo) {

        String token = data.getString(TOKEN_KEY_RONG);// 接口校验及融云IM

        if (!TextUtils.isEmpty(token)) {

            if (withUserInfo) {
                UserInfo userInfo = data.getObject("user", UserInfo.class);
                if (userInfo != null) {

                    String talkId = userInfo.getUserId();
                    if (talkId != null && !talkId.isEmpty()) {
                        MyApp.getInstance().setUserInfo(userInfo);
                        MyApp.getInstance().setToken(token);
                        MyApp.getInstance().setUserId(talkId);
                        Config.saveUserInfo(userInfo);
                        Config.setUserToken(token);
                        return true;
                    } else {
                        LogUtil.e(TAG, "parseUser() error, talkId is null or empty.");
                    }

                } else {

                    LogUtil.e(TAG, "parseUser() error, userInfo is null.");
                }
            } else {
                MyApp.getInstance().setToken(token);
                Config.setUserToken(token);
                return true;
            }

        } else {
            LogUtil.e(TAG, "parseUser() error, token is null or empty.");
        }

        return false;
    }



    public static boolean parseUser(JSONObject data, boolean withUserInfo) {

        String token = data.getString(TOKEN_KEY_RONG);// 接口校验及融云IM

        if (!TextUtils.isEmpty(token)) {

            if (withUserInfo) {
                UserInfo userInfo = data.getObject("user", UserInfo.class);
                if (userInfo != null) {

                    String talkId = userInfo.getUserId();
                    if (talkId != null && !talkId.isEmpty()) {
                        MyApp.getInstance().setUserInfo(userInfo);
                        MyApp.getInstance().setToken(token);
                        MyApp.getInstance().setUserId(talkId);
                        Config.saveUserInfo(userInfo);
                        Config.setUserToken(token);
                        return true;
                    } else {
                        LogUtil.e(TAG, "parseUser() error, talkId is null or empty.");
                    }

                } else {

                    LogUtil.e(TAG, "parseUser() error, userInfo is null.");
                }
            } else {
                MyApp.getInstance().setToken(token);
                Config.setUserToken(token);
                return true;
            }

        } else {
            LogUtil.e(TAG, "parseUser() error, token is null or empty.");
        }

        return false;
    }

    public static boolean thirdLoginIsRegister(JSONObject data) {
        // 判断是否注册 1注册 2登录
        String status = data.getString("status");
        return "1".equals(status);
    }

    public static boolean checkUserValid() {
        String talkId = MyApp.getInstance().getUserId();
        String token = MyApp.getInstance().getToken();
        if (!TextUtils.isEmpty(talkId)
                && !TextUtils.isEmpty(token)
                && MyApp.getInstance().getUserInfo() != null) {
            return true;
        }
        return false;
    }

    /**
     * 返回 true 表示一切信息正常
     * @return
     */
    public static boolean loadUser() {
        String token = Config.getUserToken();
        UserInfo userInfo = Config.getUserInfo();
        if (TextUtils.isEmpty(token)
                || userInfo == null
                || TextUtils.isEmpty(userInfo.getUserId())) {
            String msg = "Token:" + token + "  UserInfo:" + userInfo;
            LogUtil.d(TAG, "LoadUser return false. " + msg);
            return false;
        }
        BuglyUtils.setUserID(userInfo.getUserId());
        MyApp.getInstance().setToken(token);
        MyApp.getInstance().setUserInfo(userInfo);
        MyApp.getInstance().setUserId(userInfo.getUserId());
        return true;
    }

    public static void saveUser(String token, String userSig, UserInfo userInfo) {
        MyApp.getInstance().setToken(token);
        MyApp.getInstance().setUserInfo(userInfo);
        MyApp.getInstance().setUserId(userInfo.getUserId());

        Config.setUserSig(userSig);
        Config.setUserToken(token);
        Config.saveUserInfo(userInfo);
    }



    public static void clearUserInfo(boolean isLogout) {

        //如果有手机号，那么就跳转到一键登录假页面
        //有手机号码 并且不是注销 那么才不去清除数据
        Config.deleteUserSig();
        Config.deleteUserInfo();
        Config.deleteUserToken();
        MyApp.getInstance().setToken(null);
        MyApp.getInstance().setUserId(null);
        MyApp.getInstance().setUserInfo(null);


    }













    public static String getHeadPortraitSmallDefault(String headPortrait) {
        return getHeadPortraitWithSize(headPortrait, 140);
    }

    public static String getHeadPortraitWithSize(String headPortrait, int size) {
        if (headPortrait == null) {
            return null;
        }

        if (headPortrait.contains(SIZE_APPEND)) {
            return headPortrait;
        }

        return headPortrait + "?" + SIZE_APPEND + size;
    }
}
