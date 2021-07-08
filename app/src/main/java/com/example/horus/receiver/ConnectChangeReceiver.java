package com.example.horus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;



public class ConnectChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectChangeReceiver";
    public static final String RECONNECT_ACTION = "action_reconnect";

    public ConnectChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo networkInfo = null;

            try {
                networkInfo = cm.getActiveNetworkInfo();
            } catch (Exception var7) {
            }

            boolean networkAvailable = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && networkAvailable) {
//                connectIM();

            } else if (intent.getAction().equals("action_reconnect") && networkAvailable) {
//                connectIM();

            } else {
//                RongIMClient.ConnectionStatusListener.ConnectionStatus state;
//                if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
//                    state = RongIMClient.getInstance().getCurrentConnectionStatus();
//                    if (state.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) && networkAvailable) {
//                        connectIM();
//
//                    }
//                } else if (intent.getAction().equals("action_reconnect") && networkInfo != null && networkInfo.isAvailable() && !networkInfo.isConnected()) {
//                    state = RongIMClient.getInstance().getCurrentConnectionStatus();
//                    if (state.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE) && networkAvailable) {
//                        connectIM();
//                    }
//                } else {
//                }
            }

        }
    }


    /**
     * 网络不好
     * 主动断过一次网
     * 需要自己重新连接
     */
    private void connectIM(){
//        if(UserUtil.loadUser()){
//            TranslateSwitchModel model = new TranslateSwitchModel();
//            model.getFuncSwitch();
//
//            EventBus.getDefault().post(new NetReConnectionEvent());
//            Context context=MyApp.getInstance();
//            SharedPreferences sp =context .getSharedPreferences("Statistics", 0);
//
//            UserInfo userInfo=MyApp.getInstance().getUserInfo();
//            String token = MyApp.getInstance().getToken();
//
//            if(TextUtils.isEmpty(sp.getString("token",""))){
//                RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
//
//
//                    @Override public void onSuccess(String s) {
//                        LogUtil.e("网络变化重连-->    onSuccess ");
//
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ConnectionErrorCode connectionErrorCode) {
//
//                    }
//
//                    @Override
//                    public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus databaseOpenStatus) {
//
//                    }
//
//
//                });
//            }
//
//
//        }


    }
}