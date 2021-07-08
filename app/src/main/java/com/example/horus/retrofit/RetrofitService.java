package com.example.horus.retrofit;

import com.alibaba.fastjson.JSONObject;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lognyun on 2018/8/7 11:52:56
 * <p>
 * 网络请求接口类
 * <p>
 * 生成COS签名的链接在PutObjectSamples类中定义
 */
public interface RetrofitService {

    @GET
    Observable<String> get(@Url String url);




//
//    @POST("/services/v6/user_login")
//    Observable<BaseResponse<JSONObject>> login(
//            @Body LoginInfo loginInfo);
//
//    @POST("/services/v6/register")
//    Observable<BaseResponse<JSONObject>> register(
//            @Body LoginInfo loginInfo);

//    @POST("/services/v6/replace_phone")
//    Observable<BaseResponse<JSONObject>> replacePhoneNumber(
//    @Body LoginUser loginUser);

//    @POST("/services/v6/phone_binding")
//    @FormUrlEncoded
//    Observable<BaseResponse<JSONObject>> replacePhoneNumber(
//            @Body LoginUser loginUser);

    @POST("/services/v6/phone_binding")
    @FormUrlEncoded
    Observable<BaseResponse<JSONObject>> replacePhoneNumber(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("talkId") String talkId);


    @POST("/services/v6/onekey_phone_binding")
    @FormUrlEncoded
    Observable<BaseResponse<JSONObject>> onekeyPhoneBinding(
            @Field("token") String token,
            @Field("phone") String phone,
            @Field("talkId") String talkId);


    /**
     * string	nationCode	*区号
     * string	phone	*手机号
     * string	verifyCode	*验证码 验证码登录需要验证码 一键注册不需要验证码
     * string	password	*密码 密码登录需要密码1
     * @return
     */
    @POST("/services/v6/onekey_register")
    Observable<BaseResponse<JSONObject>> oneKeyRegister(
           @Body Map<String,String> body
    );



    /**
     * 绑定用户的系统
     *
     * @param channel    渠道
     * @param systemType 系统 1 安卓 2 ios
     */
    @FormUrlEncoded
    @POST("/services/v6/set_user_system")
    Observable<BaseResponse<JSONObject>> setUserSystem(
            @Field("channel") String channel,
            @Field("talkId") String talkId,
            @Field("systemType") String systemType,
            @Field("version") String version,
            @Field("ip") String ip,
            @Field("timeZone") String timeZone,
            @Field("country") String country,
            @Field("systemNum") String systemNum ,//系统编号
            @Field("phoneModel") String phoneModel,
            @Field("phoneSystemVersion") String phoneSystemVersion

    );



//    @POST("/services/v6/third_login")
//    Observable<BaseResponse<JSONObject>> thirdLogin(
//            @Body ThirdLoginInfo thirdLoginInfo);


    /**
     * 获取滑动图形验证码
     *
     * @param nationCode
     * @param phone
     * @param status
     * @return
     */
    @FormUrlEncoded
    @POST("/services/v12/get_verify_image")
    Observable<BaseResponse<JSONObject>> getVerifyImage(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("status") int status);

    /**
     * 验证滑动图形验证码
     *
     * @param nationCode
     * @param phone
     * @param x          0-1之间float值
     * @param y
     * @return
     */
    @FormUrlEncoded
    @POST("/services/v12/verify_image_check")
    Observable<BaseResponse<JSONObject>> checkVerifyImage(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("x") String x,
            @Field("y") String y);

    /**
     * 1 注册 2重置密码 3绑定 5 验证码注册
     */
    @FormUrlEncoded
    @POST("/services/v1/send_verify_code")
    Observable<BaseResponse<JSONObject>> registerSendCodePhone(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("status") int status);

    /**
     * 图形验证码校验
     */
    @FormUrlEncoded
    @POST("/services/v1/verify_graphic_check")
    Observable<BaseResponse<JSONObject>> checkGraphicCode(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("graphicCode") String graphicCode);

    /**
     * 刷新图形验证码
     */
    @FormUrlEncoded
    @POST("/services/v1/get_graphic_verify_code")
    Observable<BaseResponse<JSONObject>> getGraphicCode(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone);

//    /**
//     * 验证码登录/注册
//     */
//    @POST("/services/v6/auth_code_login")
//    Observable<BaseResponse<JSONObject>> authCodeLogin(
//            @Body CodeLoginInfo loginUser
//    );


    @FormUrlEncoded
    @POST("/services/v1/send_email")
    Observable<BaseResponse<Void>> registerSendCodeEmail(
            @Field("email") String email,
            @Field("status") int status);// 1 注册 2重置密码 3绑定

    @FormUrlEncoded
    @POST("/services/v1/verify_phone_check")
    Observable<BaseResponse<Void>> checkVerifyCodePhone(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("verifyCode") String verifyCode,
            @Field("status") int status);// 1 注册 2重置密码 3绑定

    @FormUrlEncoded
    @POST("/services/v1/verify_email_check")
    Observable<BaseResponse<Void>> checkVerifyCodeEmail(
            @Field("email") String email,
            @Field("verifyCode") String verifyCode,
            @Field("status") int status);// 1 注册 2重置密码 3绑定

    @FormUrlEncoded
    @POST("/services/v1/send_code")
    Observable<BaseResponse<JSONObject>> forgetSendCode(
            @Field("phoneOrEmail") String phoneOrEmail);

//    @POST("/services/v6/reset_password")
//    Observable<BaseResponse<JSONObject>> forgetResetPwd(
//            @Body LoginInfo loginInfo);

    /**
     * 第三方注册绑定手机号/邮箱
     *
     * @param type 1 送钻  2非送钻
     */
    @FormUrlEncoded
    @POST("/services/v1/phone_email_binding")
    Observable<BaseResponse<String>> bindPhoneOrEmail(
            @Field("nationCode") String nationCode,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("verifyCode") String verifyCode,
            @Field("talkId") String talkId,
            @Field("type") int type,
            @Field("imei") String imei);
//
//    /**
//     * 注册时上传头像
//     */
//    @POST("/services/v6/update_user_head")
//    Observable<BaseResponse<Void>> uploadHeadPortaitInfo(
//            @Body UserInfo userInfo
//    );
//
//    /**
//     * 跟新用户信息
//     */
//    @POST("/services/v6/update_user")
//    Observable<BaseResponse<Void>> updateUserInfo(
//            @Body UserInfo userInfo);
//
//
//
//        /**
//     * 注册后完善个人信息
//     */
//    @POST("/services/v6/perfection_user_data")
//    Observable<BaseResponse<Void>> perfectionUserData(
//            @Body UserInfo userInfo);


    /**
     * 获取寻缘列表
     */
    @FormUrlEncoded
    @POST("/services/v1/query_search_edges")
    Observable<BaseResponse<JSONObject>> updateUserHead(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("statisticalWeight") long statisticalWeight,
            @Field("sex") int sex,
            @Field("country") String country);

//    /**
//     * 获取寻缘列表
//     */
//    @POST("/services/v1/query_search_edges2")
//    Observable<BaseResponse<JSONObject>> getMatchUserList(
//            @Body MatchUserListRequestParams params);
//
//
//    /**
//     * 获取曝光的结束时间
//     * 类型 1普通曝光 2广告曝光
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/save_exposure_user")
//    Observable<BaseResponse<ExposureEndInfo>> getExposureEndDate(
//            @Field("talkId") String talkId,
//            @Field("country") String country,
//            @Field("type") int type
//
//    );


    /**
     * 更改寻缘状态
     *
     * @param status 3 不应该用了,应该直接调用 (#superLove) 接口
     */
    @FormUrlEncoded
    @POST("/services/v1/save_search_edge")
    Observable<BaseResponse<JSONObject>> saveMatchStatus(
            @Field("talkId") String talkId,
            @Field("matchingTalkId") String matchingTalkId,
            @Field("status") int status);// 1喜欢 2不喜欢 3互相喜欢


