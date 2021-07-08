package com.example.baselib.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class CompatibleUtil {

    /**
     * 适配android11 Build.VERSION_CODES.R 以上不能使用getExternalStorageDirectory
     * @return
     */
    public static File getExternalStoragePath(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q )
        {
            return context.getExternalFilesDir(null);
        }else {
            return Environment.getExternalStorageDirectory();
        }
    }


}
