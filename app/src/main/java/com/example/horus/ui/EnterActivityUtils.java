package com.example.horus.ui;

import android.content.Context;
import android.content.Intent;

import com.example.horus.ui.main.MainActivity;
import com.example.horus.utils.ActivityUtils;

/**
 * 启动App的流程比较复杂
 * 统一跳转至该类进行分发处理
 * <p>
 * 目前流程分为两大类
 * 1.Splash后的流程
 * 2.注册/登录后的信息完善流程
 * <p>
 * 相关页面统一跳转至EnterActivity即可
 */
public class EnterActivityUtils {

    public static boolean isWillLogIn = false;

    public static void toNextStep(Context context) {
//        ActivityUtils.finishAllActivity();// 先结束之前的所有Activity
        if (ActivityUtils.currentActivity() != null){
            context = ActivityUtils.currentActivity();
        }
        switchActivity(context);
    }



    /**
     * 更新 2018/10/12 ：Bind页入口改到三方登录后了
     * <p>
     * 启动屏后有三条主路径
     * 1.未显示Feature时展示Feature页 //已去除
     * 2.用户关键信息缺失时展示Login页
     * 3.用户信息正确时进入副流程
     * 3.1 用户手机号/邮箱缺失时进入绑定Bind页
     * 3.2 用户个人资料缺失时进入完善信息Complete页
     * 3.3 用户信息正常，进入Main
     */
    private static void switchActivity(Context context) {
        /** 去掉Feature页
        int featureVersion = Config.getFeatureVersion();
        //小于最新版本，展示特性
        if (featureVersion != BuildConfig.VERSION_CODE && featureVersion < Constant.FEATURE_VERSION) {
            context.startActivity(new Intent(context, FeatureActivity.class));
            return;
        }
         */

//        //满足绑定手机号条件，去绑定手机号
//        if (UserUtil.checkShouldBindPhone()) {
//            SplashActivity.isOldUer = false;
//            if (!OneKeyLoginManager.getSharedInstance().isOverSeasChannel()){
//                OneKeyBindPhoneManager.getSharedInstance().showOneKeyBindPhoneUI(new OneKeyBindPhoneManager.onShowLisener() {
//                    @Override
//                    public void onShowSuccess() {
//
//                    }
//
//                    @Override
//                    public void onNotSupport() {
//                        BindActivity.start(context, false);
//                    }
//
//                    @Override
//                    public void onBindOtherPhoneClick() {
//                        BindActivity.start(context, true);
//                    }
//                });
//            }else {
//                BindActivity.start(context, false);
//            }
//
//            return;
//        }
//
//        //用户关键信息没有，去登录
//       int status = Config.getUsePhoneOneKeyLoginStatus();
//        if (!UserUtil.checkUserValid() || status == 2) {//可能会被重复调用
//            goToLoginUI(context,status);
//            return;
//        }
//        //个人资料不完整，去完善个人资料
//        if (UserUtil.checkShouldCompleteInfo()) {
//            CompleteActivity.start(context,null);
//            SplashActivity.isOldUer = false;
////            ActivityUtils.finishAllActivity();// 先结束之前的所有Activity
//            return;
//        }
//
//        //头像不完整，去完善个人资料
//        if (UserUtil.checkShouldCompleteHeadPortraitInfo()) {
//            UploadAvatarActivity.start(context, UploadAvatarActivity.RESULT_FLAG_GO_COMPLETE);
//            SplashActivity.isOldUer = false;
//            ActivityUtils.finishAllActivity();// 先结束之前的所有Activity
//            return;
//        }
        //均不满足，跳主页
        context.startActivity(new Intent(context, MainActivity.class));
        if (ActivityUtils.currentActivity() != null){
            ActivityUtils.currentActivity().overridePendingTransition(0,0);
        }
    }


    private static void  goToLoginUI(Context context,int status){
            Intent intent = new Intent();
//            if (status == 2 ){
//                intent.setClass(context, OneKeyLoginActivity.class);
//                intent.putExtra(LoginUmengActivity.INTENT_TYPE_KEY,LoginUmengActivity.LOGIN_AUTO_LOGIN);
//            }else {
//                intent.setClass(context,LoginUmengActivity.class);
//            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if (status != 2){
                ActivityUtils.currentActivity().overridePendingTransition(0,0);
            }
            ActivityUtils.finishAllActivity();// 先结束之前的所有Activity
    }

}
