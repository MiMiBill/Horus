package com.example.horus.app;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.baselib.utils.LogUtil;
import com.example.horus.BuildConfig;
import com.example.horus.data.UserInfo;
import com.example.horus.data.VersionInfo;
import com.example.horus.utils.SPUtil;
import com.example.horus.utils.UserUtil;


public class Config {

    private static Context mContext;

    public static  boolean isUserLogout = false;

    //在application中调用init方法进行初始化，传入全局context
    public final static void init(Context context){
        mContext = context;
    }

    private static final String KEY_USER_INFO = "Key_UserInfo";
    private static final String KEY_USER_TOKEN = "Key_UserToken";
    private static final String KEY_FEATURE_VERSION = "Key_FeatureVersion";
    private static final String KEY_USER_SIG = "Key_USerSig";// IM登录需要
    private static final String KEY_LOGIN_ACCOUNT = "Key_LoginAccount";// 当前登录账号（邮箱/密码）
    private static final String KEY_LOGIN_REGIN = "Key_LoginRegion";// 当前登录区号
    private static final String KEY_LOGIN_Local = "Key_LoginLocal";// 保存系统地区
    private static final String KEY_VERSION_INFO = "Key_VersionInfo";// 新版本信息
    private static final String KEY_DOWNLOAD_ID = "Key_DownloadId";// apk下载id

    private static final String KEY_FORCE_OFFLINE_CODE = "Key_ForceOfflineCode";// 用户强制下线异常码
    private static final String KEY_FORCE_OFFLINE_MSG = "Key_ForceOfflineMsg";// 强制下线Msg

    private static final String KEY_KEYBOARD_HEIGHT = "Key_KeyboardHeight";// 软键盘高度
    private static final String KEY_VIDEO_CALL_KEYBOARD_HEIGHT = "Key_VideoCallKeyboardHeight";// 主播视频页软键盘高度

    private static final String KEY_OFFICIAL_EVENT = "Key_OfficialEvent";// 官方活动弹窗

    private static final String KEY_SAY_HI_HISTORY = "Key_SayHiHistory";// 打招呼历史纪录（次数/日期）

    private static final String KEY_TRANSLATION_FREE_NUM = "Key_TranslationFreeNum"; //翻译包免费翻译数量

    public static final String KEY_SHOW_BUY_TRANSLATION = "Key_ShowBuyTranslation"; //是否展示过购买翻译包对话框
    public static final String KEY_SHOW_OPEN_TRANSLATION_TIPS = "Key_ShowOpenTranslationTips"; //是否展示过打开自动翻译Tips

    public static final String KEY_AUTH_FAILURE_TIPS = "Key_AuthFailureTips"; //是否展示过认证失败的标签
    public static final String KEY_AUTH_COVER_TIPS = "Key_AuthCoverTips"; //是否展示过认证引导蒙层
    public static final String KEY_ADD_FRIEND_COVER_TIPS = "Key_AddFriendTips"; //是否展示过添加好友引导蒙层
    public static final String KEY_MINE_WALLET_TIPS = "Key_MineWallet"; //是否展示过我的钱包
    public static final String KEY_SHOW_BECOME_CHATTER_TIPS = "KEY_show_become_chatter_TIPS"; //主播
    public static final String KEY_Msg_Light_TIPS = "key_msg_light_tips"; //是否展示过曝光
    public static final String KEY_DOUBBLE_CIRCLE_TIPS = "key_doubble_circle_tips"; //动态圈子指引
    public static final String KEY_MAIN_RECOMMEND = "Key_MainRecommend"; //是否展示过首页推荐Tab
    public static final String KEY_PERSONAL_VIDEO = "Key_PersonalVideo"; //是否展示过个人页主播视频
    public static final String KEY_SELECTED_CONTRACT = "Key_SelectedContract"; //是否选择同意用户协议、隐私政策

