package com.example.horus.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.horus.R;

/**
 * des: 透明主题的Dialog，后面最好改为Builder模式来新建Dialog
 * author: lognyun
 * date: 2019/9/12 15:20
 */
public class TransparentDialog extends Dialog {
    private Context mContext;
    private Builder mBuilder;

    public TransparentDialog(@NonNull Context context) {
        super(context, R.style.dialogTransparent); //透明style不可少
        mContext = context;
    }

    public TransparentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected TransparentDialog(@NonNull Context context, boolean cancelable,
                                @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mBuilder.contentView); //设置内容布局
        //设置Dialog尺寸
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //获取屏幕尺寸
        DisplayMetrics d = mContext.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * mBuilder.widthScale); //设置Dialog宽度默认屏幕宽度的90%
//        lp.dimAmount = 0.0f; //外围遮罩层透明度
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(mBuilder.gravity == -1 ? Gravity.CENTER : mBuilder.gravity);

        setCancelable(mBuilder.isCancelable);
    }

    public void setBuilder(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    public static class Builder {
        public float widthScale; //宽度占屏幕的比例
        public int gravity;
        public View contentView;
        public Context context;
        public boolean isCancelable;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setWidthScale(float widthScale){
            this.widthScale = widthScale;
            return this;
        }

        public Builder setGravity(int gravity){
            this.gravity = gravity;
            return this;
        }

        public Builder setContentView(View view){
            contentView = view;
            return this;
        }

        public Builder setCancelable(boolean isCancelable){
            this.isCancelable = isCancelable;
            return this;
        }

        public TransparentDialog create(){
            TransparentDialog dialog = new TransparentDialog(context);
            dialog.setBuilder(this);
            return  dialog;
        }

        public void show(){
            create().show();
        }
    }

}
