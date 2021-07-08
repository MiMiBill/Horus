package com.example.horus.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;
import com.example.horus.utils.MediaUtils;
import com.example.horus.utils.version.UpdateManager;
import com.example.horus.utils.version.UpdateUtils;


/**
 * Created by Liao on 2018/11/20 16:00:23
 */
public class UpdateReceiver extends BroadcastReceiver {
    private static final String TAG = UpdateReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            installApk(context, downloadId);
        }
    }

    private void installApk(Context context, long downloadId) {
        MediaUtils.updateMediaAlbumVideo();
        String apkPath = UpdateManager.getApkFilePath(context, downloadId);
        if (!TextUtils.isEmpty(apkPath)) {
            if (UpdateUtils.isApkFile(context, apkPath)) {
//                EventBus.getDefault().post(new InstallApkEvent());
            }

        } else {
            LogUtil.e(TAG, "No such file with download id:" + downloadId);
        }
    }
}
