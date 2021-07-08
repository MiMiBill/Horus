package com.example.horus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.baselib.utils.LogUtil;
import com.example.horus.app.MyApp;
import com.example.horus.utils.ActivityUtils;


public class NotifyClickBroadcastReceiver extends BroadcastReceiver {

    public static final String NOTIFY_CLICK_ACTION ="notify.click.action";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(intent.getAction(),NOTIFY_CLICK_ACTION)){
            String pushData = intent.getStringExtra("data");
            LogUtil.d("融云推送 NotifyClickBroadcastReceiver点击：" + intent.getStringExtra("data") );
            try {
                if (TextUtils.isEmpty(pushData)){
                    ActivityUtils.luanchCurrentApp(MyApp.getInstance());
                    return;
                }
//                SystemNotifyInfo systemNotifyInfo = MyApp.getInstance().getGson().fromJson(pushData, SystemNotifyInfo.class);
//                ProcessSystemNotifyManager.sharedInstance().openActivity(systemNotifyInfo);
            } catch (Exception e) {
                e.printStackTrace();
                ActivityUtils.luanchCurrentApp(context);
            }
        }

    }
}
