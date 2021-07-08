package com.example.horus.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

import com.example.baselib.utils.LogUtil;
import com.example.baselib.utils.ToastUtil;
import com.luck.picture.lib.permissions.Permission;
import com.luck.picture.lib.permissions.RxPermissions;
import com.example.horus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lognyun on 2018/11/23 17:13:34
 *
 * 调用 putPermissions()
 * 然后调用 check()
 *
 * 国产 6.0 以下系统，摄像头和录音需采用特殊方案
 * 详见 request 内部 onNext() 回调
 */
public class PermissionTool {
    private static final String TAG = PermissionTool.class.getSimpleName();

    private Activity mActivity;

    private OnPermissionListener mOnPermissionListener;

    private Map<String, PermissionInfo> mPermissionInfoMap;

    private String[] permissions = null;

    private List<String> nonToastPermissions = null;

    public interface OnPermissionListener {
        void onPermissionGranted(boolean granted);
    }

    public PermissionTool(Activity activity) {
        mActivity = activity;
        init();
    }

    private void init() {
        mPermissionInfoMap = initInfoMap();
    }

    /**
     * 放置权限
     */
    public void putPermissions(String...permissions) {
        this.permissions = permissions;
    }


    /**
     * 如果有哪些权限被拒后不需要toast 就放入该数组
     */
    public void nonToastPermissions(String...permissions) {
        if (permissions != null) {
            nonToastPermissions = Arrays.asList(permissions);
        }
    }


    /**
     * 通常使用该方法即可 目前支持5个权限，后期优化
     */
    public void check(OnPermissionListener onPermissionListener) {
        mOnPermissionListener = onPermissionListener;

        checkPermissions();
    }

    private void checkPermissions() {
        String rationaleStr = "";
        for (int i = 0; i < permissions.length; i++) {
            if (shouldShowRationale(mActivity, permissions[i])) {
                if (!rationaleStr.isEmpty()) {
                    rationaleStr += "\n\n";
                }
                rationaleStr += getRationale(permissions[i]);
            }
        }

        if (!rationaleStr.isEmpty()) {
            showRationale(rationaleStr);
        } else {
            request();
        }
    }

