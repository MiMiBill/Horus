package com.example.horus.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;

import com.example.baselib.utils.LogUtil;
import com.luck.picture.lib.config.PictureMimeType;
import com.example.horus.app.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by lognyun on 2018/11/22 16:33:39
 */
public class MediaUtils {

    public static class MediaSize {
        public int width;// 宽
        public int height;// 高
        public int duration;// 时长

        public int degree;// 旋转角度
    }


    /**
     * 获取图片信息
     * @param path 图片路径
     * @return MediaSize
     */
    public static MediaSize getImageSize(String path) {
        MediaSize imageSize = new MediaSize();

        try {

            // 相册旋转照片是国产ROM自己做的功能（但没做完善）
            // 在相册中旋转JPG后可通过Exif获取真实旋转角度
            // 但旋转PNG后 通过Bitmap解码还是原图角度
            // 所以目前相册旋转PNG无解
            imageSize = getImageSizeFromExif(path);

            if (!isMediaSizeAvaliable(imageSize, false)) {
                imageSize = getImageSizeFromBitmap(path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageSize;
    }



    /**
     * 获取视频信息
     * @param path 视频路径
     * @return VideoSize
     */
    public static MediaSize getVideoSize(String path) {
        MediaSize videoSize = new MediaSize();

        try {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            metadataRetriever.setDataSource(path);

            String duration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String width = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String height = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String orientation = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

            int angle = getInt(orientation);
            if ((angle / 90) % 2 == 1) {
                // 竖屏
                String temp = width;
                width = height;
                height = temp;
            }

            videoSize.width = getInt(width);
            videoSize.height = getInt(height);
            videoSize.duration = getInt(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoSize;
    }

    public static int getInt(String str) {
        int ret = 0;
        try {
            ret = Integer.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 判断尺寸是否可用
     * @return true/false
     */
    private static boolean isMediaSizeAvaliable(MediaSize mediaSize, boolean isVideo) {
        if (mediaSize == null) return false;

        if (isVideo) {
            return mediaSize.height > 0 && mediaSize.width > 0 && mediaSize.duration > 0;
        } else {
            return mediaSize.height > 0 && mediaSize.width > 0;
        }
    }


    /**
     * 这破东西读不支持文件时既没有异常也没有公共状态变量
     * 只能靠长宽判断
     *
     * @param path 路径
     * @return 文件尺寸
     * @throws IOException
     */
    private static MediaSize getImageSizeFromExif(String path) throws IOException {
        MediaSize imageSize = new MediaSize();
        int degree = 0;

        ExifInterface exifInterface = new ExifInterface(path);

        int exifHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 100);
        int exifWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 100);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }

        imageSize.degree = degree;
        if (degree == 90 || degree == 270) {
            imageSize.height = exifWidth;
            imageSize.width = exifHeight;
        } else {
            imageSize.height = exifHeight;
            imageSize.width = exifWidth;
        }

        return imageSize;
    }


    /**
     * 通过直接解码获取图片宽高
     * PS：相册中旋转过的PNG图片 解码和上传仍是原图角度
     * PPS：谷歌原生ROM不存在旋转图片操作（国产机自己做的旋转功能）
     *
     * @param path 图片路径
     * @return MediaSize
     */
    private static MediaSize getImageSizeFromBitmap(String path) {
        MediaSize imageSize = new MediaSize();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        imageSize.width = options.outWidth;
        imageSize.height = options.outHeight;

        return imageSize;
    }


    /**
     * 刷新媒体库（单个文件）
     * @param context 尽量使用Application
     * @param filePath 文件路径
     */
    public static void updateMediaAlbum(Context context, String filePath) {

        if (TextUtils.isEmpty(filePath))return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){

            if (PictureMimeType.isSuffixOfImage(filePath.toLowerCase())){
                copyImageToPublicDir(context,filePath);
            }else {
                copyVideoToPublicDir(context,filePath);
            }

        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(context, new String[]{filePath}, null, (path, uri) -> {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(uri);
                context.sendBroadcast(mediaScanIntent);
            });

        } else {
            Uri uri = Uri.fromFile(new File(filePath).getParentFile());
            Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);
            context.sendBroadcast(intent);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void copyImageToPublicDir(Context context,String filePath)
    {


        File file = new File(filePath);
        if (!file.exists())return;
        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.DISPLAY_NAME,file.getName());
//        if (filePath.toLowerCase().endsWith(".gif")){
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/gif");
//        }else {
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        }
        values.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());
        Uri localUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {

            ContentResolver contentResolver = context.getContentResolver();
            ParcelFileDescriptor fileDescriptor = contentResolver.openFileDescriptor(localUri,"w");
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(fileDescriptor.getFileDescriptor());
            copy(fis,fos);
            if (localUri != null)
            {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(localUri);
                MyApp.getInstance().sendBroadcast(mediaScanIntent);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        LogUtil.d("updateMediaAlbum" ,"updateMediaAlbum:" + localUri);
    }

    public static void copy(FileInputStream fis,FileOutputStream fos) throws IOException {
        byte [] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer,0,buffer.length)) != -1)
        {
            fos.write(buffer,0,len);
        }
        fis.close();
        fos.close();
    }



