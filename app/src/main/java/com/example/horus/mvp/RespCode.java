package com.example.horus.mvp;

import android.content.Context;
import android.util.SparseArray;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.horus.R;
import com.example.horus.app.MyApp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class RespCode {
    private static Context sContext;
    private static String sLanguageName;
    private static final String FILE_NAME_RRE="error_code/error_code_";
    private static SparseArray<String> errorCodeArr;

    public static final int SUCCESS = 0;

    public static final int SYS_ERROR = -1;
    public static final int NO_LOGIN = -2;

    public static final int RESP_NULL = -100;
    public static final int DATA_NULL = -101;
    public static final int NET_ERROR = -102;// 指请求抛出异常

    // java.net.ConnectException: Failed to connect to /150.109.61.188:8080
    public static final int NET_DISABLE = -103;

    public static final int NET_TIMEOUT = -104;

    public static void init(Context context) {
        sContext = context;

        String fileName = getFileName(context);

        errorCodeArr = getErrorCodeArr(fileName);

        errorCodeArr.append(NET_DISABLE, context.getString(R.string.no_network));
        errorCodeArr.append(NET_TIMEOUT, context.getString(R.string.timeout));
    }

    public static String getErrorMessage(int code) {
        return getErrorMessage(code,"没有对应的错误信息：" + code);
    }

    public static String getErrorMessage(int code, String defaultErrorMsg) {
        if(errorCodeArr==null){
            init(MyApp.getInstance());
        }
        if(errorCodeArr==null){
            return defaultErrorMsg;
        }

        String msg = errorCodeArr.get(code);

        if (msg == null) {
            msg = sContext.getString(R.string.error_data); //没有对应code的提示，默认"数据异常，请重试"
        }
        return msg;
    }

    private static SparseArray<String> getErrorCodeArr(String fileName) {
        errorCodeArr = new SparseArray<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(sContext.getResources().getAssets().open(fileName)));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();

            JSONArray ja = JSON.parseArray(sb.toString());

            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int code = jo.getIntValue("code");
                String info = jo.getString("info");
                errorCodeArr.append(code, info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errorCodeArr;
    }

    public static void clear() {
        errorCodeArr=null;
    }



    /**
     * asset目录需要放好位置
     */
    private static String getFileName(Context context) {
        String fileName = null;
        try {
            String testName = "error_code_zh-CN";
            context.getAssets().open("error_code/" + testName + ".json");
            fileName = "error_code/" + testName + ".json";
        } catch (IOException e) {
            // e.printStackTrace();
            // 异常信息不用打印
        }
        if (fileName == null) {
            fileName = "error_code/error_code_en.json";
        }
        return fileName;
    }
}