    public static final String KEY_SHOW_IMAGE_DRAG_TIP_ANIM = "Key_ShowImageDragTipAnim"; //是否展示过图片滑动提示动画
    public static final String KEY_VOICE_ENABLE = "Key_VoiceEnable"; //手机提示是否有声音提示
    public static final String KEY_APPEAL_TIMES = "Key_AppealTimes"; //申述次数
    public static final String KEY_FOLLOW_IDS = "Key_FollowIds";//关注的talkId集合
    public static final String KEY_HAS_CLAER_SP = "Key_HasCleanSP"; //是否清除过SP
    public static final String KEY_USER_TALK_ID = "Key_User_Talk_Id";//当前用户的id
    private static final String KEY_OLD_PWD = "_OLD_PWD";
    private static final String KEY_USEONEKEYLOGINSTATUS = "useOneKeyLoginStatus";//一键登录得状态
    public static final String KEY_LAST_USER_INFO ="KEY_LAST_USER_INFO";//最后一个具有手机号信息的用户
    public static final String KEY_UNLOGIN_TIMES = "KEY_UNLOGIN_TIMES";//该手机注销次数
    public static final String KEY_IS_REGISTERED = "key_is_registered";//注册状态
    public static final String KEY_SHOWED_SPRING_FESTIVAL_DATE = "key_showed_spring_festival_date";//是否已经展示了春节礼物

    public static final String KEY_TRANSLANT_SWITCH = "key_translant_switch";

    public static final String KEY_TRANSLANT_SWITCH_LOCATION_PERMISSION = "key_translant_switch_location_permission";

    public static final String KEY_IS_FIRST_TIME_INTO_APP = "key_is_first_time_into_app";//是不是第一次

    public static final String KEY_PUSH_SETTING = "key_push_setting_";//个性化推荐

    public static void saveIsFirstTimeIntoApp(){
        SPUtil.put(MyApp.getInstance(),KEY_IS_FIRST_TIME_INTO_APP,false);
    }


    public static boolean isFirstTimeIntoApp(){
        boolean isFirst = (boolean)SPUtil.get(MyApp.getInstance(),KEY_IS_FIRST_TIME_INTO_APP,true);
        return isFirst && !UserUtil.loadUser();
    }











    public static void saveUserInfo(UserInfo userInfo) {
        if (userInfo == null)return;
        String infoStr = JSON.toJSONString(userInfo);
        SPUtil.put(mContext, KEY_USER_INFO, infoStr);
    }



