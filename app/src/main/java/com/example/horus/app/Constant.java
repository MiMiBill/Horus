package com.example.horus.app;

import com.example.horus.BuildConfig;


/**
 * Created by lognyun on 2018/8/7 14:50:11
 * <p>
 * 用于保存通用的值，例如文件名，存储目录等
 */
public class Constant {

    public static final String BASE_URL_RELEASE = "https://www.zerophil.com";// 域名
    public static final String BASE_URL_TEST = "http://49.235.227.208:8080";// 预上线环境
//    public static final String BASE_URL_DEBUG = "http://150.109.61.188:8080";// 测试
    public static String BASE_URL_DEBUG = BASE_URL_RELEASE;// 线上调试
//    public static final String BASE_URL_DEBUG = BASE_URL_TEST;// 预上线环境
//    public static String BASE_URL_DEBUG = "http://192.168.3.130:8080";// 开发-永光
//    public static String BASE_URL_DEBUG = "http://192.168.3.127:8080";// 开发-张毅

    public static final String BASE_URL_COS = "http://worldtalk-1257096260.cos.ap-shanghai";//
    // 文件服务器

    public static final String APOLLO_CONFIG_URL = "http://212.64.4.241:8080/configfiles/json" +
            "/app_conf/default/application";// 配置中心

//    public static final String APOLLO_CONFIG_URL = "http://212.64.4.241:8080/configs/app_conf/default/application?releaseKey=";// 配置中心

    public static final String GOOGLE_TRANSLATE_URL = "https://translation.googleapis.com";//
    // google翻译
    public static final String AZURE_TRANSLATE_URL = "https://api.cognitive.microsofttranslator" +
            ".com/translate?api-version=3.0";// 微软翻译
    public static final String SPRING_FESTIVAL_ACTIVITY_URL_NET = "http://www.worldtalk.link/activity/index.html";
    public static final String SPRING_FESTIVAL_ACTIVITY_URL = "file:///android_asset/springfestival/index.html";

    public static final String FILE_NAME = "shared_info";// 设置存储的文件名

    public static final String FOLDER_NAME = "WorldTalk";// 存储照片视频等文件的文件名

    public static final String DATABASE_NAME = "worldtalk-db";// 数据库

    public static final int FEATURE_VERSION = 100;// 如果已存的特性版本号小于该值，就显示特性页

    public static final int TIME_OUT_SECOND = 10;// 网络超时时间
    public static final int TIME_OUT_SECOND_DEBUG = 100;// 网络超时时间

    public static final int PAGE_SIZE = 20;// 默认一页加载数
    public static final int PAGE_SIZE_TEN = 10;// 默认一页加载数
    public static final int PAGE_SIZE_HOME = 12;// 默认一页加载数
    public static final int PAGE_START = 1;// 分页列表第一页默认为1

    public static final String TRANASLATE_TAG =  "==";

    //百度语音识别



    /**
     * 百度翻译相关
     */
    public static String BAIUD_FANYI_APP_ID = "20200812000540757";
    public static String BAIDU_FANYI_SECRET_KEY = "LTh636WsCRQDRcXGXGoc";


    /**
     * 评论的最大长度
     */
    public static final int COMMENT_MAX_LENGTH = 140;

    public static final String GOOGLE_API_KEY = "AIzaSyC3C_d6WpbiO4W2MsmpWDATp09RM9hJI48";
    public static final String AZURE_TRANS_KEY = "e5f6b3b884d9467a858225f7b9e64f42";

    public static final String PIC_SIGN = "xHjvRizVK1BbbGxr6n19hfedOrphPTEyNTcwOTYyNjAmaz1" +
            "BS0lESFNUSzBXejIzc1d6TGJiYUpyRVFUSzJaMkpQTzZwR1cmZT0xNTM3NjA5NTc5JnQ9MTUzNTAx" +
            "NzU3OSZyPTE4NjYyODY5OTMmZj0vbG9nby5wbmcmYj13b3JsZHRhbGs=";// 图片服务器签名

    public static final String COS_APP_ID = "1257096260";
    public static final String COS_BUCKET = "worldtalk";

    public static final String UMENG_APP_KEY = "5b85406ba40fa352cd000132";

    public static final String WX_APP_ID = "wx8ae4f3db4045ec31";
    public static final String WX_APP_SECRET = "e3d1123225716fd5f6ec3145b6364d22";


    public static final String WX_APP_ID_HW = "wx52ad4f24e3968b1f";
    public static final String WX_APP_SECRET_HW = "1ecad816a4fcff6149e4427d80c3f93f";


