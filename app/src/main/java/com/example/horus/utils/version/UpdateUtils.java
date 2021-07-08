package com.example.horus.utils.version;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * Created by lognyun on 2019/2/15 17:59:33
 *
 * 把一些方法移到这里
 * UpdateManager会清晰一些
 */
public class UpdateUtils {
    private static final String TAG = UpdateUtils.class.getSimpleName();

    /**
     * 传入两个版本号
     * @param ver1 1.0.6
     * @param ver2 1.10.1
     * @return -1前者小于后者 / 0等于 / 1前者大于后者
     */
    public static int compare(String ver1, String ver2) {
        if (ver1 == null || ver1.isEmpty()
                || ver2 == null || ver2.isEmpty()) {
            return 0;
        }

        String[] arr1 = ver1.split("\\.");
        String[] arr2 = ver2.split("\\.");

        if (arr1.length != 3 || arr2.length != 3) {
            return 0;
        }

        for (int i = 0; i < 3; i++) {
            int compare = arr1[i].compareToIgnoreCase(arr2[i]);
            if (compare == 0) {
                continue;
            }
            return compare;
        }

        return 0;
    }



    /**
     * 获取最新APK的保存路径（不一定存在）
     * @param version 版本号
     * @return 路径
     */
    protected static String getCacheApkPath(Context context, String version) {
        String cacheDir = getDiskCacheDir(context);
        String apkName = UpdateManager.APK_HEAD + version + UpdateManager.APK_END;
        return cacheDir + File.separator + apkName;
    }

    private static String getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                return context.getExternalCacheDir().getPath();
            }
        }
        return context.getCacheDir().getPath();
    }


    /**
     * 无需更新时删除任何安装包
     */
    protected static void deleteApk(Context context) {
        String cache = getDiskCacheDir(context);
        File cacheDir = new File(cache);
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        if (fileName.contains(UpdateManager.APK_END)) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }






    /**
     * 魅蓝s6上报错
     */
    @Deprecated
    protected static Uri getApkUri(Context context, long downloadId) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (dm != null) {
            // FIXME 官方推荐的方法，也没有版本限制，为什么在魅蓝s6上报错？
            Uri fileUri = dm.getUriForDownloadedFile(downloadId);
            if (fileUri != null) {
                LogUtil.d(TAG, "File path:" + fileUri.getPath());
                return fileUri;
            } else {
                LogUtil.e(TAG, "No such file with download id:" + downloadId);
            }
        } else {
            LogUtil.e(TAG, "DownloadManager is null.");
        }
        return null;
    }

    /**
     * 以供从下载服务中调用
     * @param context
     * @param uri
     */
    protected static void installApk(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * getUriForDownloadedFile()在魅蓝s6上报错
     * 只能使用下面这种复杂的方式处理
     * 由于版本区别 导致无法直接使用 fileUri
     */
    protected static String getApkFilePath(Context context, long downloadId) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (dm != null) {

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            Cursor c = dm.query(query);
            String filePath = null;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                String fileUri = null;

                if (c.moveToFirst()) {
                    int fileUriIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    fileUri = c.getString(fileUriIdx);
                }
                c.close();

                if (fileUri != null) {
//                    filePath = Uri.parse(fileUri).getPath();
                    File file = new File(Uri.parse(fileUri).getPath());
                    return file.getAbsolutePath();
                } else {
                    LogUtil.e(TAG, "FileUri is null.");
                }
            } else {
                // 过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
                if (c.moveToFirst()) {
                    int fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    filePath = c.getString(fileNameIdx);
                }

            }

            return filePath;

        } else {
            LogUtil.e(TAG, "DownloadManager is null.");
        }
        return null;
    }

    /**
     * 以供从下载服务中调用
     * @param context
     * @param apkPath
     */
    protected static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        File file = new File(apkPath);
        if (file.exists() && file.isFile() && file.length() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".myfileprovider", new File(apkPath));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        } else {
            toast(R.string.update_install_path_error);
        }
    }


    public static boolean isApkFile(Context context, String filePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                if (!TextUtils.isEmpty(appInfo.packageName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // 不是Apk文件
        }
        return false;
    }


    private static void toast(int strId) {
        ToastUtil.showShortMessage(strId);
    }

}