    private void showRationale(String rationale) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.PermissionDialog);
        builder.setTitle(R.string.pms_rationale);
        builder.setMessage(rationale);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnPermissionListener != null) {
                    mOnPermissionListener.onPermissionGranted(false);
                }
            }
        });
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                request();
            }
        });
        builder.show();
    }

    private void requestComplete(List<String> permissions) {

        if (permissions == null || permissions.isEmpty()) {
            if (mOnPermissionListener != null) {
                mOnPermissionListener.onPermissionGranted(true);
            }

        } else {

            // 可能会有其他的流程或展示需求（考虑把toast的部分放在回调外让用户自己去处理）
            // 把未通过的权限拼接起来，Toast提醒用户去设置开启
            String name = "";
            for (int i = 0; i < permissions.size(); i++) {

                // 有的权限可能不需要拒绝后Toast（例如启动时的定位权限）
                String permission = permissions.get(i);
                if (nonToastPermissions == null || !nonToastPermissions.contains(permission)) {
                    name += getName(permission);
                }
            }

            if (!name.isEmpty()) {
                String msg = mActivity.getString(R.string.pms_denied, name);
                ToastUtil.showShortMessage(msg);
            }

            if (mOnPermissionListener != null) {
                mOnPermissionListener.onPermissionGranted(false);
            }

        }
    }

    private void request() {
        List<String> permissionList = new ArrayList<>();
        RxPermissions rxPermissions = new RxPermissions(mActivity);
        rxPermissions.requestEach(permissions).subscribe(new Observer<Permission>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Permission permission) {
                if (!permission.granted) {
                    permissionList.add(permission.name);
                } else {
                    // 如果是国产小于 6.0 的系统，判断一下拍照和录音
                    // 录音权限判断 isAudioUsable 可能会产生问题（例如设备不支持采样率）
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        if (Manifest.permission.CAMERA.equals(permission.name) && !isCameraUsable()) {
                            permissionList.add(permission.name);
                        } else if (Manifest.permission.RECORD_AUDIO.equals(permission.name) && !isAudioUsable()) {
                            permissionList.add(permission.name);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();// 应该走授权回调吗（还是提供异常回调？）
            }

            @Override
            public void onComplete() {
                requestComplete(permissionList);
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    private boolean shouldShowRationale(Activity activity, String permission) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
                && activity.shouldShowRequestPermissionRationale(permission);
    }

    private String getName(String permission) {
        String name = "";
        PermissionInfo info = mPermissionInfoMap.get(permission);
        if (info != null) {
            name = mActivity.getString(info.name);
            name = mActivity.getString(R.string.pms_name_wrapper, name);
        } else {
            LogUtil.e(TAG, "Not support permission:" + permission);
        }
        return name;
    }

    private String getRationale(String permission) {
        String rationale = "";
        PermissionInfo info = mPermissionInfoMap.get(permission);
        if (info != null) {
            String name = mActivity.getString(info.name);
            name = mActivity.getString(R.string.pms_name_wrapper, name);
            rationale = mActivity.getString(info.rationale, name);
        } else {
            LogUtil.e(TAG, "Not support permission:" + permission);
        }
        return rationale;
    }

    private static class PermissionInfo {
        int name;
        int rationale;

        PermissionInfo(int name, int rationale) {
            this.name = name;
            this.rationale = rationale;
        }
    }

    private Map<String, PermissionInfo> initInfoMap() {
        Map<String, PermissionInfo> infoMap = new HashMap<>();

        infoMap.put(Manifest.permission.READ_PHONE_STATE, new PermissionInfo(
                R.string.pms_name_read_phone_state,
                R.string.pms_rationale_read_phone_state));

        infoMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionInfo(
                R.string.pms_name_read_external_storage,
                R.string.pms_rationale_read_external_storage));

        infoMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionInfo(
                R.string.pms_name_write_external_storage,
                R.string.pms_rationale_write_external_storage));

        infoMap.put(Manifest.permission.CAMERA, new PermissionInfo(
                R.string.pms_name_camera,
                R.string.pms_rationale_camera));

        infoMap.put(Manifest.permission.RECORD_AUDIO, new PermissionInfo(
                R.string.pms_name_record_audio,
                R.string.pms_rationale_record_audio));

        infoMap.put(Manifest.permission.ACCESS_FINE_LOCATION, new PermissionInfo(
                R.string.pms_name_access_fine_location,
                R.string.pms_rationale_access_fine_location));

        infoMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, new PermissionInfo(
                R.string.pms_name_access_coarse_location,
                R.string.pms_rationale_access_coarse_location));

        infoMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE , new PermissionInfo(
                R.string.pms_name_write_external_storage,
                R.string.pms_rationale_write_external_storage));

        return infoMap;
    }


    /**
     * 针对国产 6.0 以下的权限处理（据说部分国产6.0以上也会用到）
     */
    public static boolean isCameraUsable() {
        boolean usable = true;
        Camera camera = null;
        try {
            camera = Camera.open();

            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            // 对象不为null
            Camera.Parameters mParameters = camera.getParameters();
            camera.setParameters(mParameters);
        } catch (Exception e) {
            usable = false;
        }

        if (camera != null) {
            try {
                camera.release();
            } catch (Exception e) {
                // Do nothing
            }
        }

        return usable;
    }

    /**
     * 针对国产 6.0 以下的录音权限处理
     * 采样率定为 44100
     */
    public static boolean isAudioUsable() {

        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRate = 44100;
        int channel = AudioFormat.CHANNEL_IN_MONO;// 单声道
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channel, audioFormat);
        if (bufferSize < 0) {
            return false;
        }

        AudioRecord audioRecord = null;

        try {
            audioRecord = new AudioRecord(audioSource, sampleRate, channel, audioFormat, bufferSize);
            audioRecord.startRecording();
        } catch (Exception e) {
            return false;
        }

        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            audioRecord.stop();
            audioRecord.release();
            return false;
        }

        audioRecord.stop();
        audioRecord.release();

        return true;
    }


    /**
     * 启动
     */
    public static String[] readImei() {
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};
        return permissions;
    }

    /**
     * 启动
     */
    public static String[] splash() {
        String[] permissions = new String[3];
        permissions[0] = Manifest.permission.READ_PHONE_STATE;
        permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
        return permissions;
    }

    /**
     * 保存文件
     */
    public static String[] saveFile(){
        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return permissions;
    }

    /**
     * 系统弹窗
     */
    public static String[] systemAlertWindow () {
        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.SYSTEM_ALERT_WINDOW ;
        return permissions;
    }

    /**
     * 选择相册
     */
    public static String[] album() {
        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return permissions;
    }

    /**
     * 选择相册带相机
     */
    public static String[] albumWithCamera() {
        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
//        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.CAMERA ;

        return permissions;
    }

    /**
     * 发布动态
     */
    public static String[] publish() {
        String[] permissions = new String[3];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.CAMERA;
        permissions[2] = Manifest.permission.RECORD_AUDIO;
        return permissions;
    }

    /**
     * 清除缓存
     */
    public static String[] clearCache(){
        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        return permissions;
    }

    /**
     * 视频聊天
     */
    public static String[] videoChat() {
        String[] permissions = new String[2];
        permissions[0] = Manifest.permission.CAMERA;
        permissions[1] = Manifest.permission.RECORD_AUDIO;
        return permissions;
    }

    /**
     * 聊天相机
     */
    public static String[] chatCamera() {
        String[] permissions = new String[3];
        permissions[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.CAMERA;
        permissions[2] = Manifest.permission.RECORD_AUDIO;
        return permissions;
    }
}
