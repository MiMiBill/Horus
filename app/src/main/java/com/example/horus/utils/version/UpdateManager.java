package com.example.horus.utils.version;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.example.horus.R;
import com.example.horus.app.Config;
import com.example.horus.data.VersionInfo;
import com.example.horus.widget.dialog.TransparentDialog;


import java.io.File;

/**
 * Created by lognyun on 2018/11/14 15:07:23
 *
 * 版本更新弹窗
 *
 * 首先根据大中小版本判断是否展示弹窗/展示何种弹窗
 *
 * 点击更新按钮时，判断是否已下载好更新包
 * 已下载好则直接安装，否则开启下载服务进行下载
 */
public class UpdateManager {
    private static final String TAG = UpdateManager.class.getSimpleName();
    public static final String APK_HEAD = "Timi_";
    public static final String APK_END = ".apk";

    private static final int DOWNLOAD_QUERY_DURATION = 1000;// 毫秒

    // 设置隔多久请求一次版本信息接口（ms）
    private static final long UPDATE_DURATION = 5 * 60 * 1000L;

    public static final String LARGE = "3";
    public static final String MIDDLE = "2";
    public static final String SMALL = "1";
    public static final String NONE = "0";

    private Context mContext;

    private TransparentDialog mDialog;

    private Handler mHandler;// 下载用
    private DownloadRunnable mDownloadRunnable;

    private View mDialogView;// 弹窗视图
    private TextView mDownloadText;// 下载按钮
    private TextView mDownloadMask;// 下载进度遮罩
    private int mDownloadTextWidth;// 下载按钮宽度


    private DownloadChecker mDownloadChecker;

    private OnUpdateListener mOnUpdateListener;

    private boolean showDialog;


    public interface OnUpdateListener {
        /**
         * 大/中/小
         * @param type
         */
        void onUpdate(String type, VersionInfo versionInfo);

        /**
         * 大版本取消时要关闭app
         */
        void onCancel(String type);
    }

    /**
     * 检测是否需要调用接口
     * @return true 需要调用接口
     */
    public static boolean needUpdateVersionInfo() {
        VersionInfo info = Config.getVersionInfo();
        if (info != null) {
            long duration = System.currentTimeMillis() - info.getUpdateTime();
            return duration > UPDATE_DURATION;
        }
        return true;
    }

