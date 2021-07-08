package com.example.horus.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.baselib.utils.CompatibleUtil;
import com.example.baselib.utils.LogUtil;
import com.example.horus.R;
import com.example.horus.app.Constant;
import com.example.horus.app.MyApp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;



/**
 * Created by lognyun on 2019/4/15 15:34:25
 *
 * 保存媒体文件
 *
 * 保存图片直接使用Glide
 * 保存视频/文件使用DownloadManager
 */
public class MediaSaver {

    public static final String ERROR_MEDIA_PATH = "error_media_path";
    public static final String ERROR_MEDIA_TYPE = "error_media_type";
    public static final String VIDEO_SAVE_START = "video_save_start";

    public static final int MEDIA_IMAGE = 1;
    public static final int MEDIA_VIDEO = 2;



    private OnMediaSaveListener mOnMediaSaveListener;
    private String mUrl;
    private int mType;
    private boolean mIsLocal;


    public MediaSaver(int type, String mediaUrl) {
        mUrl = mediaUrl;
        mType = type;
        mIsLocal = false;
    }


    public void startSaving(OnMediaSaveListener listener) {
        mOnMediaSaveListener = listener;

        if (mType == MEDIA_IMAGE) {
            MediaSaverTask saverTask = new MediaSaverTask();
            saverTask.executeOnExecutor(Executors.newCachedThreadPool(), mUrl);

        } else if (mType == MEDIA_VIDEO) {
            if (mUrl.startsWith("file:")){ //本地视频文件直接复制
                mIsLocal = true;
                MediaSaverTask saverTask = new MediaSaverTask();
                saverTask.executeOnExecutor(Executors.newCachedThreadPool(), mUrl);
            }else {
                mIsLocal = false;
                startDownloadVideo(mUrl);
            }

        } else {
            LogUtil.e("MediaSaver", "No such type " + mType);
            if (mOnMediaSaveListener != null) {
                mOnMediaSaveListener.onMediaSaved(false, ERROR_MEDIA_TYPE);
            }
        }
    }



    public interface OnMediaSaveListener {

        /**
         * 保存成功
         * @param success true/false
         * @param info 保存成功返回路径 失败返回错误码
         */
        void onMediaSaved(boolean success, String info);
    }



    private class MediaSaverTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String path = getStoragePath();
            if (path == null || path.isEmpty()) {
                return ERROR_MEDIA_PATH;
            }

            return mIsLocal ? copyVideoOnThread(strings[0], path) : downloadImageOnThread(strings[0], path);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            boolean success = true;
            if (s == null || s.isEmpty() || ERROR_MEDIA_PATH.equals(s)) {
                success = false;
            }

            if (mOnMediaSaveListener != null) {
                mOnMediaSaveListener.onMediaSaved(success, s);
            }
        }
    }



    private String getStoragePath() {
        String filePath = CompatibleUtil.getExternalStoragePath(MyApp.getInstance()).getAbsolutePath();
        filePath += File.separator + Constant.FOLDER_NAME;
        File workspace = new File(filePath);
        if (!workspace.exists()) {
            boolean created = workspace.mkdir();
            if (created) {
                return filePath;
            }
        } else {
            return filePath;
        }
        return null;
    }


    /**
     * 视频下载 暂时只给个开始下载的回调
     * @param url 媒体地址
     */
    private void startDownloadVideo(String url) {
        String path = getStoragePath();
        if (path == null || path.isEmpty()) {
            if (mOnMediaSaveListener != null) {
                mOnMediaSaveListener.onMediaSaved(false, ERROR_MEDIA_PATH);
            }
        } else {
            downloadVideoByService(url, path);

            if (mOnMediaSaveListener != null) {
                mOnMediaSaveListener.onMediaSaved(true, VIDEO_SAVE_START);
            }
        }

    }


    private long downloadVideoByService(String url, String savePath) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // 允许下载的网络
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // 显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 自定义下载路径
        String filePath = savePath + File.separator + FileUtil.createFileName("WorldTalk_VD_", ".mp4");
        MediaUtils.pathList.add(filePath);
        request.setDestinationUri(Uri.fromFile(new File(filePath)));
        // 设置信息
        request.setTitle(MyApp.getInstance().getString(R.string.app_name));
        request.setDescription(MyApp.getInstance().getString(R.string.video_saving));
        request.allowScanningByMediaScanner();// 允许被系统媒体库扫描

        //7.0以上的系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(false);
            request.setRequiresCharging(false);
        }

        // 加入下载队列
        DownloadManager dm = (DownloadManager) MyApp.getInstance().getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = dm.enqueue(request);
        return downloadId;
    }





    private String downloadImageOnThread(String url, String savePath) {
        try {
            FutureTarget<File> target = Glide.with(MyApp.getInstance())
                    .asFile()
                    .load(url)
                    .submit();
            final File imageFile = target.get();
            LogUtil.d("imageFile:" + imageFile.getAbsolutePath());

            // 这样可以拿到path吗？
            String imgSuffix = FileUtil.getImageSuffix(imageFile.getPath());

            String saveFileName = FileUtil.createFileName("WorldTalk_IMG_", "." + imgSuffix);
            File saveTargetFile = new File(savePath, saveFileName);
            if (!saveTargetFile.exists()) {
                saveTargetFile.getParentFile().mkdirs();
                try {
                    saveTargetFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileUtil.copy(imageFile, saveTargetFile);

            // 刷新媒体库
            MediaUtils.updateMediaAlbum(MyApp.getInstance(), saveTargetFile.getAbsolutePath());
//            MediaUtils.updateMediaFolder(MyApp.getInstance(), savePath);

            return saveTargetFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //自己发出的视频直接将缓存里的视频copy一下就好了
    private String copyVideoOnThread(String url, String savePath) {
        try {
            Uri localUri = Uri.parse(url); //Uri和file的path转成String是不一样的
            File copyFile = new File(localUri.getPath());
            String fileSuffix = FileUtil.getFileSuffix(copyFile.getPath());

            String saveFileName = FileUtil.createFileName("video_", fileSuffix);
            File saveTargetFile = new File(savePath, saveFileName);
            if (!saveTargetFile.exists()) {
                saveTargetFile.getParentFile().mkdirs();
                try {
                    saveTargetFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileUtil.copy(copyFile, saveTargetFile);
            // 刷新媒体库
            MediaUtils.updateMediaAlbum(MyApp.getInstance(), saveTargetFile.getAbsolutePath());
            return saveTargetFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
