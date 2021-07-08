package com.example.horus.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.horus.app.MyApp;

import java.io.File;

/**
 * Created by lognyun on 2019/4/27 19:59:04
 */
public class ImageLoadTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = ImageLoadTask.class.getSimpleName();

    private OnImageLoadListener mOnImageLoadListener;


    public ImageLoadTask(OnImageLoadListener onImageLoadListener) {
        mOnImageLoadListener = onImageLoadListener;
    }

    @Override
    protected String doInBackground(String... strings) {
        return downloadImageOnThread(strings[0], "");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!TextUtils.isEmpty(s)) {
            if (mOnImageLoadListener != null) {
                mOnImageLoadListener.onImageLoaded(true, s);
            }
        } else {
            if (mOnImageLoadListener != null) {
                mOnImageLoadListener.onImageLoaded(false, "图片加载失败");
            }
        }
    }




    private String downloadImageOnThread(String url, String savePath) {
        try {
            FutureTarget<File> target = Glide.with(MyApp.getInstance())
                    .asFile()
                    .load(url)
                    .submit();
            final File imageFile = target.get();

            return imageFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }





    public interface OnImageLoadListener {

        void onImageLoaded(boolean success, String filePath);
    }




    /**
     * 计算出图片初次显示需要放大倍数
     * @param context 需要Activity context
     * @param imagePath 图片的绝对路径
     */
    public static float getImageScale(Context context, String imagePath){
        WindowManager wm = ((Activity)context).getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        return getImageScale(width, height, imagePath);
    }



    /**
     * 计算出图片初次显示需要放大倍数
     * @param displayWidth 显示宽
     * @param displayHeight 显示高
     * @param imagePath 图片的绝对路径
     */
    public static float getImageScale(int displayWidth, int displayHeight, String imagePath){
        if(TextUtils.isEmpty(imagePath)) {
            return 2.0f;
        }

        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        if(bitmap == null) {
            return 2.0f;
        }

        // 拿到图片的宽和高
        int dw = bitmap.getWidth();
        int dh = bitmap.getHeight();

        int width = displayWidth;
        int height = displayHeight;

        float scale = 1.0f;
        //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh <= height) {
            scale = width * 1.0f / dw;
        }
        //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
        if (dw <= width && dh > height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
        if (dw < width && dh < height) {
            scale = width * 1.0f / dw;
        }
        //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
        if (dw > width && dh > height) {
            scale = width * 1.0f / dw;
        }
        bitmap.recycle();
        return scale;
    }

}