    /**
     * 超级喜欢
     * 爆灯
     */
    @FormUrlEncoded
    @POST("/services/v1/super_love_message")
    Observable<BaseResponse<JSONObject>> superLove(
            @Field("talkId") String talkId,
            @Field("matchingTalkId") String matchingTalkId,
            @Field("country") String country, @Field("name") String name,
            @Field("headPortrait") String headPortrait
    );


//    /**
//     * 剩余 爆灯次数
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/get_super_love_num")
//    Observable<BaseResponse<LightCountInfo>> getSuperLoveNum(
//            @Field("talkId") String talkId,
//            @Field("country") String country
//            );
//
//
//    /**
//     * 查询产品价格
//     * <p>
//     * 曝光
//     * 解锁图标
//     * 视频翻译
//     */
//    @POST("/pay/v1/query_product_price")
//    Observable<BaseResponse<PayDrillProductInfoWrapInfo>> queryProductPrice();


    /**
     * 获取“喜欢我”列表
     */
    @FormUrlEncoded
    @POST("/services/v1/quer_search_edge_by_love")
    Observable<BaseResponse<JSONObject>> getLikeMeList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId);


    /**
     * 获取“喜欢我”列表
     *
     * @param seeStatus 1未读 2已读 不传此参数默认查所有
     */
    @FormUrlEncoded
    @POST("/services/v1/quer_search_edge_by_loves")
    Observable<BaseResponse<JSONObject>> getLikeMeListWithReadState(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("seeStatus") int seeStatus,
            @Field("createTime") long create_time);

    /**
     * 标记寻缘->喜欢我,用户为已读
     */
    @FormUrlEncoded
    @POST("/services/v1/update_search_edge_user")
    Observable<BaseResponse<Void>> updateSearchEdgeUser(
            @Field("talkId") String talkId
    );

    /**
     * 标记寻缘->喜欢我,用户为已读
     */
    @FormUrlEncoded
    @POST("/services/v1/update_search_edge_user_status")
    Observable<BaseResponse<Void>> updateSearchEdgeUser(
            @Field("talkId") String talkId,
            @Field("id") long id
    );


    /**
     * 获取“我喜欢”列表
     */
    @FormUrlEncoded
    @POST("/services/v1/quer_search_edge_love")
    Observable<BaseResponse<JSONObject>> getILikeList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId);


//    /**
//     * 查询 闪屏页的banner
//     */
//    @POST("/services/v1/query_splash_screes")
//    Observable<BaseResponse<SplashBannerWrapInfo>> querySplashScrees();
//
//
//    /**
//     * 查询 首页的banner
//     */
//    @POST("/services/v1/query_banners")
//    Observable<BaseResponse<HomeBannerWrapInfo>> queryBanners();

    /**
     * 获取首页列表
     */
    @FormUrlEncoded
    @POST("/services/v1/query_home_pages")
    Observable<String> getHomeList(
            @Field("statisticalWeight") long statisticalWeight,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("sex") int sex,
            @Field("beginBirthday") long beginBirthday,
            @Field("endBirthday") long endBirthday,
            @Field("country") String country,
            @Field("alliance") String alliance,
            @Field("userType") int userType,
            @Field("payType") int payType,
            @Field("anchorStatisticalWeight") long anchorStatisticalWeight

    );

//    //获取首页主播列表
//    @FormUrlEncoded
//    @POST("/services/v1/query_anchors")
//    Observable<BaseResponse<HomeUserinfoWrapInfo>> getHomeListRecommend(
//            @Field("statisticalWeight") long statisticalWeight,
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId,
//            @Field("sex") int sex,
//            @Field("beginBirthday") long beginBirthday,
//            @Field("endBirthday") long endBirthday,
//            @Field("country") String country,
//            @Field("alliance") String alliance);

    /**
     * 获取首页列表
     */
    @FormUrlEncoded
    @POST("/services/v1/query_home_page")
    Observable<String> getHomeListWithCache(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("sex") int sex,
            @Field("country") String country,
            @Field("alliance") String alliance);

//    /**
//     * 获取其他用户详情
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_detail")
//    Observable<BaseResponse<UserInfoWrapResultInfo>> getUserDetailInfo(
//            @Field("talkId") String talkId, @Field("othersTalkId") String othersTalkId);
//
//
//    /**
//     * 个人主页接口
//     * 三合一
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_info")
//    Observable<BaseResponse<UserinfoThreeToOneWrapInfo>> getUserInfoThreeToOn(
//            @Field("talkId") String talkId, @Field("othersTalkId") String othersTalkId,
//            @Field("pageNum") int pageNum,
//            @Field("pageSize") int pageSize,
//            @Field("country") String country
//            );


//    /**
//     * 获取其他用户详情
//     * 融云IM所用
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_im")
//    Observable<BaseResponse<IMUserInfoWrapResultInfo>> getUserDetailInfoIM(
//            @Field("talkId") String talkId,
//            @Field("othersTalkId") String othersTalkId,
//            @Field("country") String country
//
//    );
//
//
//    /**
//     * 获取粉丝列表
//     */
//    @FormUrlEncoded
//    @POST("/services/v10/query_fans_list")
//    Observable<BaseResponse<FansInfoWrapInfo>> queryFansList(
//            @Field("pageNum") int pageNum,
//            @Field("pageSize") int pageSize,
//            @Query("createTime") long createTime,
//            @Field("talkId") String talkId
//    );

    /**
     * 删除粉丝
     * string	talkId	用户ID
     * string	concernTalkId	关注者ID
     */
    @FormUrlEncoded
    @POST("/services/v10/delete_fans")
    Observable<BaseResponse<Void>> deleteFans(
            @Field("talkId") String talkId,
            @Field("concernTalkId") String concernTalkId

    );

//    /**
//     * 获去关注列表
//     */
//    @FormUrlEncoded
//    @POST("/services/v10/query_attention_list")
//    Observable<BaseResponse<AttentionInfoWrapInfo>> queryAttentionList(
//            @Field("pageNum") int pageNum,
//            @Field("pageSize") int pageSize,
//            @Query("createTime") long createTime,
//            @Field("talkId") String talkId
//    );


//    /**
//     * 获取用户可以选择的标签
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_label_list")
//    Call<BaseResponse<PersonalInformationExtraFlagInfoWrapInfo>> queryLabelListSynchronized(
//            @Field("language") String language,
//            @Field("country") String country
//    );
//
//    /**
//     * 获取 用户的图片
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_images")
//    Observable<BaseResponse<MediaInfoWrapInfo>> getUserImages(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId,
//            @Field("othersTalkId") String othersTalkId,
//            @Field("country") String country
//    );

//    /**
//     * 获取 用户的图片 ,视频
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_photos")
//    Observable<BaseResponse<MediaInfoWrapInfo>> getUserMedia(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId,
//            @Field("othersTalkId") String othersTalkId,
//            @Field("country") String country
//    );

    /**
     * 删除 用户的图片
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_user_images")
    Observable<BaseResponse<Void>> deleteUserMedia(
            @Field("talkId") String talkId,
            @Field("ids") String dis,
            @Field("country") String country

    );

    /**
     * 删除 用户的图片
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_user_images_all")
    Observable<BaseResponse<Void>> deleteUserMediaWithoutIDs(
            @Field("talkId") String talkId,
            @Field("ids") String dis,
            @Field("country") String country
    );


    /**
     * 删除 用户的 ,视频
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_user_video")
    Observable<BaseResponse<Void>> deleteUserVideo(
            @Field("talkId") String talkId,
            @Field("ids") String dis,
            @Field("country") String country

    );

    /**
     * 删除 用户的图片 ,视频
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_user_video_all")
    Observable<BaseResponse<Void>> deleteUserVideoWithoutIDs(
            @Field("talkId") String talkId,
            @Field("ids") String dis,
             @Field("country") String country
    );


    /**
     * 保存用户的头像相册
     */
    @POST("/services/v1/save_user_photo_album")
    Observable<BaseResponse<Void>> saveUserPhotoAlbum(@Body RequestBody allInfo);


