package com.example.horus.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Preconditions;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * 方形的圆角图片转换器
 */

public class RectRoundCorners extends BitmapTransformation {
    private static final String ID = "com.songhetz.house.util.glide.RectRoundCorners";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private final int roundingRadius;

    public RectRoundCorners(int roundingRadius) {
        Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");
        this.roundingRadius = roundingRadius;
    }

    @Override protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int outWidth, int outHeight) {
        Preconditions.checkArgument(outWidth > 0, "width must be greater than 0.");
        Preconditions.checkArgument(outHeight > 0, "height must be greater than 0.");
        Preconditions.checkArgument(roundingRadius > 0, "roundingRadius must be greater than 0.");

        // Alpha is required for this transformation.
        int size = outWidth > outHeight ? outHeight : outWidth;
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);

        result.setHasAlpha(true);


        inBitmap = scaleBitmap(inBitmap, size, size);


        BitmapShader shader = new BitmapShader(inBitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);


        //绘制圆角
        RectF rect = new RectF(0, 0, result.getWidth(), result.getHeight());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawRoundRect(rect, roundingRadius, roundingRadius, paint);
        canvas.setBitmap(null);


        return result;
    }

    @Override public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
        messageDigest.update(radiusData);
    }


    private Bitmap scaleBitmap(Bitmap bitmap, float outX, float outY) {
        Matrix matrix = new Matrix();
        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        matrix.postScale(outX / w, outY / h); // 长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, (int) w,
                (int) h, matrix, true);

    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof RectRoundCorners) && ((RectRoundCorners) o).roundingRadius == roundingRadius;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + roundingRadius;
    }
}
