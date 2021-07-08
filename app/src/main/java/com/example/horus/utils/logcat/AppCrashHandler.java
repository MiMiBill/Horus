package com.example.horus.utils.logcat;

import android.util.Log;

import com.example.horus.BuildConfig;
import com.example.horus.app.MyApp;
import com.example.horus.utils.ActivityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by lognyun on 2019/6/17 15:32:25
 *
 * 全局的异常捕捉
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = AppCrashHandler.class.getSimpleName();

    private boolean showLog = BuildConfig.DEBUG;


    @Override
    public void uncaughtException(Thread t, Throwable e) {

        if (showLog) {
            Log.e(TAG, "Thread = " + t.getName() + "\nThrowable = " + e.getMessage());
        }


        String stackTraceInfo = getStackTraceInfo(e);
        if (showLog) {
            Log.e(TAG, "stackTraceInfo : \n" + stackTraceInfo);
        }


        String folder = AppCrashUtils.getLogFilePath();
        saveThrowableMessage(folder, stackTraceInfo);

        ActivityUtils.finishAllActivity();
    }


    /**
     * 获取错误信息
     *
     * @param throwable 异常
     * @return string
     */
    private String getStackTraceInfo(Throwable throwable) {
        PrintWriter pw = null;
        Writer writer = new StringWriter();

        try {
            pw = new PrintWriter(writer);
            throwable.printStackTrace(pw);
        } catch (Exception e) {
            return "";
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

        return writer.toString();
    }




    private void saveThrowableMessage(String logFilePath, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }

        File folder = new File(logFilePath);

        if (!folder.exists()) {
            boolean mkdirs = folder.mkdirs();
            if (mkdirs) {
                new Thread(() -> writeFile(message, folder)).start();
            }
        } else {
            new Thread(() -> writeFile(message, folder)).start();
        }
    }




    private void writeFile(String message, File folder) {
        String fileName = AppCrashUtils.getLogFileName();
        FileOutputStream os = null;
        if (showLog) {
            Log.e(TAG, "开始写日志--------------------------------------------------");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(message.getBytes());
            os = new FileOutputStream(new File(folder, fileName));
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
            if (showLog) {
                String filePath = folder.getAbsolutePath() + File.separator + fileName;
                Log.e(TAG, "成功捕获崩溃日志 :" + filePath);
            }

            uploadLogFile(folder.getAbsolutePath(), fileName);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }





    /**
     * 这个方法需要token才能上传
     * 所以针对上传需要考虑别的方案
     * 目前仅仅是将日志存在本地
     *
     * @param filePath
     * @param fileName
     */
    private void uploadLogFile(String filePath, String fileName) {
//        BizService bizService = BizService.getInstance(MyApp.getInstance());
//        bizService.setSrcPath(filePath);
//        bizService.setFileId(BizService.TYPE_LOG + "/" + fileName);
//
//        PutObjectSamples putObjectSamples = new PutObjectSamples(
//                BizService.TYPE_LOG, PutObjectSamples.PUT_TYPE.SAMPLE, new PutObjectSamples.onUpLoadListener() {
//
//            @Override
//            public void onFinish(List<String> fields) {
//                if (showLog) {
//                    Log.e(TAG, "崩溃日志上传完成");
//                }
//
//                // 上传完成就删除
//                File file = new File(filePath, fileName);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//
//            @Override
//            public void onProgress(long progress) {
//
//            }
//
//            @Override
//            public void onError(int code, String msg) {
//                if (showLog) {
//                    Log.e(TAG, "崩溃日志上传失败:" + msg);
//                }
//            }
//        });
//
//        putObjectSamples.executeOnExecutor(Executors.newCachedThreadPool(), bizService);
    }
}