    public static final String QQ_APP_ID = "1107729171";
    public static final String QQ_APP_HUAWEI_ID = "101569047";
    public static final String QQ_APP_KEY = "off76cxCiudvhDGp";

    public static final String WB_APP_KEY = "3033215420";
    public static final String WB_APP_SECRET = "01b23d971ff53c398908390011def131";
    public static final String WB_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";


    public static final String WB_APP_HUAWEI_KEY = "1954409858";
    public static final String WB_APP_HUAWEI_SECRET = "c80da1de6bc5aad6176b510f0176fc87";


    public static final String TWITTER_KEY = "3aIN7fuF685MuZ7jtXkQxalyi";
    public static final String TWITTER_SECRET =
            "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO";




    public static final String PUSH_APP_ID_MI = "2882303761517883663";
    public static final String PUSH_APP_KEY_MI = "5771788365663";


    public static final String PUSH_APP_ID_MZ = "116454";
    public static final String PUSH_APP_KEY_MZ = "adf7051e07fc411084853962af4d0784";


    public static final String PUSH_APP_KEY_OPPO = "8cc168f505834e51a52346b4848c6485";
    public static final String PUSH_APP_SECRET_OPPO = "97c8d2b3dcf94f96bc2dfffc08878590";


    /**
     * 融云相关配置
     */
    public static final String IM_RONG_APP_KEY = "ik1qhw09ilyqp"; //融云正式Key

//    public static final String IM_RONG_APP_KEY = "3argexb63qsge"; //融云测试key


    /**
     * 云通信的第三方推送证书
     */
    public static final long IM_PUSH_CER_ID_HW = 4723;// 华为
    public static final long IM_PUSH_CER_ID_MI = 4733;// 小米
    public static final long IM_PUSH_CER_ID_MZ = 4734;// 魅族

    /**
     * 系统通知
     */
    public static final String HOST = "app";
    public static final String SCHEME = "worldtalk";
    public static final String INTENT_MATCH_SUCCESS = "/MatchSuccess";
    public static final String INTENT_MATCH_LIGHT = "/MatchLight";

    public static final String BURIED_POINT_SYSTEM = "Android";

    /**
     * 腾讯Bugly的APP_ID
     */
    public static final String BUGLY_APP_ID = "179380009e";


    /**
     * 填写邀请码
     * imei 已存在的error code
     */
    public static final int ERROR_CODE_COMPLETE_IMEI_ALREADY_EXISTS = 121;

    /**
     * 邀请码不正确
     */
    public static final int ERROR_CODE_ERROR = 118;

    /**
     * 用户被永久封禁的errorCode
     */
    public static final int ERROR_CODE_TEMPORARILY_BAN = 9;
    public static final int ERROR_CODE_PERMANENT_BAN = 99;
    public static final int ERROR_CODE_USER_LOGOUT = 11; //用户注销账号的errorCode
    /**
     * 动态已经被删除
     */
    public static final int ERROR_CODE_MOMENT_DELETE = 122;
    /**
     * 先发后审
     */
    public static final int ERROR_CODE_FIRST_REVIEW_AUTO_PUHLISH = 143;

    /**
     * 内部用户
     * 动态可以编辑定位
     * 可以一键打招呼
     * 聊天直接翻译中文为目标语言
     */
    public static final boolean INTERNAL_USER = false && BuildConfig.DEBUG;


    /**
     * 系统类型 Android 1 / iOS 2
     */
    public static final String SYSTEM_TYPE_ANDROID = "1";

    /**
     * 圈子图片 最大大小
     * <p>
     * <p>
     * 500k
     */
    public static final int MOMENTS_IMAGE_SIZE = 500;


    /**
     * 头像的最大大小
     * 单位 kb
     */
    public static final int AVATAR_SIZE = 1024;
    /**
     * 头像里面视频的最大时间数量
     * 单位 s
     */
    public static final int AVATAR_VIDEO_DURATION = 15;

    /**
     * 发布的时候的视频长度限制
     */
    public static final int PUBLISH_VIDEO_DURATION = 300;


    /**
     * 推送的id
     * <p>
     * 上传埋点的常驻通知
     */
    public static final int PUSH_ID_BURIED_POINT = 1001;
    public static final String AUTH_EXAMPLE_1 = "http://worldtalk-1257096260.cos.ap-shanghai.myqcloud.com/img/ident1.png";
    public static final String AUTH_EXAMPLE_2 = "http://worldtalk-1257096260.cos.ap-shanghai.myqcloud.com/img/ident2.png";
}
