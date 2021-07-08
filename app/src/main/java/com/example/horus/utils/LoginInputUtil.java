package com.example.horus.utils;

import android.text.TextUtils;
import android.widget.EditText;

import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.app.Config;
import com.example.horus.app.MyApp;
import com.example.horus.data.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lognyun on 2018/11/16 14:51:02
 * <p>
 * 历史帐号记录/自动填写
 * <p>
 * 主要用于注册时自动记录
 * 登录/忘记密码时自动填写
 */
public class LoginInputUtil {

    private static final boolean showHisAcc = true;

    /**
     * 空串校验
     */
    public static boolean isEmpty(String text) {
        return (text == null || text.isEmpty());
    }

    /**
     * 历史输入帐号记录
     *
     * @param account 当前帐号
     */
    public static void saveHistoryAccount(String account) {
        if (!showHisAcc) {
            return;
        }
        if (!TextUtils.isEmpty(account)) {
            Config.setLoginAccount(account);
        }
    }

    /**
     * 历史输入区号
     *
     * @param region 当前帐号
     */
    public static void saveHistoryRegion(String region) {
        if (!showHisAcc) {
            return;
        }
        if (!TextUtils.isEmpty(region)) {
            Config.setLoginRegin(region);
        }
    }

    /**
     * 历史输入区号
     */
    public static String getHistoryRegion() {
        return Config.getLoginRegin();
    }

    /**
     * 历史输入帐号记录
     */
    public static void saveHistoryAccount(String phone, String email) {
        saveHistoryAccount(phone);
        saveHistoryAccount(email);
    }

    /**
     * 历史输入帐号显示
     *
     * @param editText 显示的文本
     */
    public static void showHistoryAccount(EditText editText) {
        if (!showHisAcc || editText == null) {
            return;
        }
        String historyAccount = Config.getLoginAccount();
        if (historyAccount != null && !historyAccount.isEmpty()) {
            if (historyAccount.length() > 11)return;
            editText.setText(historyAccount);
            editText.setSelection(historyAccount.length());
        }
    }

    public static String getHistoryAccount()
    {
        if (!showHisAcc) {
            return "";
        }
        String historyAccount = Config.getLoginAccount();
        if (historyAccount != null && !historyAccount.isEmpty()) {
            return historyAccount;
        }
        return "";
    }

    /**
     * 输入手机号/邮箱 仅判空
     *
     * @param account 帐号
     * @return 是否为空
     */
    public static boolean checkAccount(String account) {
        if (isEmpty(account)) {
            ToastUtil.showShortMessage(R.string.login_util_input_account);
            return false;
        }
        return true;
    }

    /**
     * 校验邮箱合法性
     *
     * @param email 邮箱
     * @return 是否可用
     */
    public static boolean checkEmail(String email,boolean isShowToast) {
        if (isEmpty(email)) {
            if (isShowToast){
                ToastUtil.showShortMessage(R.string.login_util_input_email);
            }
            return false;
        }
        if (!isEmail(email)) {
            if (isShowToast){
                ToastUtil.showShortMessage(R.string.login_util_email_invalid);
            }
            return false;
        }
        return true;
    }

    /**
     * 校验手机号合法性
     *
     * @param phone 手机号
     * @return 是否可用
     */
    public static boolean checkPhone(String phone, boolean isShowToast, String nationCode) {
        if (isEmpty(phone)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_util_input_phone);
            return false;
        }
        if (!isPhone(phone, nationCode)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_util_phone_invalid);
            return false;
        }
        return true;
    }

    /**
     * 校验密码合法性
     *
     * @param pwd 密码
     * @return 是否合法
     */
    public static boolean checkPassword(String pwd, boolean isShowToast) {
        if (isEmpty(pwd)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_input_password);
            return false;
        }
        if (!isPassword(pwd)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_util_password_rule);
            return false;
        }
        return true;
    }


    /**
     * 设置密码 校验旧密码/新密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 输入是否合法
     */
    public static boolean checkResetPwd(String oldPwd, String newPwd) {
        if (isEmpty(oldPwd)) {
            ToastUtil.showShortMessage(R.string.login_util_input_old_pwd);
            return false;
        }
        if (!isPassword(oldPwd)) {
            ToastUtil.showShortMessage(R.string.login_util_password_rule);
            return false;
        }
        if (isEmpty(newPwd)) {
            ToastUtil.showShortMessage(R.string.login_util_input_new_pwd);
            return false;
        }
        if (!isPassword(newPwd)) {
            ToastUtil.showShortMessage(R.string.login_util_password_rule);
            return false;
        }

        if (oldPwd.equals(newPwd)) {
            ToastUtil.showShortMessage(R.string.login_util_input_old_new_pwd_not_equals);
            return false;
        }
        return true;
    }

    /**
     * 校验验证码
     * 当前的验证码是6位数字，不确定是否会变更
     *
     * @param code
     * @return
     */
    public static boolean checkVerifyCode(String code, boolean isShowToast) {
        if (isEmpty(code)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_util_input_verify);
            return false;
        }
        if (!isVerifyCode(code)) {
            if (isShowToast)
                ToastUtil.showShortMessage(R.string.login_util_verify_invalid);
            return false;
        }
        return true;
    }

    /**
     * 网上关于邮箱的正则大部分都是错的
     * 这里只判断是否符合 *@*.*的规则，其他不管
     * "."的域名可能造成用冒号的IP后缀域名不可用，忽略这部分特殊人群
     *
     * @param email 邮箱
     * @return 符合
     */
    public static boolean isEmail(String email) {
        String[] atArr = email.split("@");
        if (atArr.length == 2) {
            int dotIndex = atArr[1].indexOf(".");
            if (dotIndex > 0 && dotIndex < atArr[1].length() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 限定了一个比较大的范围
     *
     * @param phone 手机号
     * @return 符合
     */
    private static boolean isPhone(String phone, String nationCode) {

        String regEx = "[0-9]{"+ 8 +","+ 11 +"}";
        if (!TextUtils.isEmpty(nationCode) && nationCode.equals("86")) //中国用户需手机号为11位
            regEx = "[0-9]{11}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 密码格式为6-16位数字/字母或符号，不允许有空格
     *
     * @param pwd 密码
     * @return 符合
     */
    private static boolean isPassword(String pwd) {
        if (pwd.length() >= 6 && pwd.length() <= 16) {
            return !pwd.contains(" ");
        }
        return false;
    }

    private static boolean isVerifyCode(String code) {
        String regEx = "[0-9]{6}";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(code);
        return m.matches();
    }

}
