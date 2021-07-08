package com.example.horus.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

/**
 * Created by Liao on 2018/9/6 14:46:35
 * Glide加载带边框的圆形
 */
public class GlideBorderCircleCrop extends BitmapTransformation {
    private static final String ID = "com.zerophil.worldtalk.GlideBorderCircleCrop";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private int mBorderColor;
    private int mBorderWidth;

    private Paint mPaintBorder;

    public GlideBorderCircleCrop() {
    }

    public GlideBorderCircleCrop(int borderWidth, int borderColor) {
        mBorderColor = borderColor;
        mBorderWidth = borderWidth;

        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(mBorderWidth);
        mPaintBorder.setColor(mBorderColor);
    }

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
        if (mPaintBorder == null) {
            return bitmap;
        }

        float x = bitmap.getWidth() / 2.0f;
        float y = bitmap.getHeight() / 2.0f;
        float r = x - mBorderWidth / 2.0f;
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(x, y, r, mPaintBorder);
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideBorderCircleCrop;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