    /**
     * 后台的数据在data外又包了一层，只能自己解析
     */
    public static void saveVersionInfo(Context context, String verInfo) {
        VersionInfo info = null;
        try {
            JSONObject object = JSONObject.parseObject(verInfo);
            if (object != null) {
                info = object.getObject("channel", VersionInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveVersionInfo(context, info);
    }

    /**
     * 保存接口获取的版本信息
     * @param context 上下文
     * @param info 版本信息
     */
    public static void saveVersionInfo(Context context, VersionInfo info) {
        if (info == null) {
            info = new VersionInfo();
            info.setCurrentVersionCode(VersionUtil.getVersionName(context));
            info.setCurrentVersionType(NONE);
        }


        VersionInfo oldInfo = Config.getVersionInfo();
        if (oldInfo != null && oldInfo.getCurrentVersionCode() != null
                && oldInfo.getCurrentVersionCode().equals(info.getCurrentVersionCode())) {

            info.setMiddleClosed(oldInfo.isMiddleClosed());// 如果该版本已关闭中版本弹窗 记得保留状态以免弹窗反复出现
        }

        info.setUpdateTime(System.currentTimeMillis());
        Config.setVersionInfo(info);
    }


    /**
     * 获取文件路径
     */
    public static String getApkFilePath(Context context, long downloadId) {
        return UpdateUtils.getApkFilePath(context, downloadId);
    }

    /**
     * 以供从下载服务中调用
     */
    public static void installApk(Context context, String apkPath) {
        UpdateUtils.installApk(context, apkPath);
    }


    /**
     * 其他方法需要频繁使用context或回调 故不用静态方法处理
     * @param context
     */
    public UpdateManager(Context context) {
        mContext = context;
    }

    public void checkUpdate(OnUpdateListener onUpdateListener) {
        checkUpdate(onUpdateListener, false);
    }

    public void checkUpdateIgnoreSmall(OnUpdateListener onUpdateListener) {
        checkUpdate(onUpdateListener, true);
    }

    /**
     * 需要listener的更新检测
     */
    public void checkUpdate(OnUpdateListener onUpdateListener, boolean ignoreSmall) {
        mOnUpdateListener = onUpdateListener;

        VersionInfo info = Config.getVersionInfo();
        if (info == null) return;

        String type = checkUpdateType(info);
        switch (type) {
            case LARGE:// 大弹窗
                showDialog(info, true, false);
                showDialog = true;
                break;
            case MIDDLE:// 中弹窗
                if (info.isMiddleClosed()) {
                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onCancel(MIDDLE);
                    }
                } else {
                    showDialog(info, false, false);
                    showDialog = true;
                }
                break;
            case SMALL:// 在关于我们页展示小红点
                if (!ignoreSmall) {
                    showDialog(info, false, true);
                    showDialog = true;
                } else {
                    showUpdateDot(info);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 是否显示弹窗 需要在checkUpdateType后调用
     */
    public boolean ifShowDialog() {
        return showDialog;
    }

    /**
     * 判断是否显示更新title，更新版本为中版本且之前关闭过更新弹窗?
     * @return true
     */
    public boolean checkShowTitle() {
        VersionInfo info = Config.getVersionInfo();
        if (info == null) return false;

        String type = checkUpdateType(info);
        return MIDDLE.equals(type) && info.isMiddleClosed();
    }

    /**
     * 判断是否有更新
     * @return true
     */
    public boolean checkHasNewVer() {
        VersionInfo info = Config.getVersionInfo();
        if (info == null) return false;

        String type = checkUpdateType(info);
        return !NONE.equals(type);
    }

    private String checkUpdateType(VersionInfo info) {
        String type;
        if (info == null)
            return NONE;

        String curVer = VersionUtil.getVersionName(mContext);
        String newVer = info.getCurrentVersionCode();
        if (newVer != null) {
            newVer = newVer.replace("V", "");
            newVer = newVer.replace("v", "");
        }
        if (UpdateUtils.compare(curVer, newVer) >= 0) {
            UpdateUtils.deleteApk(mContext);
            return NONE;
        }

        type = info.getCurrentVersionType();
        if (type == null || type.isEmpty()) {
            LogUtil.e(TAG, "Version Type is null or empty.");
            return NONE;
        }

        return type;
    }


    /**
     * 更新弹窗 下载时弹窗需要显示进度条并更新下载进度
     */
    private void showDialog(VersionInfo info, boolean force, boolean cancelable) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.layout_update_dialog, null);
        mDialogView = root;

        TextView tvVer = root.findViewById(R.id.tv_layout_update_ver);
        tvVer.setText(info.getCurrentVersionCode());

        mDialog = new TransparentDialog.Builder(mContext)
                .setContentView(root)
                .setCancelable(cancelable)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
                .create();
        mDialog.show();

        TextView button = root.findViewById(R.id.btn_layout_update_now);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDialog.dismiss();// 等待下载
                update(info);
                if (mOnUpdateListener != null) {
                    String type = SMALL;
                    if (force) {
                        type = LARGE;
                    } else if (!cancelable){
                        type = MIDDLE;
                    }
                    mOnUpdateListener.onUpdate(type, info);
                }
            }
        });

        ImageView ivClose = root.findViewById(R.id.iv_layout_update_dialog_close);
        ivClose.setVisibility(View.VISIBLE);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新版本信息
                if (!cancelable) {
                    info.setMiddleClosed(true);
                    Config.setVersionInfo(info);
                }

                // 界面更新
                mDialog.dismiss();
                if (mOnUpdateListener != null) {
                    if (force) {
                        mOnUpdateListener.onCancel(LARGE);
                    } else {
                        mOnUpdateListener.onCancel(MIDDLE);
                    }
                }
            }
        });
    }


    /**
     * 显示关于我们里小版本更新的小红点
     */
    private void showUpdateDot(VersionInfo info) {
        if (mOnUpdateListener != null) {
            mOnUpdateListener.onUpdate(SMALL, info);
        }
    }

    /**
     * 首先区分国内外
     * 国外：跳转play
     * 国内：判断是否存在apk 存在安装，不存在启动服务下载
     * @param info
     */
    public void update(VersionInfo info) {
//        if (!MarketConditionUtils.isMainLand()) {
//            jumpToGooglePlay();
//            return;
//        }

        toast(R.string.update_update_start);
        String apkPath = UpdateUtils.getCacheApkPath(mContext, info.getCurrentVersionCode());

        // 由于程序保存了旧版本下载id，所以如果没有对应版本的apk，需清除旧下载id，否则会获取到旧版本地址
        File file = new File(apkPath);
        if (!file.exists()) {
            Config.setApkDownloadId(0L);
        }

        // 下载
        if (info.getCurrentUpdateApk() == null || info.getCurrentUpdateApk().isEmpty()) {
            toast(R.string.update_download_url_error);
        } else {
            downloadApk(info.getCurrentUpdateApk(), apkPath, info.getCurrentVersionCode());
        }
    }

    private void jumpToGooglePlay() {
        String uri = "market://details?id=" + mContext.getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        // 存在手机里没有安装应用市场的情况，跳转会报异常，做一个接收判断
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        } else {
            // 没有应用市场，通过浏览器跳转
            uri = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
            intent.setData(Uri.parse(uri));
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            } else {
                // 连浏览器都没有
                toast(R.string.update_jump_google_error);
                LogUtil.e(TAG, "Don't have App Market and Browser!");
            }
        }
    }


    /**
     * 启动服务下载
     */
    private void downloadApk(String url, String savePath, String version) {
        // UpdateService.download(mContext, url, savePath, version);

        if (mDownloadChecker == null) {
            mDownloadChecker = new DownloadChecker(mContext);
        }

        long downloadId = mDownloadChecker.download(url, savePath, version);
        if (downloadId > 0) {
            changeDialogToDownloadingMode();// 更改弹窗样式
            startDownloadingApk(downloadId);// 下载任务轮询

        } else if (downloadId == DownloadChecker.DOWNLOAD_DIRECT_INSTALL){
            // 直接安装不要异常提示

        } else {
            LogUtil.e(TAG, "Download Id 异常：" + downloadId);
            toast(R.string.error_data);
        }
    }


    /**
     * 开始下载 查询当前下载量
     */
    private void startDownloadingApk(long downloadId) {
        // 开始轮询查询
        if (mHandler == null) {
            mHandler = new Handler();
        }
        if (mDownloadRunnable == null) {
            mDownloadRunnable = new DownloadRunnable(downloadId);
        }
        mHandler.postDelayed(mDownloadRunnable, 0);
    }


    /**
     * 弹窗切换成下载模式
     */
    private void changeDialogToDownloadingMode() {
        if (mDialogView == null) {
            VersionInfo info = Config.getVersionInfo();
            if (info != null) {
                showDialog(info, true, false);
            } else {
                toast(R.string.error_data);
                return;
            }
        }

        ImageView ivClose = mDialogView.findViewById(R.id.iv_layout_update_dialog_close);
        ivClose.setVisibility(View.INVISIBLE);// 隐藏关闭按钮

        mDownloadText = mDialogView.findViewById(R.id.btn_layout_update_now);
        mDownloadText.setClickable(false);

        mDownloadTextWidth = mDownloadText.getWidth();
        mDownloadMask = mDialogView.findViewById(R.id.tv_layout_update_progress_mask);
    }


    private class DownloadRunnable implements Runnable {

        private long mDownloadId;

        public DownloadRunnable(long downloadId) {
            mDownloadId = downloadId;
        }

        @Override
        public void run() {
            long[] status = mDownloadChecker.queryDownloadStatus(mDownloadId);
            if (status[2] < -100) {
                toast(R.string.error_data);
                return;
            }

            int percent = (int) (status[1] * 100.0f / status[0]);
            if (percent < 0) {
                percent = 0;
            }
            if (percent >= 99) {
                percent = 100;
                if (mDownloadText != null) {
                    mDownloadText.setClickable(true);
                }

            } else {
                mHandler.postDelayed(mDownloadRunnable, DOWNLOAD_QUERY_DURATION);
            }

            if (mDownloadText != null && mDownloadMask != null) {
                mDownloadText.setText(percent + "%");

                if (mDownloadTextWidth == 0) {
                    mDownloadTextWidth = mDownloadText.getWidth();
                }

                int maskWidth = (int) (mDownloadTextWidth * (100.0f - percent) / 100);
                if (maskWidth < 0) {
                    maskWidth = 0;
                }
                ViewGroup.LayoutParams lp = mDownloadMask.getLayoutParams();
                lp.width = maskWidth;
                mDownloadMask.setLayoutParams(lp);
            }

        }
    }



    private static void toast(int strId) {
        ToastUtil.showShortMessage(strId);
    }
}
