package com.example.horus.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * des: 剪贴板工具类
 * author: lognyun
 * date: 2018/10/23 09:47
 */
public class ClipboardUtils {


    public static void copyText(Context context, String text,String tip){
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager)context.getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText(tip, text);
        if (myClipboard != null) {
            myClipboard.setPrimaryClip(myClip);
        }
    }
}
