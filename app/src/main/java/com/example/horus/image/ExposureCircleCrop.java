package com.example.horus.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.example.horus.R;

import java.security.MessageDigest;

/**
 * Created by Liao
 * Glide加载带边框的圆形 仅用于曝光购买弹窗的头像
 */
public class ExposureCircleCrop extends BitmapTransformation {
    private static final String ID = "com.zerophil.worldtalk.ExposureCircleCrop";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    // 白色内圈（覆盖在蓝色外圈上）
    private int mBorderColorWhite;
    private int mBorderWidthWhite;

    // 蓝色外圈
    private int mBorderColorBlue;
    private int mBorderWidthBlue;

    private Paint mPaintBorderWhite;
    private Paint mPaintBorderBlue;

    public ExposureCircleCrop(Context context) {
        mBorderColorBlue = context.getResources().getColor(R.color.chat_exposure_border_color);
        mBorderWidthBlue = context.getResources().getDimensionPixelOffset(R.dimen.chat_exposure_avatar_border_width_blue);

        mBorderColorWhite = context.getResources().getColor(R.color.white);
        mBorderWidthWhite= context.getResources().getDimensionPixelOffset(R.dimen.chat_exposure_avatar_border_width_white);

        mPaintBorderBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorderBlue.setStyle(Paint.Style.STROKE);
        mPaintBorderBlue.setStrokeWidth(mBorderWidthBlue);
        mPaintBorderBlue.setColor(mBorderColorBlue);

        mPaintBorderWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorderWhite.setStyle(Paint.Style.STROKE);
        mPaintBorderWhite.setStrokeWidth(mBorderWidthWhite);
        mPaintBorderWhite.setColor(mBorderColorWhite);
    }


    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);

        if (mPaintBorderWhite == null || mPaintBorderBlue == null) {
            return bitmap;
        }


        // 圆心
        float centerX = bitmap.getWidth() / 2.0f;
        float centerY = bitmap.getHeight() / 2.0f;


        // 新建画布
        Bitmap bgBmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bgBmp);


        // 给头像加缩放（不然Border外侧容易溢出一像素的bitmap内容）
        Matrix matrix = new Matrix();
        matrix.setScale(0.95f, 0.95f, centerX, centerY);
        canvas.drawBitmap(bitmap, matrix, null);



        // 绘制蓝色环
        float rB = (bgBmp.getWidth() - mBorderWidthBlue) / 2.0f;
        canvas.drawCircle(centerX, centerY, rB, mPaintBorderBlue);

        // 绘制渐变白色环（y1乘0.9以保证底部完全透明 CLAMP表示渐变结束后始终使用尾色渲染）
        LinearGradient linearGradient = new LinearGradient(centerX, 0, centerX, bgBmp.getHeight() * 0.9f,
                new int[]{Color.WHITE, Color.TRANSPARENT},
                new float[]{0.55f, 1f},
                Shader.TileMode.CLAMP);

        mPaintBorderWhite.setShader(linearGradient);
        float rW = (bgBmp.getWidth() - 2 * mBorderWidthBlue + mBorderWidthWhite) / 2.0f;
        canvas.drawCircle(centerX, centerY, rW, mPaintBorderWhite);

        return bgBmp;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ExposureCircleCrop;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
