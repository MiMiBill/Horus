package com.example.horus.utils.version;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.app.Config;

import java.io.File;


/**
 * Created by lognyun on 2019/2/15 17:27:07
 */
public class DownloadChecker {
    private static final String TAG = DownloadChecker.class.getSimpleName();

    public static final int DOWNLOAD_SERVICE_NULL = -1;// 服务异常
    public static final int DOWNLOAD_DIRECT_INSTALL = -2;// 直接安装
    public static final int DOWNLOAD_ERROR_URL = -3;// url异常

    private Context mContext;
    private DownloadManager mDownloadManager;



    public  DownloadChecker(Context context){
        mContext = context;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }


    /**
     * 开始下载时返回 downloadId
     * 其他情况返回对应状态码
     */
    public long download(String url, String savePath, String version) {

        if (mDownloadManager == null) {
            toast("下载服务获取失败");
            return DOWNLOAD_SERVICE_NULL;
        }

        // 检测之前的下载
        Uri uri = null;
        String apkPath = null;
        long downloadId = Config.getApkDownloadId();

        if (downloadId != 0L) {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            Cursor c = mDownloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    apkPath = UpdateManager.getApkFilePath(mContext, downloadId);
//                    uri = UpdateManager.getApkUri(this, downloadId);
                }
            }
            if (c != null) {
                c.close();
            }
        }

        // 如果有文件直接安装
        if (apkPath != null) {
            LogUtil.d(TAG, "Apk exist:" + apkPath);
            UpdateManager.installApk(mContext, apkPath);
            return DOWNLOAD_DIRECT_INSTALL;
        }

//        if (uri != null) {
//            LogUtil.e(TAG, "Apk exist:" + uri.toString());
//            UpdateManager.installApk(this, uri);
//            stopSelf();
//            return;
//        }

        //可能为空
        if(TextUtils.isEmpty(url)){
            return DOWNLOAD_ERROR_URL;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // 允许下载的网络
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // 显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 自定义下载路径
        request.setDestinationUri(Uri.fromFile(new File(savePath)));

        // 设置信息
        request.setTitle(mContext.getString(R.string.app_name) + ":" + version);
        request.setDescription(mContext.getString(R.string.update_notification_text));
        request.setMimeType("application/vnd.android.package-archive");// 文件类型为apk

        //7.0以上的系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(false);
            request.setRequiresCharging(false);
        }

        // 加入下载队列
        downloadId = mDownloadManager.enqueue(request);
        Config.setApkDownloadId(downloadId);

        return downloadId;
    }


    /**
     *
     * @param downloadId 下载id
     * @return long[3] 总bytes / 当前bytes / 状态值
     */
    public long[] queryDownloadStatus(long downloadId) {
        long[] ret = new long[]{ 0, 0, 0 };

        Cursor c = mDownloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
        if (c == null) {
            LogUtil.e(TAG, "下载服务游标异常");
            ret[2] = -101;
        } else if (c.moveToFirst()){

            ret[0] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            ret[1] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            ret[2] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_STATUS));// 下载状态

        } else {
            LogUtil.e(TAG, "下载服务游标非MoveToFirst");
            ret[2] = -102;
        }

        if (c != null && !c.isClosed()) {
            c.close();
        }

        return ret;
    }



    private void toast(String msg) {
        ToastUtil.showShortMessage(msg);
    }
}