//    /**
//     * 查询用户头像数据
//     */
//    @GET("/services/v1/query_user_photo_album_by_talk_id")
//    Observable<BaseResponse<AllAvatarInfo>> queryUserPhotoAlbumByTalkID(
//            @Query("talkId") String talkId
//    );

    /**
     * /**
     * 改变关注的状态
     */
    @FormUrlEncoded
    @POST("/services/v1/save_concern")
    Observable<BaseResponse<Void>> saveConcern(
            @Field("talkId") String talkId,
            @Field("concernTalkId") String concernTalkId);


//    /**
//     * 发布动态
//     */
//    @POST("/services/v1/save_dynamic_state")
//    Observable<BaseResponse<String>> publishMoment(
//            @Body MomentInfo momentInfo);


    /**
     * 黑名单
     *
     * @param type 1 屏蔽 2拉黑
     */
    @FormUrlEncoded
    @POST("/services/v1/save_black_list")
    Observable<BaseResponse<Void>> saveBlackList(
            @Field("talkId") String talkId,
            @Field("blackTalkId") String blackTalkId,
            @Field("type") int type
    );


//    /**
//     * 查询黑名单屏蔽
//     *
//     * @param type 1 屏蔽 2拉黑
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/get_black")
//    Observable<BaseResponse<BlackListInfoWrapInfo>> getBlack(
//            @Field("talkId") String talkId,
//            @Field("blackTalkId") String blackTalkId,
//            @Field("type") int type
//    );


    /**
     * 移除 黑名单
     *
     * @param type 1 屏蔽 2拉黑
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_black_list")
    Observable<BaseResponse<Void>> deleteBlackList(
            @Field("talkId") String talkId,
            @Field("blackTalkId") String blackTalkId,
            @Field("type") int type
    );


    /**
     * 黑名单列表
     *
     * @param type 1 屏蔽 2拉黑
     */
    @FormUrlEncoded
    @POST("/services/v1/query_black_list")
    Observable<BaseResponse<JSONObject>> queryBlackList(
            @Field("talkId") String talkId,
            @Field("blackTalkId") String blackTalkId,
            @Field("type") int type
    );


    /**
     * 获取 用户的动态
     */
    @FormUrlEncoded
    @POST("/services/v1/delete_dynamic_state")
    Observable<BaseResponse<Void>> deleteDynamicState(@Field("dynamicId") Long talkId);


    /**
     * 获取某一用户的动态
     * talkId是查询者的ID
     * othersTalkId 是被查询者的ID
     * <p>
     * 在1.5.0之前的版本中 仅有talkId字段 表示被查询者的ID
     */
    @FormUrlEncoded
    @POST("/services/v1/query_dynamic_states_by_user")
    Observable<BaseResponse<JSONObject>> queryDynamicInfoListByUser(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("othersTalkId") String othersTalkId,
            @Field("country") String country

    );

    /**
     * 查询动态列表（世界圈）， 热门动态与普通动态合并一个接口查询
     * -动态列表 第一页的话，第一条是精选
     *
     * @return
     */
    @FormUrlEncoded
    @POST("/services/v10/query_dynamic_list")
    Observable<BaseResponse<JSONObject>> queryDynamicList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("dynamicId") Long dynamicId,
            @Field("statisticalWeight") Long statisticalWeight
    );

    /**
     数据类型	属性名称	标注
     int	pageSize	页数
     int	pageNum	页码
     string	talkId	用户ID
     Long	dynamicId	动态id 第一页传精选id 第一次传0
     long	statisticalWeight	权重 去掉精选权重，取最小值
     * @return
     */
    @FormUrlEncoded
    @POST("/services/v1/query_choiceness_dynamic_states")
    Observable<BaseResponse<JSONObject>> queryChoicenessDynamicStates(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("dynamicId") Long dynamicId,
            @Field("statisticalWeight") Long statisticalWeight
    );



    /**
       主播推荐
     * 类型:[1:卡片,2:落地页,3:弹窗]
     * 权重 传入最小权重
     * @return
     */
    @FormUrlEncoded
    @POST("/services/v1/query_anchors_recommend")
    Observable<BaseResponse<JSONObject>> queryAnchorsRecommend(
            @Field("type") Integer type,
            @Field("statisticalWeight") Long statisticalWeight
    );

    /**
     * 查询普通动态（世界圈）列表1.7
     */
    @FormUrlEncoded
    @POST("/services/v1/query_dynamic_state_list")
    Observable<BaseResponse<JSONObject>> getWorldCircleList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("statisticalWeight") Long statisticalWeight
    );

    /**
     * 查询动态（朋友圈）列表
     */
    @FormUrlEncoded
    @POST("/services/v10/query_friend_dynamic_state_list")
    Observable<BaseResponse<JSONObject>> getFriendCircleList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("dynamicId") Long dynamicId,
            @Field("country") String country
    );


    /**
     * 查询动态（世界圈热门)
     * 分页
     */
    @FormUrlEncoded
    @POST("/services/v1/query_hot_dynamic_states")
    Observable<BaseResponse<JSONObject>> getHotCircleList(
            @Field("talkId") String talkId,
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize);

    /**
     * 动态（圈子）点赞
     */
    @FormUrlEncoded
    @POST("/services/v1/update_dynamic_laud_num")
    Observable<BaseResponse<String>> addCircleLikeNum(
            @Field("dynamicId") long dynamicId,
            @Field("talkId") String talkId,
            @Field("name") String name,
            @Field("headPortrait") String headPortrait,
            @Field("receiveTalkId") String receiveTalkId,
            @Field("url") String url,// 动态的图片url
            @Field("country") String country);

    /**
     * 动态（圈子）分享
     */
    @FormUrlEncoded
    @POST("/services/v1/update_dynamic_forwar_num")
    Observable<BaseResponse<String>> addCircleShareNum(
            @Field("dynamicId") long dynamicId,
            @Field("talkId") String talkId);

    /**
     * 动态（圈子）评论
     */
    @Deprecated
    @FormUrlEncoded
    @POST("/services/v1/save_dynamic_comment")
    Observable<BaseResponse<String>> addCircleComment(
            @Field("dynamicId") long dynamicId,
            @Field("talkId") String talkId,
            @Field("name") String name,
            @Field("content") String content,
            @Field("headPortrait") String headPortrait,
            @Field("receiveTalkId") String receiveTalkId,
            @Field("url") String url);// 动态的图片url

    /**
     * 动态（圈子）评论2
     * 后台要求使用GET
     */
    @GET("/services/v1/save_dynamic_state_comment")
    Observable<BaseResponse<JSONObject>> addCircleComment2(
            @QueryMap Map<String, Object> comment);
//            @Body CommentInfo commentInfo);


    /**
     * 添加二级评论
     * 后台要求使用GET
     */
    @GET("/services/v1/save_dynamic_state_comment_second")
    Observable<BaseResponse<JSONObject>> addCircleSecondLevleComment(
            @QueryMap Map<String, Object> comment);
