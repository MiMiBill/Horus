package com.example.horus.utils;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import com.example.baselib.utils.LogUtil;

import java.lang.ref.WeakReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by lognyun on 2017/5/15 0015.
 *
 * 输入管理
 */

public class InputUtil {
    private static final String TAG = InputUtil.class.getSimpleName();

    public static void setIDCardInput(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18), new InputFilter.AllCaps()});
        // 下面这种写法在Android原生键盘时不能输入英文
         String digits = "xX0123456789";
         editText.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    public static void setInputNumberFilter(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public static String getStringWithoutBlank(String src) {
        if (src == null) {
            return null;
        }
        return src.replace(" ", "");
    }

    private static String addBlank(String src) {
        if (src == null) {
            src = "";
        }
        src = src.replace(" ", "");
        String result = "";
        int length = src.length();
        for (int i = 0; i < length; i++) {
            if ((i+1)%4 == 1 && i > 0) {
                result += " ";
            }
            result += src.charAt(i);
        }
        return result;
    }

    /**
     * 为银行卡号添加空格
     * @param editText
     */
    public static void addBankcardBlank(final EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(23)});
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.removeTextChangedListener(this);
                int sel = editText.getSelectionStart();
                LogUtil.e(TAG, "Selection:" + sel);
                String result = addBlank(s.toString());
                // 增加字符后停在5,10,15,20位，需补空格，光标后移一位
                if (count > 0 && before == 0 && (sel%5 == 0) && sel > 0) {
                    sel++;
                }
                // 删除字符后
                else if (count == 0 && before > 0 && sel > 0) {
                    // 停在5,10,15,20位（删后最后一位是空格），需再去空格
                    if (sel % 5 == 0) {
                        sel--;
                        result = addBlank(result);
                    }
                    // 停在4,9,14,19位（删的最后一位是空格），需再往前删一位
                    else if ((sel+1) % 5 == 0) {
                        sel--;
                        result = result.substring(0, sel) + result.substring(sel+1);
                        result = addBlank(result);
                    }
                }
                editText.setText(result);
                editText.setSelection(sel);
                editText.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static void hideIDCardNum(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.length() > 6) {
            String result = text.substring(0,6) + " **** " + text.substring(text.length() - 4);
            editText.setText(result);
            return;
        }
        editText.setText(text);
    }

    public static void hideBankcard(EditText editText) {
        String text = editText.getText().toString().trim();
        if (text.length() > 4) {
            String result = text.substring(0,4) + " **** **** " + text.substring(text.length() - 4);
            editText.setText(result);
            return;
        }
        editText.setText(text);
    }

    public static void setChineseFilter(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[]{chineseFilter, new InputFilter.LengthFilter(maxLength)});
    }

    public static void setEmojiFilter(EditText editText, int maxLength) {
        editText.setFilters(new InputFilter[]{emojiFilter, new InputFilter.LengthFilter(maxLength)});
    }

    public static void setChineseFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{chineseFilter});
    }

    public static void setEmojiFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{emojiFilter});
    }

    public static void setEmojiFilterLen(EditText editText) {
        editText.setFilters(new InputFilter[]{emojiFilterLen});
    }

    /**
     * 屏蔽非CJK输入
     */
    public static final InputFilter chineseFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!isChinese(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };

    /**
     * 屏蔽emoji输入
     */
    public static final InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (hasEmoji(source.toString())) {
                return cleanEmoji(source.toString());
            }
            return null;
        }
    };

    /**
     * 限制长度
     */
    public static final InputFilter emojiFilterLen = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (dend > 14)
                return "";
            if (hasEmoji(source.toString())) {
                return cleanEmoji(source.toString());
            }
            return null;
        }
    };

    /**
     * 判断是否是中文字符
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ) {
            return true;
        }
        return false;
    }

    private static final String MATCH_EMOJI="[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";

    /**
     * 删除句子中的表情符号
     */
    public static String cleanEmoji(String string) {
        Pattern p = Pattern.compile(MATCH_EMOJI,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.replaceAll("");
    }

    public static boolean hasEmoji(String string) {
        Pattern p = Pattern.compile(MATCH_EMOJI,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }



    public static String getMaxEmxLengthText(String input,int maxCount){
        return getMaxEmxLengthText(input,maxCount,false);
    }
    /**
     * 取指定ems长度的 字符串
     * 中日韩占两格 为 2
     * 阿拉伯 占0.4
     */
    public static String getMaxEmxLengthText(String input,int maxCount,boolean appendEnd){
        if(TextUtils.isEmpty(input)){
            return "";
        }
        boolean over=false;

        StringBuilder stringBuilder=new StringBuilder();
        float valueLength=0;
        String unit2 = "[\u4e00-\u9fa5]|[\uac00-\ud7ff]|[\u0800-\u4e00]";
        String unitArabia ="[\\p{InArabic}]";
        for (int i = 0; i < input.length(); i++) {
            String temp = input.substring(i, i + 1);
            if (temp.matches(unit2)) {
                valueLength += 2;
            } else if(temp.matches(unitArabia)){
                valueLength += 0.4f;
            }else {
                valueLength += 1;

            }

            if(valueLength<=maxCount){
                stringBuilder.append(temp);
            }else {
                over=true;
                break;
            }

        }

        return stringBuilder.toString()+((over&&appendEnd)?"...":"");

    }


    public static boolean checkOverLength(String input,int maxCount){
        float valueLength=0;
        String unit2 = "[\u4e00-\u9fa5]|[\uac00-\ud7ff]|[\u0800-\u4e00]";
        String unitArabia ="[\\p{InArabic}]";
        for (int i = 0; i < input.length(); i++) {
            String temp = input.substring(i, i + 1);
            if (temp.matches(unit2)) {
                valueLength += 2;
            } else if(temp.matches(unitArabia)){
                valueLength += 0.4f;
            }else {
                valueLength += 1;

            }

            if(valueLength<=maxCount){
            }else {
                return true;
            }

        }
        return false;
    }

    /**
     * 昵称长度检测
     */
    public static class NickNameCheckTextWatch implements TextWatcher {
        private WeakReference<EditText> mEdt;
        boolean filled=false;
        public static final  int NICK_NAME_MAX_EMS_COUNT=20;

        public NickNameCheckTextWatch(EditText edt) {
            mEdt=new WeakReference<>(edt);
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override public void afterTextChanged(Editable s) {
            if(!filled){
                EditText mEdtNickname=mEdt.get();
                if(mEdtNickname==null){
                    return;
                }


                String text=mEdtNickname.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    filled=true;
                    mEdtNickname.setText(InputUtil.getMaxEmxLengthText(text,NICK_NAME_MAX_EMS_COUNT));
                    mEdtNickname.setSelection(mEdtNickname.getText().length());
                }
            }else {
                filled=false;
            }
        }
    }
}
