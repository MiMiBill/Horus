package com.example.horus.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.horus.R;
import com.example.horus.widget.dialog.CommonDialog;



public class DialogUtils {



    /**
     * 显示确认功能的dialog
     */
    public static void showConfirmDialog(Context context, String tip, CommonDialog.OnDialogOptionClickListener onClickListener) {
        new CommonDialog.Builder(context)
                .setTitle(tip)
                .setPositiveClickListener(onClickListener)
                .show();
    }

    /**
     * 显示确认功能的dialog
     */
    public static CommonDialog showConfirmDialog(Context context, String tip,
                                                 CommonDialog.OnDialogOptionClickListener onClickListener,
                                                 CommonDialog.OnDialogOptionClickListener onNegativeClickListener) {
        CommonDialog commonDialog = new CommonDialog.Builder(context)
                .setTitle(tip)
                .setPositiveClickListener(onClickListener)
                .setNegativeListener(onNegativeClickListener).create();
        commonDialog.show();

        return commonDialog;
    }









}