//            @Body CommentInfo commentInfo);


    /**
     * 动态（圈子）评论列表
     */
    @FormUrlEncoded
    @POST("/services/v1/query_comment_detail_list")
    Observable<BaseResponse<JSONObject>> getCommentList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("dynamicId") long dynamicId,
            @Field("commentId") Long commentId //一级评论ID
    );

    /**
     * 动态（圈子）二级评论列表
     */
    @FormUrlEncoded
    @POST("/services/v1/query_comment_second_list")
    Observable<BaseResponse<JSONObject>> getCommentSubList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("dynamicCommentId") long dynamicCommentId);

    /**
     * 删除/举报 动态评论
     * 2举报 3删除
     */
    @FormUrlEncoded
    @POST("/services/v1/update_dynamic_comment")
    Observable<BaseResponse<Void>> updateDynamicComment(
            @Field("dynamicId") long dynamicId,
            @Field("id") long id,
            @Field("talkId") String talkId,
            @Field("status") int status);

    /**
     * 动态（圈子）评论点赞
     */
    @FormUrlEncoded
    @POST("/services/v1/save_dynamic_comment_laud")
    Observable<BaseResponse<String>> addCircleCommentLikeNum(
            @Field("commentId") long commentId,
            @Field("talkId") String talkId,
            @Field("type") String type,// 评论等级
            @Field("dynamicId") long dynamicId,
            @Field("name") String name,
            @Field("headPortrait") String headPortrait,
            @Field("receiveTalkId") String receiveTalkId,
            @Field("url") String url);// 动态的图片url

    /**
     * 动态（圈子）添加二级评论
     * dynamicCommentId 是一级评论id
     * receiveCommentId 是二级评论id
     * 回复一级评论时 receiveCommentId 传 null 即可
     */
    @FormUrlEncoded
    @POST("/services/v1/save_dynamic_comment_second")
    Observable<BaseResponse<String>> addCircleCommentSub(
            @Field("dynamicCommentId") long dynamicCommentId,
            @Field("talkId") String talkId,
            @Field("name") String name,
            @Field("headPortrait") String headPortrait,
            @Field("content") String content,
            @Field("receiveTalkId") String receiveTalkId,
            @Field("receiveName") String receiveName,
            @Field("replyLevel") int replyLevel,
            @Field("url") String url, // 动态的图片url
            @Field("dynamicId") long dynamicId,
            @Field("receiveCommentId") Long receiveCommentId);


    /**
     * 查询动态消息列表
     *
     * @param type 4点赞 5打赏 6评论
     */
    @FormUrlEncoded
    @POST("/services/v1/query_dynamic_infos_by_type")
    Observable<BaseResponse<JSONObject>> queryCircleMessageList(
            @Field("pageSize") int pageSize,
            @Field("pageNum") int pageNum,
            @Field("talkId") String talkId,
            @Field("type") int type);

    /**
     * 获取动态详情
     */
    @FormUrlEncoded
    @POST("/services/v1/get_dynamic_state")
    Observable<BaseResponse<JSONObject>> getCircleDetail(
            @Field("talkId") String talkId,
            @Field("dynamicId") long dynamicId);