    public static UserInfo getUserInfo() {
        String infoStr = (String) SPUtil.get(mContext, KEY_USER_INFO, SPUtil.SHARE_STRING);
        if (!TextUtils.isEmpty(infoStr)) {
            try {
                return JSONObject.parseObject(infoStr, UserInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }






    public static void deleteUserInfo() {
        if (SPUtil.contains(mContext, KEY_USER_INFO)) {
            SPUtil.remove(mContext, KEY_USER_INFO);
        }
    }

    public static void setUserToken(String token) {
        LogUtil.d("setUserToken","正在保存Token："  + token);
        SPUtil.put(mContext, KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return (String) SPUtil.get(mContext, KEY_USER_TOKEN, SPUtil.SHARE_STRING);
    }

    public static void deleteUserToken() {
        if (SPUtil.contains(mContext, KEY_USER_TOKEN)) {
            SPUtil.remove(mContext, KEY_USER_TOKEN);
        }
    }

    public static void setFeatureVersion(int versionCode) {
        SPUtil.put(mContext, KEY_FEATURE_VERSION, versionCode);
    }

    public static int getFeatureVersion() {
        return (int) SPUtil.get(mContext, KEY_FEATURE_VERSION, 0);
    }

    public static void setUserSig(String userSig) {
        SPUtil.put(mContext, KEY_USER_SIG, userSig);
    }

    public static String getUserSig() {
        return (String) SPUtil.get(mContext, KEY_USER_SIG, SPUtil.SHARE_STRING);
    }

    public static void deleteUserSig() {
        if (SPUtil.contains(mContext, KEY_USER_SIG)) {
            SPUtil.remove(mContext, KEY_USER_SIG);
        }
    }


    /**
     * 保存登录时候的区号
     * @param account
     */
    public static void setLoginRegin(String account) {
        SPUtil.put(mContext, KEY_LOGIN_REGIN, account);
    }

    /**
     * 第一次保存，后面就不保存啦
     */
    public static void setSysLocal(String local) {
        SPUtil.put(mContext, KEY_LOGIN_Local, local);
    }

    /**
     * 获取系统地区
     * @return
     */
    public static String getSysLocal()
    {
        return (String) SPUtil.get(mContext,KEY_LOGIN_Local,"");
    }

    public static String getLoginRegin()
    {
        return (String) SPUtil.get(mContext,KEY_LOGIN_REGIN,"");
    }

    public static void setLoginAccount(String account) {
        SPUtil.put(mContext, KEY_LOGIN_ACCOUNT, account);
    }

    public static String getLoginAccount() {
        return (String) SPUtil.get(mContext, KEY_LOGIN_ACCOUNT, SPUtil.SHARE_STRING);
    }

    public static void setVersionInfo(VersionInfo versionInfo) {
        String json = JSONObject.toJSONString(versionInfo);
        SPUtil.put(mContext, KEY_VERSION_INFO, json);
    }

    public static VersionInfo getVersionInfo() {
        String ver = (String) SPUtil.get(mContext, KEY_VERSION_INFO, SPUtil.SHARE_STRING);
        if (ver != null && !ver.isEmpty()) {
            try {
                return JSONObject.parseObject(ver, VersionInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setApkDownloadId(long downloadId) {
        SPUtil.put(mContext, KEY_DOWNLOAD_ID, downloadId);
    }

    public static long getApkDownloadId() {
        Long range = (Long) SPUtil.get(mContext, KEY_DOWNLOAD_ID, SPUtil.SHARE_LONG);
        if (range == null || range < 0) range = 0L;
        return range;
    }

    public static void setForceOffline(int code, String msg) {
        SPUtil.put(mContext, KEY_FORCE_OFFLINE_CODE, code);
        SPUtil.put(mContext, KEY_FORCE_OFFLINE_MSG, msg);
    }

    public static int getForceOfflineCode() {
        return (int) SPUtil.get(mContext, KEY_FORCE_OFFLINE_CODE, SPUtil.SHARE_INTEGER);
    }

    public static String getForceOfflineMsg() {
        return (String) SPUtil.get(mContext, KEY_FORCE_OFFLINE_MSG, SPUtil.SHARE_STRING);
    }

    public static int getKeyboardHeight() {
        return (int) SPUtil.get(mContext, KEY_KEYBOARD_HEIGHT, SPUtil.SHARE_INTEGER);
    }

    public static void setKeyboardHeight(int keyboardHeight) {
        SPUtil.put(mContext, KEY_KEYBOARD_HEIGHT, keyboardHeight);
    }


    public static int getVideoCallKeyboardHeight() {
        return (int) SPUtil.get(mContext, KEY_VIDEO_CALL_KEYBOARD_HEIGHT, SPUtil.SHARE_INTEGER);
    }

    public static void setVideoCallKeyboardHeight(int keyboardHeight) {
        SPUtil.put(mContext, KEY_VIDEO_CALL_KEYBOARD_HEIGHT, keyboardHeight);
    }




    /**
     * 是否是静音模式
     * @return
     */
    public static boolean getVoiceEnable(){
        return (Boolean) SPUtil.get(mContext, KEY_VOICE_ENABLE, true); //默认提示有声音
    }

    public static void setVoiceEnable(boolean enalble){
        SPUtil.put(mContext, KEY_VOICE_ENABLE, enalble);
    }



    public static void setUserTalkId(String userTalkId)
    {
        SPUtil.put(mContext, KEY_USER_TALK_ID, userTalkId);
    }

    public static String getUserTalkId()
    {
        return (String) SPUtil.get(mContext, KEY_USER_TALK_ID,"");
    }


    public static boolean getHasCleanSP(){
        return (boolean) SPUtil.get(mContext, KEY_HAS_CLAER_SP + BuildConfig.VERSION_NAME, false);
    }

    public static void setHasCleanSP(boolean hasCleanSP){
        SPUtil.put(mContext, KEY_HAS_CLAER_SP + BuildConfig.VERSION_NAME, hasCleanSP);
    }
}