    public static ArrayList<String> pathList = new ArrayList();
    public static void updateMediaAlbumVideo()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && pathList.size() > 0){
            Iterator<String> iterator = pathList.iterator();
            while(iterator.hasNext()) {
                String path = iterator.next();
                iterator.remove();
                File file1 = new File(path);
                //获取ContentResolve对象，来操作插入视频
                ContentResolver localContentResolver = MyApp.getInstance().getContentResolver();
                //ContentValues：用于储存一些基本类型的键值对
                ContentValues localContentValues = getVideoContentValues(MyApp.getInstance(), file1, System.currentTimeMillis());
                //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
                Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,localContentValues);

                try {
                    ContentResolver contentResolver = MyApp.getInstance().getContentResolver();
                    ParcelFileDescriptor fileDescriptor = contentResolver.openFileDescriptor(localUri,"w");
                    FileInputStream fis = new FileInputStream(file1);
                    FileOutputStream fos = new FileOutputStream(fileDescriptor.getFileDescriptor());
                    copy(fis,fos);
                    LogUtil.d("updateMediaAlbum" ,"updateMediaAlbum:" + localUri);
                    if (localUri != null)
                    {
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        mediaScanIntent.setData(localUri);
                        MyApp.getInstance().sendBroadcast(mediaScanIntent);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }




            }

        }
    }


    /**
     * 适配android Q以上的版本
     * @param context
     * @param filePath
     */
    private static void copyVideoToPublicDir(Context context,String filePath){

        File file1 = new File(filePath);
        //获取ContentResolve对象，来操作插入视频
        ContentResolver localContentResolver = MyApp.getInstance().getContentResolver();
        //ContentValues：用于储存一些基本类型的键值对
        ContentValues localContentValues = getVideoContentValues(MyApp.getInstance(), file1, System.currentTimeMillis());
        //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,localContentValues);

        try {
            ContentResolver contentResolver = MyApp.getInstance().getContentResolver();
            ParcelFileDescriptor fileDescriptor = contentResolver.openFileDescriptor(localUri,"w");
            FileInputStream fis = new FileInputStream(file1);
            FileOutputStream fos = new FileOutputStream(fileDescriptor.getFileDescriptor());
            copy(fis,fos);
            Log.d("updateMediaAlbum" ,"updateMediaAlbum:" + localUri);
            if (localUri != null)
            {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(localUri);
                MyApp.getInstance().sendBroadcast(mediaScanIntent);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    /**
     * 视频存在本地
     * @param paramContext
     * @param paramFile
     * @param paramLong
     * @return
     */
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong){
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(MediaStore.Video.Media.TITLE, paramFile.getName());
        localContentValues.put(MediaStore.Video.Media.DISPLAY_NAME, paramFile.getName());
        localContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/3gp");
        localContentValues.put(MediaStore.Video.Media.DATE_TAKEN, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATE_MODIFIED, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATE_ADDED, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATA, paramFile.getAbsolutePath());
        localContentValues.put(MediaStore.Video.Media.SIZE, Long.valueOf(paramFile.length()));
        int duration = getLocalVideoDuration(paramFile.getAbsolutePath());
        localContentValues.put(MediaStore.Video.Media.DURATION,duration);
        return localContentValues;
    }


    public static int getLocalVideoDuration(String videoPath) {
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            return 100;
        }
        return duration;
    }



    /**
     * 刷新媒体库（文件夹）
     * @param context 尽量使用Application
     * @param folderPath 文件夹
     */
    public static void updateMediaFolder(Context context, String folderPath) {
        File folder = new File(folderPath);
        List<String> pathList = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    pathList.add(file.getAbsolutePath());
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {

            MediaScannerConnection.scanFile(context, pathList.toArray(new String[0]), null, (path, uri) -> {

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(uri);
                context.sendBroadcast(mediaScanIntent);
            });

        }else {

            if (pathList.size() > 0)
            {
                Uri uri = Uri.fromFile(new File(pathList.get(0)).getParentFile());
                Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);
                context.sendBroadcast(intent);
            }

        }
    }
}