//    /**
//     * 圈子 打赏
//     * 查询可打赏的礼物
//     */
//    @POST("/pay/v1/query_gift_price")
//    Observable<BaseResponse<RewardGiftInfoWrapInfo>> queryGiftPrice();
//
//    /**
//     * 主播视频
//     * 查询礼物
//     */
//    @POST("/pay/v12/query_gift_price")
//    Observable<BaseResponse<RewardGiftInfoWrapInfo>> queryGiftPrice2();
//
//    /**
//     * 查询可我收到和送出的礼物所有的
//     * type	int	1收到 2发出
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_gift_income")
//    Observable<BaseResponse<GiftTradeAllInfoWrapInfo>> queryGiftTradeAll(
//            @Field("talkId") String talkId, @Field("date") String date,
//            @Field("type") int type
//    );
//
//
//    /**
//     * 查询可我收到和送出的礼物
//     * type	int	1收到 2发出
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_gift_trade")
//    Observable<BaseResponse<GiftTradeInfoWrapInfo>> queryGiftTrade(
//            @Field("talkId") String talkId, @Field("date") String date,
//            @Field("type") int type, @Field("channels") String channels,
//            @Field("pageSize") int pageSize, @Field("pageNum") int pageNum
//    );
//
//    //获取礼物详情蓝钻
//    @FormUrlEncoded
//    @POST("pay/v1/query_gift_trade_blue")
//    Observable<BaseResponse<GiftTradeInfoWrapInfo>> queryGiftTradeBlue(
//            @Field("talkId") String talkId,
//            @Field("type") int type, @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum);
//
//
//    /**
//     * 充值界面 可以充值的商品的分类
//     *
//     * @param type    1 粉钻 2 VIP 5翻译包 10 蓝砖
//     * @param channel 查询VIP的价格的渠道,不同的vip价格不一样
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_pay_price")
//    Observable<BaseResponse<RechargeSortWrapInfo>> queryPayPrice(
//            @Field("type") int type,
//            @Field("channel") String channel
//    );
//
//    /**
//     * 翻译包充值页面 商品的分类
//     * int	type	1 砖石 2 VIP 5翻译包
//     * String  channel	渠道
//     * int	payType	支付类型 1 默认类型 2 华为支付 3 谷歌支付
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_pay_price")
//    Observable<BaseResponse<RechargeSortWrapInfo>> queryPayPrice2(
//            @Field("type") int type,
//            @Field("channel") String channel,
//            @Field("payType") int payType
//    );
//
//    /**
//     * 查询翻译包
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_translation")
//    Observable<BaseResponse<String>> queryTranslation(
//            @Field("talkId") String talkId
//    );
//
//
//    /**
//     * 查询钱包信息
//     *
//     * @param talkId 用户id
//     */
//    @FormUrlEncoded
//    @POST("pay/v12/get_wallet")
//    Observable<BaseResponse<MineWalletWrapInfo>> getWallet(@Field("talkId") String talkId);
//
//
//    /**
//     * 调起支付
//     * int	payType	支付类型 1支付宝 2微信 3谷歌支付 4苹果支付
//     * string	subject	标题 例如砖石充值
//     * string	body	支付标注 例如优惠40
//     * string	talkId	id
//     * int	channel	付费渠道 3视频翻译充值 4曝光充值 5被喜欢开通VIP 6撤销开通VIP 7爆灯开通VIP 8私密照开通VIP 9普通充值
//     * string	productCode	产品Code
//     * int	goodsType	产品类型
//     * int	orderType	订单类型 1充值订单2VIP订单
//     */
//    @POST("/pay/v1/pay_gateway")
//    Observable<BaseResponse<PayOrderInfo>> payGateway(@Body RequestBody payOrder);
//
//
//    /**
//     * 支付砖石
//     * int	type	交易类型 1视频翻译 2私密 3礼物 4曝光
//     * string	talkId	id
//     * int	channel	11私密照打赏12 动态圈打赏 13 视频翻译 14 聊天打赏 15 视频打赏
//     * string	productCode	产品Code
//     * string	tradeTalkId	接受交易方的talkId
//     * Long	dynamicId	动态id 类型为私密照需要
//     * Long	image	图片id 类型为私密照需要
//     */
//    @POST("/pay/v1/trade_gateway")
//    Observable<BaseResponse<String>> tradeGateway(@Body RequestBody payOrder);
//
//    /**
//     * 华为支付，处理订单
//     */
//    @FormUrlEncoded
//    @POST("pay/v1/hua_wei_pay_call")
//    Observable<BaseResponse<Object>> processHuaweiRecharge(@Field("orderNo") String orderNo,
//                                                           @Field("purchaseToken") String purchaseToken,
//                                                           @Field("productId") String productId,
//                                                           @Field("subscriptionId") String subscriptionId);
//
//    /**
//     * 谷歌支付 ,处理订单
//     */
//    @Headers({RetrofitManager.CONNECT_TIMEOUT + ":30000", RetrofitManager.READ_TIMEOUT + ":30000"
//            , RetrofitManager.WRITE_TIMEOUT + ":30000"})
//    @FormUrlEncoded
//    @POST("http://49.51.41.107:8080/pay/v1/google_pay_result_call")
//    Observable<BaseResponse<Object>> processGoogleRecharge(
//            @Field("packgName") String packageName,
//            @Field("orderNo") String orderNo,
//            @Field("mToken") String token);
//
//    /**
//     * pay pal支付 ,处理订单
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/pay_pal_result_call")
//    Observable<BaseResponse<Object>> processPayPalRecharge(
//            @Field("orderNo") String orderNo,
//            @Field("paymentId") String paymentId);
//
//    /**
//     * 查询支付记录
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_pay_order")
//    Observable<BaseResponse<RechargeRecordGroupInfo>> queryRechargeRecord(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("date") String date,
//            @Field("talkId") String talkId
//    );
//
//    /**
//     * 查询今日收益
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_income")
//    Observable<BaseResponse<HistoryIncomeInfoWrapInfo>> queryTodayIncome(
//            @Field("talkId") String talkId
//    );
//
//
//    /**
//     * 查询累计收益
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_accumulate_income")
//    Observable<BaseResponse<HistoryIncomeInfoWrapInfo>> queryAccumulateIncome(
//            @Field("talkId") String talkId,
//            @Field("date") String date,
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum
//    );
//
//    /**
//     * 添加好友
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/update_user_remark")
//    Observable<BaseResponse<Void>> updateUserRemark(
//            @Field("talkId") String talkId,
//            @Field("friendTalkId") String friendTalkId,
//            @Field("remark") String remark,
//            @Field("country") String country
//
//    );
//
//
//    /**
//     * 添加好友
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/save_friend")
//    Observable<BaseResponse<String>> sendAddFriend(
//            @Field("talkId") String talkId,
//            @Field("friendTalkId") String friendTalkId);
//
//    /**
//     * 删除好友
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/delete_friend")
//    Observable<BaseResponse<Void>> deleteFriend(
//            @Field("talkId") String talkId,
//            @Field("friendTalkId") String friendTalkId);
//
//
//
//    /**
//     * 搜索好友
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/search_friend_user")
//    Observable<BaseResponse<FriendSearchResultInfo>> queryFriendUser(
//            @Field("searchKey") String searchKey,
//            @Field("talkId") String talkId,
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum
//    );
//
//    /**
//     * 好友申请列表
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_friend_request_list")
//    Observable<BaseResponse<UserinfoWrapInfo>> queryAddFriendList(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId);
//
//
//
//    /**
//     * 处理好友请求
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/dispose_friend_request")
//    Observable<BaseResponse<Void>> disposeFriendRequest(
//            @Field("status") int status,
//            @Field("talkId") String talkId,
//            @Field("friendTalkId") String friendTalkId
//    );
//
//
//    /**
//     * 批量处理好友请求
//     * status 2拒绝 3同意
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/dispose_batch_friend_request")
//    Observable<BaseResponse<Void>> disposeBatchFriendRequest(
//            @Field("status") int status,
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 好友列表
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_friend_list")
//    Observable<BaseResponse<UserinfoWrapInfo>> queryFriendList(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId);
//
//    /**
//     * 好友列表
//     * 缓存框架所用
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_friend_list")
//    Observable<String> queryFriendListForCache(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId);
//
//    /**
//     * 查询未读消息数量
//     * 13动态 14寻缘 15好友 2系统消息
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_message_num")
//    Observable<BaseResponse<UnreadCountResultInfo>> queryUserMessageNum(
//            @Field("type") int type,
//            @Field("talkId") String talkId);
//
//    /**
//     * 清空未读消息数量
//     * 13动态 14寻缘 15好友 2系统消息 21新粉丝 22打赏 23评论 24点赞
//     * 31访客
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/empty_user_message_num")
//    Observable<BaseResponse<Void>> emptyUserMessageNum(
//            @Field("type") int type,
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 查询系统消息
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_message_list")
//    Observable<BaseResponse<SystemMessageWrapInfo>> querySystemMessageList(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 标记系统消息为已读
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/update_message_read")
//    Observable<BaseResponse<Void>> updateMessageRead(
//            @Field("id") long id,
//            @Field("talkId") String talkId);
//
//    /**
//     * 删除系统消息
//     *
//     * @param id ,删除单个传ID，全部删除不需要传id
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/delete_message")
//    Observable<BaseResponse<Void>> deleteSystemMessage(
//            @Field("id") Long id, @Field("talkId") String talkId);
//
//
//    /**
//     * 上传埋点数据
//     */
//    @POST("/services/v1.1.0/batch_burying_point")
//    Observable<BaseResponse<Void>> batchBuryingPoint(
//            @Body RequestBody buryingPoints);
//
//    /**
//     * 新上传埋点数据
//     */
//    @POST("http://192.168.101.203:8080/action/v1/batch_burying_point")
//    Observable<BaseResponse<Void>> newBatchBuryingPoint(
//            @Body RequestBody buryingPoints);
//
//    /**
//     * 举报
//     * type 1动态举报 2用户举报
//     *
//     * @param talkId       被举报人ID
//     * @param reportTalkId 举报人ID
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/save_impeach")
//    Observable<BaseResponse<String>> report(
//            @Field("reason") String reason,
//            @Field("talkId") String talkId,
//            @Field("impeachTalkId") String reportTalkId,
//            @Field("impeachImage") String images,
//            @Field("remark") String remark,
//            @Field("type") String type,
//            @Field("dynamicId") Long dynamicId,
//            @Field("name") String name,
//            @Field("country") String country
//
//    );
//
//    /**
//     * 修改密码
//     */
//    @FormUrlEncoded
//    @POST("services/v1/update_user_password")
//    Observable<BaseResponse<Void>> updateUserPassword(
//            @Field("talkId") String talkId,
//            @Field("password") String password,
//            @Field("newPassword") String newPassword
//
//    );
//
//    @FormUrlEncoded
//    @POST("/pay/v1/query_vip_by_talk_id")
//    Observable<BaseResponse<String>> getVipInfo(
//            @Field("talkId") String talkId);
//
//    @FormUrlEncoded
//    @POST("/services/v1/query_system_version")
//    Observable<BaseResponse<String>> getAppVersion(
//            @Field("channelPackCode") String channelPackCode,
//            @Field("version") String version);// 用户当前版本号
//
//
//    /**
//     * 查询邀请的好友的列表
//     */
//    @FormUrlEncoded
//    @POST("services/v1/query_inviter_friend_list")
//    Observable<BaseResponse<InviteUserWrapInfo>> queryInviterFriendList(
//            @Field("pageSize") int pageSize,
//            @Field("pageNum") int pageNum,
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 查询审核结果
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_user_status")
//    Observable<BaseResponse<ReviewResultInfo>> getReviewIconNickNameResult(
//            @Field("talkId") String talkId,
//            @Field("country") String country
//            );
//
//
//    /**
//     * 结束视频通话
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/end_gateway")
//    Observable<BaseResponse<String>> endVideoTranslate(
//            @Field("orderNo") String orderNo);
//
//    /**
//     * 视频通话检测
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/order_detection")
//    Observable<BaseResponse<String>> videoOrderDetection(
//            @Field("orderNo") String orderNo);
//
//
//    /**
//     * 声网Token
//     */
//    @FormUrlEncoded
//    @POST("services/v1/query_agora_token")
//    Observable<BaseResponse<JSONObject>> videoCallToken(
//            @Field("channelName") String channelName);
//
//
//    /**
//     * 问题反馈
//     * int	type	类型 1普通反馈 2 登录页反馈
//     */
//    @FormUrlEncoded
//    @POST("services/v1/save_couple_back")
//    Observable<BaseResponse<Void>> saveCoupleBack(
//            @Field("name") String name,
//            @Field("talkId") String talkId,
//            @Field("sex") int sex,
//            @Field("content") String content,
//            @Field("version") String version,
//            @Field("channel") String channel,
//            @Field("systemType") String systemType,
//            @Field("phoneModel") String phoneModel,
//            @Field("phoneVersion") String phoneVersion,
//            @Field("phoneSystem") String phoneSystem,
//            @Field("country") String country,
//            @Field("type") int type,
//            @Field("contactWay") String contactWay
//    );
//
//
//    /**
//     * 查询活动信息
//     */
//    @FormUrlEncoded
//    @POST("services/v1/query_activity ")
//    Observable<BaseResponse<String>> queryActivities(
//            @Field("language") String language);
//
//
//    /**
//     * 查询动态圈未读消息数
//     */
//    @FormUrlEncoded
//    @POST("services/v1/query_inform_num")
//    Observable<BaseResponse<JSONObject>> queryUnreadMsgNum(
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 清空互动消息历史（和清空消息未读数接口区分开）
//     * 类型 4点赞 5打赏 6评论 7粉丝 10全部
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/update_dynamic_info_empty")
//    Observable<BaseResponse<Void>> emptyCircleMsgHistory(
//            @Field("type") int type,
//            @Field("talkId") String talkId);
//
//
//    /**
//     * 查询ip是否在中国
//     */
//    @Headers({RetrofitManager.CONNECT_TIMEOUT + ":30000", RetrofitManager.READ_TIMEOUT + ":30000"
//            , RetrofitManager.WRITE_TIMEOUT + ":30000"})
//    @GET("http://ip.taobao.com/service/getIpInfo.php?ip=myip")
//    Observable<String> checkIpLocale();
//
//    /**
//     * 查询ip是否在中国
//     */
//    @Headers({RetrofitManager.CONNECT_TIMEOUT + ":30000", RetrofitManager.READ_TIMEOUT + ":30000"
//            , RetrofitManager.WRITE_TIMEOUT + ":30000"})
//    @GET("http://pv.sohu.com/cityjson?ie=utf-8")
//    Observable<String> checkIpLocaleSohu();
//
//
//    /**
//     * 聊天曝光
//     */
//    @FormUrlEncoded
//    @POST("services/v1/query_exposure_data_list")
//    Observable<String> queryChatExposure(
//            @Field("talkId") String talkId,
//            @Field("pageNum") int pageNum,
//            @Field("pageSize") int pageSize,
//            @Field("statisticalWeight") long statisticalWeight);
//
//
//
//
//    /**
//     * 获取曝光的结束时间
//     */
//    @FormUrlEncoded
//    @POST("/pay/v1/query_chat_exposure")
//    Observable<BaseResponse<ChatExposureEndInfo>> getChatExposureEndDate(
//            @Field("talkId") String talkId
//    );
//
//
//    /**
//     * 增加访客记录
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/save_visitant")
//    Observable<BaseResponse<Void>> saveVisitant(
//            @Field("talkId") String talkId,
//            @Field("visitantTalkId") String visitantTalkId);
//
//
//    /**
//     * 查询访客列表
//     */
//    @FormUrlEncoded
//    @POST("/services/v10/query_visitant_list")
//    Observable<BaseResponse<VisitorUserInfoWrapInfo>> queryVisitantList(
//            @Field("talkId") String talkId,
//            @Field("pageNum") int pageNum,
//            @Field("pageSize") int pageSize,
//            @Field("createTime") long create_time
//    );
//
//
//    /**
//     * 增加好友记录
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/delete_visitant")
//    Observable<BaseResponse<VisitorUserInfoWrapInfo>> deleteVisitantRecord(
//            @Field("id") String id);
//
//
//    /**
//     * 查询所有的未读数量
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/query_informs")
//    Observable<BaseResponse<AllMessageInfoWrapInfo>> queryInforms(
//            @Field("talkId") String id);
//
//    /**
//     * 图片认证
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/user_ident")
//    Observable<BaseResponse<Void>> setIdentImage(
//            @Field("identPhoto") String identPhoto,
//            @Field("systemPhoto") String systemPhoto,
//            @Field("talkId") String talkId,
//            @Field("country") String country
//    );
//
//    /**
//     * 账号注销
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/user_logout")
//    Observable<BaseResponse<Void>> userLogout(
//            @Field("phone") String phone,
//            @Field("sex") int sex,
//            @Field("name") String name,
//            @Field("country") String country,
//            @Field("headPortrait") String headPortrait,
//            @Field("talkId") String talkId,
//            @Field("version") String version,
//            @Field("channel") String channel,
//            @Field("systemType") String systemType,
//            @Field("registerTime") long registerTime,
//            @Field("logoutReason") int logoutReason
//    );
//
//    /**
//     * 视频日志
//     */
//    @FormUrlEncoded
//    @POST("http://118.25.90.14:8080/action/v1/insert_video_log")
//    Observable<BaseResponse<Void>> insertVideoLog(
//            @Field("orderNo") String orderNo,
//            @Field("data") String data,
//            @Field("talkId") String talkId
//    );
//
//
//
//    /**
//     * 数据类型	属性名称	标注
//     * String	country	国籍
//     * int	sex	性别
//     * int	translationSystem	翻译系统
//     * int	translationId	翻译ID
//     */
//    @FormUrlEncoded
//    @POST("http://111.231.135.31:8080/action/v1/query_translate_switch")
////    @POST("http://192.168.3.126:8080/action/v1/query_translate_switch")
//    Observable<BaseResponse<JSONObject>> queryTranslateSwitch(
//            @Field("country") String country,
//            @Field("sex") Integer sex,
//            @Field("translationSystem") Integer translationSystem,
//            @Field("translationId") Integer translationId,
//            @Field("nationCode") String nationCode
//    );
//
//
//    /**
//     * 下面是主播视频聊天
//     */
//    //创建视频订单
//    @POST("pay/v2/create_video_order")
//    Observable<BaseResponse<VideoOrderResultInfo>> createVideoOrder(@Body RequestBody videoOrder);
//
//
//    //拒接视频订单
//    @FormUrlEncoded
//    @POST("pay/v2/refuse_video_order")
//    Observable<BaseResponse<JSONObject>> refuseVideoOrder(@Field("talkId") String talkId, @Field("orderNo") String orderNo, @Field("type") int type);
//
//
//    //接通视频订单
//    @FormUrlEncoded
//    @POST("pay/v2/call_video_order")
//    Observable<BaseResponse<Void>> callVideoOrder(@Field("type") int type,
//                                                  @Field("talkId") String talkId,
//                                                  @Field("orderNo") String orderNo);
//
//
//
//    //结束视频订单
//    @FormUrlEncoded
//    @POST("pay/v2/end_video_order")
//    Observable<BaseResponse<EndVideoOrderInfo>> endVideoOrder(@Field("talkId") String talkId, @Field(
//            "orderNo") String orderNo);
//
//    //异常结束视频订单
//    @FormUrlEncoded
//    @POST("pay/v2/end_error_video_order")
//    Observable<BaseResponse<EndVideoOrderInfo>> endErrorVideoOrder(@Field("talkId") String talkId, @Field(
//            "orderNo") String orderNo);
//
//    /**
//     * 下面是主播音频聊天
//     */
//    //创建音频订单
//    @POST("pay/v2/create_voice_order")
//    Observable<BaseResponse<VideoOrderResultInfo>> createAudioOrder(@Body RequestBody videoOrder);
//
//    //拒接音频订单
//    @FormUrlEncoded
//    @POST("pay/v2/refuse_voice_order")
//    Observable<BaseResponse<JSONObject>> refuseVoiceOrder(@Field("talkId") String talkId, @Field("orderNo") String orderNo, @Field("type") int type);
//
//    //接通音频订单
//    @FormUrlEncoded
//    @POST("pay/v2/call_voice_order")
//    Observable<BaseResponse<Void>> callVoiceOrder(@Field("type") int type,
//                                                  @Field("talkId") String talkId,
//                                                  @Field("orderNo") String orderNo);
//
//    //结束音频订单
//    @FormUrlEncoded
//    @POST("pay/v2/end_voice_order")
//    Observable<BaseResponse<EndVideoOrderInfo>> endVoiceOrder(@Field("talkId") String talkId, @Field(
//            "orderNo") String orderNo);
//
//    //异常结束音频订单
//    @FormUrlEncoded
//    @POST("pay/v2/end_error_voice_order")
//    Observable<BaseResponse<EndVideoOrderInfo>> endErrorVoiceOrder(@Field("talkId") String talkId, @Field(
//            "orderNo") String orderNo);
//
//
//
//    //赠送蓝钻礼物
//    @FormUrlEncoded
//    @POST("pay/v1/send_gift_blue")
//    Observable<BaseResponse<JSONObject>> sendGiftBlue(@Field("giftNumber") int giftNumber, @Field("sendTalkId") String sendTalkId,
//                                                @Field("receiveTalkId") String receiveTalkId, @Field("orderNo") String orderNo,
//                                                @Field("giftCode") String giftCode, @Field("name") String name,
//                                                @Field("headPortrait") String headPortrait, @Field("receiveName") String receiveName,
//                                                @Field("receiveHeadPortrait") String receiveHeadPortrait, @Field("channel") int channel);
//
//    //获取蓝钻、粉钻收益
//    @FormUrlEncoded
//    @POST("pay/v1/query_accumulate_income_blue")
//    Observable<BaseResponse<JSONObject>> queryDiamondIncomeDetail(@Field("talkId") String talkId, @Field("id") long id,
//                                                                  @Field("pageSize") int pageSize, @Field("type") int type);
//
//    //获取蓝钻、粉钻收益，第二版
//    @FormUrlEncoded
//    @POST("pay/v1/query_income_details")
//    Observable<BaseResponse<IncomeTotalsWrapInfo>> queryIncomeDetails(@Field("chatType") int chatType,
//                                                                      @Field("diaType") int diaType,
//                                                                      @Field("pageSize") int pageSize,
//                                                                      @Field("pageNum") int pageNum,
//                                                                      @Field("beginTime") long beginTime,
//                                                                      @Field("endTime") long endTime,
//                                                                      @Field("talkId") String talkId);
//
//    //获取蓝钻、粉钻消费明细，第二版
//    @FormUrlEncoded
//    @POST("pay/v1/query_consume_details")
//    Observable<BaseResponse<ConsumeTotalsWrapInfo>> queryConsumeDetails(
//                                                                      @Field("diaType") int diaType,
//                                                                      @Field("chatType") int chatType,//1订单 2礼物
//                                                                      @Field("pageSize") int pageSize,
//                                                                      @Field("pageNum") int pageNum,
//                                                                      @Field("beginTime") long beginTime,
//                                                                      @Field("endTime") long endTime,
//                                                                      @Field("talkId") String talkId);
//
//    //查询主播视频功能开关列表
//    @FormUrlEncoded
//    @POST("services/v1/get_function_switch")
//    Observable<BaseResponse<JSONObject>> getFunctionSwitch(@Field("channelPackCode") String channelPackCode,
//                                                           @Field("versionCode") String versionCode);
//
//
//
//    //查询关注的人
//    @GET("services/v1/query_concern_ids")
//    Observable<BaseResponse<JSONObject>> getFollowIds(@Query("talkId") String talkId);
//
//    //查询敏感词列表
//    @GET("services/v1/get_sensitive_words")
//    Observable<BaseResponse<JSONObject>> getSensitiveWords();
//
//    //查询接单模式
//    @FormUrlEncoded
//    @POST("services/v1/get_order_status")
//    Observable<BaseResponse<JSONObject>> getOrderStatus(@Field("talkId") String talkId);
//
//    //设置接单模式
//    @FormUrlEncoded
//    @POST("services/v1/set_order_status")
//    Observable<BaseResponse<Void>> setOrderStatus(@Field("talkId") String talkId, @Field("status") int status);
//
//
//    //申述
//    @FormUrlEncoded
//    @POST("services/v1/user_state")
//    Observable<BaseResponse<Void>> apppeal(@Field("talkId") String talkId, @Field("reason") String reason);
//
//
//    /**
//     * 插入表情
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/insert_emoji")
//    Observable<BaseResponse<JSONObject>> insertEmoji(
//            @Field("emojiUrl") String emojiUrl,
//            @Field("type") int type,
//            @Field("sort") int sort,
//            @Field("talkId") String talkId,
//            @Field("width") int width,
//            @Field("height") int height
//    );
//
//    /**
//     * 查询表情
//     */
//    @GET("/services/v1/query_emojis")
//    Observable<BaseResponse<JSONObject>> queryEmojis(@Query("talkId") String talkId,
//                                               @Query("pageSize") int  pageSize,
//                                               @Query("pageNum") int  pageNum
//    );
//
//    /**
//     * 删除表情包
//     */
//    @GET("/services/v1/delete_emojis")
//    Observable<BaseResponse<Void>> deleteEmojis(@Query("talkId") String talkId,
//                                                     @Query("ids") String  ids
//    );
//    /**
//     * 置顶表情包
//     */
//    @GET("/services/v1/sort_emojis")
//    Observable<BaseResponse<Void>> sortEmojis(@Query("talkId") String talkId,
//                                                @Query("ids") String  ids
//    );
//
//
//    /**
//     * 查询注册状态
//     string	country	国籍
//     string	ip	ip
//     int	type	查询类型 1查询手机号注册状态 2查询ip封禁状态
//     string	talkId	用户id
//     string	headPortrait	头像
//     string	name	昵称
//     int	sex	性别
//     string	birthday	出生年月
//     * response
//     *
//     * 数据类型	属性名称	标注
//     * int	status	1正常注册 2不能用手机号注册 3不能注册
//     */
//    @GET("/services/v1/query_login_status")
//    Observable<BaseResponse<JSONObject>> queryRegisterStatus(@Query("country") String country,
//                                                              @Query("ip") String  ip,
//                                                               @Query("type") int  type,
//                                                              @Query("talkId") String  talkId,
//                                                              @Query("headPortrait") String  headPortrait,
//                                                             @Query("name") String  name,
//                                                             @Query("sex") int  sex,
//                                                             @Query("birthday") String  birthday
//    );
//
//
//    /**
//     * 获取文字聊天订单信息
//     */
//    @GET("/pay/v2/query_chat_order")
//    Observable<BaseResponse<JSONObject>> queryChatOrder(@Query("talkId") String talkId,
//                                              @Query("anchorTalkId") String  anchorTalkId
//    );
//
//    /**
//     * 创建文字聊天订单
//     * @param wordChatOrderInfo
//     * @return
//     */
//    @POST("/pay/v2/create_one_chat_order")
//    Observable<BaseResponse<String>> createChatOrder(@Body WordChatOrderInfo wordChatOrderInfo);
//
//    /**
//     * 续费文字聊天订单
//     * @param wordChatOrderInfo
//     * @return
//     */
//    @POST("/pay/v2/renew_chat_order")
//    Observable<BaseResponse<String>> renewChatOrder(@Body WordChatOrderInfo wordChatOrderInfo);
//
//    /**
//     * 查询好友足迹
//     */
//    @GET("/services/v6/query_user_footprint_list")
//    Observable<BaseResponse<JSONObject>> queryUserFootprintList(@Query("talkId") String talkId
//    );
//
//    /**
//     * 查询好友足迹
//     */
//    @GET("/services/v1/update_push_num")
//    Observable<BaseResponse<JSONObject>> updatePushNum(@Query("id") Long id
//    );
//
//
//    /**
//     * String	talkId	用户id
//     * String	country	CN
//     * int	systemType	系统
//     * int	sex	2代表全部
//     * int	userType	用户类型
//     * String	channel	渠道
//     */
//    @GET("/pay/v1/query_pay_activity")
//    Observable<BaseResponse<JSONObject>> queryPayActivity(
//            @Query("talkId") String talkId
//            ,@Query("country") String country
//            ,@Query("systemType") String systemType
//            ,@Query("sex") int sex
//            ,@Query("userType") int userType
//            ,@Query("channel") String channel
//    );
//
//    /**
//     数据类型	属性名称	标注
//     String	talkId	用户id
//     String	country	CN
//     int	systemType	系统
//     int	sex	2代表全部
//     int	userType	用户类型
//     String	channel	渠道
//     */
//    @GET("/pay/v1/query_lottery_activity")
//    Observable<BaseResponse<JSONObject>> queryLotteryActivity(
//            @Query("talkId") String talkId
//            ,@Query("country") String country
//            ,@Query("systemType") String systemType
//            ,@Query("sex") int sex
//            ,@Query("userType") int userType
//            ,@Query("channel") String channel
//    );
//
//    /**
//     * String	talkId	用户id
//     * String	name	用户昵称
//     * String	phone	联系方式
//     * String	country	国籍
//     * int	sex	性别
//     * @param talkId
//     * @return
//     */
//    @GET("/pay/v1/get_lottery")
//    Observable<BaseResponse<JSONObject>> getLottery(
//            @Query("talkId") String talkId,
//            @Query("name") String name,
//            @Query("phone") String phone,
//            @Query("country") String country,
//            @Query("sex") int sex
//    );
//
//    /**
//     String	talkId	用户id 查询所有的 历史 此id不传
//     int	pageSize	页数
//     int	pageNum	页码
//     */
//    @GET("/pay/v1/get_lottery_record")
//    Observable<BaseResponse<JSONObject>> getLotteryRecord(
//            @Query("talkId") String talkId
//            ,@Query("pageSize") int  pageSize
//            ,@Query("pageNum") int pageNum
//    );
//
//    /**
//     String	talkId	用户id
//     String	actionType	动作 1首次登陆 2点赞 3分享 4 充值
//     int	status	1 未使用 2已使用
//     */
//    @GET("/pay/v1/insert_lotteru_action")
//    Observable<BaseResponse<JSONObject>> insertLotteruAction(
//            @Query("talkId") String talkId
//            ,@Query("actionType") int  actionType
//            ,@Query("status") int status
//    );
//
//
//    /**
//     String	talkId	用户id
//     long	id	中奖纪录ID
//     */
//    @GET("/pay/v1/update_send_status")
//    Observable<BaseResponse<JSONObject>> updateSendStatus(
//            @Query("talkId") String talkId
//            ,@Query("id") int  id
//    );
//
//
//    /**
//     String	talkId	*用户ID
//     int	sex	*性别：[0,1]
//     int	taskType	*任务类型:[1新人礼包，2蓝钻礼包，3翻译礼包]
//
//     response
//     数据类型	属性名称	标注
//     //状态：-7参数存在空，-8参数值错误，-2没有登录，-1系统出错，1当前已有任务 并且 正在做，2当前任务已经领取过并且已经做完了，3当前任务不存在或已关闭，0成功
//
//     */
//
//    @POST("/pay/v1/add_user_gift_task")
//    Observable<BaseResponse<JSONObject>> addUserGiftTask(@Body Map map);
//
//
//    /**
//     String	talkId	用户id
//
//     返回
//     数据类型	属性名称	标注
//     list	giftTask	任务组
//     list	obtain	当前礼包已获得奖励组合JSON:[{type:1 粉钻，2VIP，5翻译包，10蓝钻,100勋章,number:数量,code:产品code(勋章code：10001青铜勋章，10002白银勋章，10003黄金勋章)}]
//     int	taskDayNum	任务天数
//     int	dayNum	当前礼包已达成天数
//     int	taskType	任务类型
//     */
//    @GET("/pay/v1/query_user_gift_task")
//    Observable<BaseResponse<JSONObject>> queryUserGiftTask(
//            @Query("talkId") String talkId
//    );
//
//    /**
//     * String	talkId	*用户ID
//     * String	taskCode	*任务code
//     */
//    @POST("/pay/v1/finish_user_gift_task")
//    Observable<BaseResponse<JSONObject>> finishUserGiftTask(
//           @Body Map map
//    );
//
//    /**
//     * String	talkId	*用户ID
//     * int	sex	*性别：[0,1]
//     * @param talkId
//     * @return
//     */
//
//    @GET("/pay/v1/query_gift_task_list")
//    Observable<BaseResponse<JSONObject>> queryGiftTaskList(
//            @Query("talkId") String talkId,
//             @Query("sex") int sex
//    );
//
//    /**
//     * String	talkId	用户id
//     * String	date	月份 例如 2018-10
//     * int	pageSize	页数
//     * int	pageNum	页码
//     * @return
//     */
//    @GET("/pay/v1/query_user_gift_task_reward_list")
//    Observable<BaseResponse<JSONObject>> queryUserGiftTaskRewardList(
//            @Query("pageSize") int pageSize,
//            @Query("pageNum") int pageNum,
//             @Query("date") String date,
//             @Query("talkId") String talkId
//
//    );
//
//    /**
//     string	orderNo	订单编
//     string	data	数据信息
//     string	talkId	ID
//     * @return
//     */
//    @GET("/action/v1/insert_pay_log")
//    Observable<BaseResponse<Void>> insertPayLog(
//            @Query("orderNo") String orderNo,
//            @Query("data") String data,
//            @Query("talkId") String talkId
//
//    );
//
//
//    @POST("/services/v6/get_login_phone")
//    @FormUrlEncoded
//    Observable<BaseResponse<JSONObject>> getLoginPhone(
//            @Field("token") String token);
//
//
//    @POST("/services/v6/onekey_login")
//    Observable<BaseResponse<JSONObject>> onekeyLogin(
//            @Body Map<String,String> body
//            );
//
//    @POST("http://vop.baidu.com/server_api")
//    Observable<JSONObject> baiduAsr(
//            @Body Map<String,Object> body
//    );
////    http://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id=6EVgsknywFX0IFnrzPjCGIGz&client_secret=Uezg2X4Ij0KTW8SFnagu6GbGOTUlM4H3
//    @GET
//    Observable<BaseResponse<JSONObject>> refreshBaiduAsrToken(
//            @Url String url
//    );
//
//
//    /**
//     * 用户认证主播
//     * 数据类型	属性名称	标注
//     * string	talkId	用户ID
//     * string	name	昵称
//     * string	headPortrait
//     * int	sex	性别
//     * String	papersPicture	证件图
//     * string	identPicture	认证图
//     * string	country	国籍
//     */
//    @FormUrlEncoded
//    @POST("/services/v1/insert_anchor_ident")
//    Observable<BaseResponse<JSONObject>> insertAnchorIdent(
//            @Field("talkId") String talkId,
//            @Field("name") String name,
//            @Field("headPortrait") String headPortrait,
//            @Field("sex") int sex,
//            @Field("papersPicture") String papersPicture,
//            @Field("identPicture") String identPicture,
//            @Field("country") String country
//    );
//
//    @GET("/services/v1/query_anchor_ident")
//    Observable<BaseResponse<JSONObject>> queryAnchorIdent(
//            @Query("talkId") String talkId
//    );
//
//
//    @GET("/services/v1/delete_anchor_ident")
//    Observable<BaseResponse<JSONObject>> deleteAnchorIdent(
//            @Query("talkId") String talkId,
//            @Query("country") String country,
//            @Query("language") String language
//    );
//
//
//    @GET("/services/v1/update_user_status")
//    Observable<BaseResponse<Void>> updateUserStatus(
//            @Query("talkId") String talkId,
//            @Query("country") String country
//    );
//
//    /**
//     * string	language	语言
//     * string	channelPackCode	渠道
//     * string	versionCode	版本
//     * @param language
//     * @return
//     */
//    @GET("/services/v1/query_anchor_rule")
//    Observable<BaseResponse<JSONObject>> queryAnchorRule(
//            @Query("language") String language,
//            @Query("channelPackCode") String channelPackCode,
//            @Query("versionCode") String versionCode
//    );
//
//    @POST("/services/v1/upload_image_bytes")
//    Observable<JSONObject> uploadFile(
//              @Body RequestBody body
//    );
//
//
//    @POST("/services/v1/create_upload_sign")
//    @FormUrlEncoded
//    Observable<JSONObject> createUploadSign(
//            @Field("bucket") String bucket,
//            @Field("cosPath") String cosPath
//    );
//
//
//    /**
//     * 修改聊主状态
//     * int	userType	1普通用户 2聊主
//     * string	talkId	talkId
//     * string	country	国籍
//     * @return
//     */
//    @GET("/services/v1/update_anchor_status")
//    Observable<BaseResponse<JSONObject>> updateAnchorStatus(
//            @Query("userType") int userType,
//            @Query("talkId") String talkId,
//            @Query("country") String country
//    );
//
//
//    @Headers({RetrofitManager.CONNECT_TIMEOUT + ":30000", RetrofitManager.READ_TIMEOUT + ":30000"
//            , RetrofitManager.WRITE_TIMEOUT + ":30000"})
//    @POST("http://81.68.219.177:8080/services/v1/upload")
//    Observable<BaseResponse<JSONObject>> uploadFiles(@Header("suffix") String suffix,@Header("index") int index,
//            @Body MultipartBody multipartBody);
//
//    @GET("/pay/v1/check_gift")
//    Observable<BaseResponse<JSONObject>> checkGift(
//            @Query("sendTalkId") String sendTalkId,
//            @Query("receiveTalkId") String receiveTalkId,
//            @Query("id") int id,
//            @Query("type") int type
//
//    );
//
//
//
//    /**
//     * 闪聊
//     */
//    @GET("/services/v1/query_fast_chat")
//    Observable<BaseResponse<JSONObject>> queryFastChat(
//            @Query("talkId") String talkId,
//            @Query("country") String country,
//            @Query("sex") int  sex
//            );


}


