package com.example.horus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import android.util.Log;

import com.example.baselib.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by lognyun on 2017/7/28 0028.
 *
 * 调用系统拍照和相册的工具类
 */

public class PhotoUtil {
    private static final String TAG = PhotoUtil.class.getSimpleName();

    public static Uri fromCamera(Activity activity, int requestCode) {
        File file = getDefaultPhotoFile(activity);

        Uri defaultUri = getUriFromFile(activity, file);

        if (defaultUri != null) {
            fromCamera(activity, requestCode, defaultUri);
        }
        return defaultUri;
    }

    public static void fromCamera(Activity activity, int requestCode, Uri uri) {
        if (uri != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestCode);
        } else {
            Log.e(TAG, "Uri is null in method fromCamera()");
        }
    }

    /**
     * 5.0以前两种方式没有区别，都是：Uri:/external/images/media/123
     * 5.0+使用这种方式会弹出带侧边栏的“最近”图片选择页，Uri:/document/image:123
     * @param activity
     * @param requestCode
     */
    public static void fromAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 该方式Uri始终是：Uri:/external/images/media/123
     * 部分定制ROM可能有改动
     * @param activity
     * @param requestCode
     */
    public static void fromAlbum2(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    // 缩放裁剪，接收参数为uri
    public static void startPhotoZoom(Activity activity, int requestCode, Uri uri) {
        if (uri == null) {
            Log.e(TAG, "Uri is null in method startPhotoZoom()");
            return;
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("scale", true);
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        // 不加这句在华为7.0上除“/external/images/media/166424”形式外的uri会提示“无法加载此图片”
        // 但在7.1.1模拟器上无异常
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(intent, requestCode);
    }

    private static File getDefaultPhotoFile(Context context) {
        String fileName = getPhotoName();
        File file;

        try {
            File albumDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            String albumPath = albumDir.getAbsolutePath() + "/Camera/";
            file = new File(albumPath, fileName);
        } catch (Exception e) {
            file = new File(context.getExternalFilesDir("/Photo"), fileName);
            e.printStackTrace();
        }

        return file;
    }

    private static Uri getUriFromFile(Context context, File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    private static String getPhotoName() {
        String name = "" + System.currentTimeMillis();

        try {
            GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            name = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "IMG_" + name + "_FNS.jpg";
    }

    public static File bitmapToFile(Context context, Bitmap bmp, String fileName) {
        if (context == null || bmp == null) {
            LogUtil.e(TAG, "Null data in class PhotoUtil");
            return null;
        }
        try {
            File file = new File(context.getExternalCacheDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            Log.e(TAG, "Catch exception in class PhotoUtil");
        }
        return null;
    }

}
