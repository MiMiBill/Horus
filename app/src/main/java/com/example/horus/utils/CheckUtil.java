package com.example.horus.utils;

import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.app.MyApp;
import com.example.horus.web.WebActivity;
import com.example.horus.widget.ClickableMovementMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 用于校验手机号格式、密码长度、验证码长度等
 */
public class CheckUtil {


    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    public static boolean checkIdCard(String cardNum) {
        if (isIDCard(cardNum)) {
            return true;
        } else {
            ToastUtil.showShortMessage("身份证号错误");
            return false;
        }
    }

    // 校验手机号，true为合格
    public static boolean checkPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            ToastUtil.showShortMessage("请输入手机号");
            return false;
        } else if (phone.length() < 11) {
            ToastUtil.showShortMessage("手机号错误");
            return false;
        } else if (!isMobile(phone)) {
            ToastUtil.showShortMessage("手机号错误");
            return false;
        }
        return true;
    }

    //检测是否有链接

    public static String[] linkString(String str){
        if (TextUtils.isEmpty(str))return new String[1];
//        Matcher m = Pattern.compile("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
//                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)").matcher(str);
        Matcher m = Pattern.compile("((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,3})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)").matcher(str.toLowerCase());
        String[] url = new String[str.length()/5];
        int count = 0;
        while(m.find()){
            url[count] = m.group();
            count++;
        }
        return url;

    }

    public static void convertToLinkText(boolean isSelf,String contentStr,TextView textView ){
        String[] urls = linkString(contentStr);
        SpannableString spannableString = new SpannableString(contentStr);
        String preStr = "";
        String leftStr = contentStr;
        for (int index = 0;urls != null && index < urls.length ; index ++ ){
            final String url = urls[index];
            if (TextUtils.isEmpty(url)){
                continue;
            }
            int linkStart = preStr.length() + leftStr.toLowerCase().indexOf(url.toLowerCase());
            leftStr = contentStr.substring(linkStart + url.length());
            preStr = contentStr.substring(0,linkStart + url.length());

            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    if (isSelf){
                        ds.setColor(ContextCompat.getColor(MyApp.getInstance(),R.color.white));
                    }else {
                        ds.setColor(ContextCompat.getColor(MyApp.getInstance(),R.color.blue_nomal));
                    }
                    ds.setUnderlineText(true);
                }

                @Override
                public void onClick(@NonNull View widget) {
                    //用户协议
                    String link = url;
                    if (link.startsWith("www.")){
                        link = "http://" + link;
                    }
                    WebActivity.start(ActivityUtils.currentActivity(), link, "");
                }
            }, linkStart, linkStart + url.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        textView.setMovementMethod(ClickableMovementMethod.getInstance());
        textView.setText(spannableString);
    }


    // 校验邮箱，true为合格
    public static boolean checkEmail(String email) {
        if (email == null || email.isEmpty()) {
            ToastUtil.showShortMessage(R.string.recharge_result_email_hint);
            return false;
        } else if (!Pattern.matches(REGEX_EMAIL, email)) {
            ToastUtil.showShortMessage(R.string.recharge_result_email_not_qualified);
            return false;
        }
        return true;
    }

    public static boolean checkPhone(EditText editText) {
        return checkPhone(editText.getText().toString().trim());
    }

    // 校验验证码格式
    public static boolean checkVerify(String verify) {
        if (verify == null || verify.isEmpty()) {
            ToastUtil.showShortMessage("请输入验证码");
            return false;
        } else if (verify.length() != 6) {
            ToastUtil.showShortMessage("验证码错误");
            return false;
        }
        return true;
    }

    public static boolean checkVerify(EditText editText) {
        return checkVerify(editText.getText().toString().trim());
    }

    // 校验密码格式
    public static boolean checkPassword(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            ToastUtil.showShortMessage("请输入密码");
            return false;
        } else if (!isPassword(pwd)) {
            ToastUtil.showShortMessage("密码为6-16位大小写字母和数字");
            return false;
        }
        return true;
    }

    public static boolean checkPassword(EditText editText) {
        return checkPassword(editText.getText().toString().trim());
    }

    // 校验密码格式
    public static boolean checkOldPwd(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            ToastUtil.showShortMessage("请输入初始密码");
            return false;
        } else if (!isPassword(pwd)) {
            ToastUtil.showShortMessage("初始密码为6-16位大小写字母和数字");
            return false;
        }
        return true;
    }

    public static boolean checkOldPwd(EditText editText) {
        return checkOldPwd(editText.getText().toString().trim());
    }

    // 校验密码格式
    public static boolean checkNewPwd(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            ToastUtil.showShortMessage("请输入新密码");
            return false;
        } else if (!isPassword(pwd)) {
            ToastUtil.showShortMessage("新密码为6-16位大小写字母和数字");
            return false;
        }
        return true;
    }

    public static boolean checkNewPwd(EditText editText) {
        return checkNewPwd(editText.getText().toString().trim());
    }

    // 校验确认密码的格式
    public static boolean checkPwdTwice(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            ToastUtil.showShortMessage("请输入确认密码");
            return false;
        } else if (!isPassword(pwd)) {
            ToastUtil.showShortMessage("确认密码为6-16位大小写字母和数字");
            return false;
        }
        return true;
    }

    public static boolean checkPwdTwice(EditText editText) {
        return checkPwdTwice(editText.getText().toString().trim());
    }

    // 校验密码是否一致
    public static boolean checkPwdSame(String pwd1, String pwd2) {
        if (pwd2.equals(pwd1)) {
            return true;
        } else {
            // ToastUtil.showShortMessage(R.string.error_different_password);
            return false;
        }
    }

    public static boolean checkPwdSame(EditText editText1, EditText editText2) {
        return checkPwdSame(editText1.getText().toString().trim(), editText2.getText().toString().trim());
    }

    // 校验设置支付密码格式
    public static boolean checkSetPay(String pwd, int time) {
        if (pwd == null || pwd.isEmpty()) {
            if (time == 1) {
                ToastUtil.showShortMessage("请输入支付密码");
            } else if (time == 2) {
                ToastUtil.showShortMessage("请输入确认密码");
            }
            return false;
        } else if (!isPwdPay(pwd)) {
            if (time == 1) {
                ToastUtil.showShortMessage("支付密码为6位数字");
            } else if (time == 2) {
                ToastUtil.showShortMessage("确认密码为6位数字");
            }
            return false;
        }
        return true;
    }

    // 校验更改支付密码格式
    public static boolean checkPayPwd(String pwd, int time) {
        if (pwd == null || pwd.isEmpty()) {
            if (time == 1) {
                ToastUtil.showShortMessage("请输入初始密码");
            } else if (time == 2) {
                ToastUtil.showShortMessage("请输入新密码");
            } else {
                ToastUtil.showShortMessage("请再次输入新密码");
            }
            return false;
        } else if (!isPwdPay(pwd)) {
            if (time == 1) {
                ToastUtil.showShortMessage("初始支付密码为6位数字");
            } else if (time == 2) {
                ToastUtil.showShortMessage("新支付密码为6位数字");
            } else {
                ToastUtil.showShortMessage("确认密码为6位数字");
            }
            return false;
        }
        return true;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isMobile(TextView textView) {
        return isMobile(textView.getText().toString().trim());
    }

    /**
     * 验证密码
     *
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isPassword(TextView textView) {
        return isPassword(textView.getText().toString().trim());
    }

    /**
     * 验证支付密码
     *
     * @param str
     * @return
     */
    public static boolean isPwdPay(String str) {
        Pattern p = Pattern.compile("^[0-9]{6}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断字符串长度
     *
     * @param str
     * @param minLen
     * @param maxLen
     * @return
     */
    public static boolean length(String str, int minLen, int maxLen) {
        return (str.length() >= minLen && str.length() <= maxLen);
    }

    public static boolean length(TextView textView, int minLen, int maxLen) {
        return length(textView.getText().toString().trim(), minLen, maxLen);
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param textView1
     * @param textView2
     * @return
     */
    public static boolean isSame(TextView textView1, TextView textView2) {
        String str1 = textView1.getText().toString().trim();
        String str2 = textView2.getText().toString().trim();
        return str2.equals(str1);
    }

    public static boolean isIDCard(String str) {
        if (!TextUtils.isEmpty(str)) {
            int correct = new IdCardUtil(str).isCorrect();
            if (correct == 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否所有的输入框都是有值的
     */
    public static boolean isAllNotEmpty(TextView... textViews) {
        boolean notEmpty = true;
        for (TextView textView : textViews) {
            if (TextUtils.isEmpty(textView.getText().toString().trim())) {
                notEmpty = false;
                break;
            }

        }


        return notEmpty;
    }
}
