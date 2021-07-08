package com.example.horus.utils.version;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.text.TextUtils;


import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.app.Config;

import java.io.File;

/**
 *
 * @Deprecated
 * 开启服务是最初自己做下载服务时的设计
 * 现在使用DownloadManager后可以直接用普通类去实现
 */
@Deprecated
public class UpdateService extends Service {
    private static final String TAG = UpdateService.class.getSimpleName();

    public static void download(Context context, String url, String savePath, String version) {
        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("url", url);
        intent.putExtra("savePath", savePath);
        intent.putExtra("version", version);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        String savePath = intent.getStringExtra("savePath");
        String version = intent.getStringExtra("version");
        download(url, savePath, version);
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String url, String savePath, String version) {

        DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (dm == null) {
            toast("下载服务获取失败");
            stopSelf();
            return;
        }

        // 检测之前的下载
        Uri uri = null;
        String apkPath = null;
        long downloadId = Config.getApkDownloadId();

        if (downloadId != 0L) {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            Cursor c = dm.query(query);
            if (c != null && c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    apkPath = UpdateManager.getApkFilePath(this, downloadId);
//                    uri = UpdateManager.getApkUri(this, downloadId);
                }
            }
            if (c != null) {
                c.close();
            }
        }

        // 如果有文件直接安装
        if (apkPath != null) {
            LogUtil.e(TAG, "Apk exist:" + apkPath);
            UpdateManager.installApk(this, apkPath);
            stopSelf();
            return;
        }

//        if (uri != null) {
//            LogUtil.e(TAG, "Apk exist:" + uri.toString());
//            UpdateManager.installApk(this, uri);
//            stopSelf();
//            return;
//        }

        //可能为空
        if(TextUtils.isEmpty(url)){
            return;
        }

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // 允许下载的网络
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // 显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

        // 自定义下载路径
        request.setDestinationUri(Uri.fromFile(new File(savePath)));

        // 设置信息
        request.setTitle(getString(R.string.app_name) + ":"+  version);
        request.setDescription(getString(R.string.update_notification_text));
        request.setMimeType("application/vnd.android.package-archive");// 文件类型为apk

        //7.0以上的系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(false);
            request.setRequiresCharging(false);
        }

        // 加入下载队列
        downloadId = dm.enqueue(request);
        Config.setApkDownloadId(downloadId);

        stopSelf();
    }

    private void toast(String msg) {
        ToastUtil.showShortMessage(msg);
    }
}
