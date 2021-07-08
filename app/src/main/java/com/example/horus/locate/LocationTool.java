package com.example.horus.locate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.example.baselib.utils.LogUtil;

import java.io.IOException;
import java.util.List;


/**
 * Created by Liao on 2019/2/14 17:51:36
 *
 * 定位相关工具
 *
 * 由于GPS定位可能拿不到回调
 * 所以同时取网络定位和GPS定位（需要保证回调不同）
 *
 * 如果一段时间后没有拿到数据，需要getLastKnownLocation吗？
 *
 * 如果用户开了位置服务开关，但设置仅GPS定位，则大概率拿不到数据（如何处理？）
 */
public class LocationTool {
    private static final String TAG = LocationTool.class.getSimpleName();

    private static final int GPS_LOCATION_TIMEOUT = 5000;// 5秒

    private static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;
    private static final String NET_PROVIDER = LocationManager.NETWORK_PROVIDER;

    private static final boolean DEBUG_LOG = true;

    private Context mContext;

    private Location mLocation;
    private LocationManager mLocationManager;
    private OnLocationToolListener mOnLocationToolListener;

    private boolean serviceAvailable = false;


    /**
     *
     * @param context
     */
    public LocationTool(Context context) {
        mContext = context.getApplicationContext();
        init();
    }


    private void init() {
        if (mContext == null) return;

        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (mLocationManager == null){
            log("LocationManager is null!");
            return;
        }

        serviceAvailable = true;
    }


    private void updateLocation(Location location) {
        if (location != null) {
            mLocation = location;
            locateEnd();
            startLocationGeocoder(mLocation);
        }
    }

    private void showLastLocation(Location location) {
        if (location != null) {
            startLocationGeocoder(location);
        }
    }

    /**
     * 用Geocoder解码Location中的经纬度信息
     */
    private void startLocationGeocoder(Location location) {
        if (mContext != null && location != null && Geocoder.isPresent()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Address address = getAddressFromLocate(location);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            if (mOnLocationToolListener != null) {
                                mOnLocationToolListener.onLocateFinished(address);
                            }

                        }
                    });
                }
            }).start();
        }
    }

    /**
     * 解码是耗时操作，放在子线程处理
     * @param location
     * @return Address
     */
    private Address getAddressFromLocate(Location location) {
        Address address = null;
        List<Address> addList = null;
        Geocoder geocoder = new Geocoder(mContext.getApplicationContext());
        try {
            addList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addList != null && !addList.isEmpty()) {
            address = addList.get(0);
        }

        return address;
    }


    /**
     * 记得清空定位监听 与locate配套使用
     */
    public void locateEnd() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mGpsLocationListener);
            mLocationManager.removeUpdates(mNetLocationListener);
            // mLocationManager = null;// 有这句的话locate就没法走第二次了
            // locate()方法可能会多次调用（用户可能会反复开启/关闭开关）
        }
    }


    @SuppressLint("MissingPermission")
    public void locate() {
        if (mLocationManager == null) return;// 会不会有服务不可用的情况？

        if (mOnLocationToolListener != null) {
            mOnLocationToolListener.onLocateEnabled(isLocationEnabled());
        }

        // 获取上次定位
        Location lastLocation = mLocationManager.getLastKnownLocation(GPS_PROVIDER);
        if (lastLocation == null) {
            lastLocation = mLocationManager.getLastKnownLocation(NET_PROVIDER);
        }
        if (lastLocation != null) {
            showLastLocation(lastLocation);
        }


//        没有GPS的时候会崩溃
        // 注册监听
//        try {
//            mLocationManager.requestSingleUpdate(GPS_PROVIDER, mGpsLocationListener, null);
//
//        }catch (Exception e){
//            BuglyUtils.postCatchedException(e);
//        }
//
//        try {
//            mLocationManager.requestSingleUpdate(NET_PROVIDER, mNetLocationListener, null);
//        }catch (Exception e){
//            BuglyUtils.postCatchedException(e);
//        }

    }

    public void locate(OnLocationToolListener listener) {
        mOnLocationToolListener = listener;
        locate();
    }


    /**
     * 判断服务是否可用
     * @return true/false
     */
    public boolean isServiceAvailable() {
        return serviceAvailable;
    }


    /**
     * 判断定位开关是否开启
     * 官方建议使用 isProviderEnabled 判断（但博客有说不准确）
     *
     * 实测小米4（Android 6.0 / MIUI 8.0）
     * 下拉面板中的定位GPS开关和长按进入设置内的位置服务开关是分离的
     * 位置服务关闭 - GPS一定关闭
     * GPS关闭 - 位置服务还开着
     *
     * @return true/false
     */
//    public boolean isLocationEnabled() {
//        int locationMode = 0;
//        String locationProviders;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
//        } else {
//            locationProviders = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//            return locationProviders != null && !locationProviders.isEmpty();
//        }
//    }

    public boolean isLocationEnabled() {
        if (mLocationManager == null) return false;

        boolean gpsEnable = mLocationManager.isProviderEnabled(GPS_PROVIDER);
        boolean netEnable = mLocationManager.isProviderEnabled(NET_PROVIDER);
        return gpsEnable || netEnable;
    }



    private LocationListener mGpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            log("On GPS Location Changed---------");
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            log("On Status changed:" + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            log("On provider enabled:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            log("On provider disabled:" + provider);
        }
    };






    private LocationListener mNetLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            log("On NET Location Changed---------");
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            log("On Status changed:" + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            log("On provider enabled:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            log("On provider disabled:" + provider);
        }
    };


    public void setOnLocationToolListener(OnLocationToolListener listener) {
        mOnLocationToolListener = listener;
    }

    public void onDestroy() {
        mOnLocationToolListener=null;
    }

    /**
     * 回调
     */
    public interface OnLocationToolListener {

        /**
         * 得到Address信息
         * @param address 包含经纬度/名称等
         */
        void onLocateFinished(Address address);

        /**
         * 网络是否可用
         * @param enabled true/false
         */
        void onLocateEnabled(boolean enabled);
    }


    private void log(String msg) {
        if (DEBUG_LOG) {
            Log.e(TAG, msg);
        }
    }




    /**
     * 写在这里方便记录
     */
    public static void toLocationSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 获取所需地址
     * @param address 位置
     * @return 国家.省份.城市（三级地址）
     */
    public static String getCityName(Address address) {
        String name = "";
        if (address != null) {

            // 国家
            if (address.getCountryName() != null) {
                name += address.getCountryName();
            }


            String adminArea=address.getAdminArea();
            // 一级行政划分
            if (!TextUtils.isEmpty(adminArea)) {
                if (!name.isEmpty()) {
                    name += ".";
                }
                name += adminArea;
            }

            // 二级行政划分 subAdmin 可能为空
            // 也可能会被过滤掉
            String subArea = "";
            if (address.getSubAdminArea() != null) {
                subArea = filterSubAdmin(address.getSubAdminArea());

            }



            if (TextUtils.isEmpty(subArea)) {

                String locality = "";// locality也可能为上海市（坑爹）
                if (address.getLocality() != null) {
                    locality = filterSubAdmin(address.getLocality());
                }

                if (TextUtils.isEmpty(locality)) {
                    locality = address.getSubLocality();

                    if (TextUtils.isEmpty(locality)) {
                        LogUtil.e(TAG, "没有二级行政区域信息！");
                    }
                }


                if(!TextUtils.isEmpty(locality)){
                    subArea=locality;
                }
            }



            if ( !TextUtils.isEmpty(subArea)&&!TextUtils.equals(adminArea,subArea)) {
                if (!TextUtils.isEmpty(name)) {
                    name += ".";
                }
                name += subArea;
            }
        }

        return name;
    }


    /**
     * 这些区域出现在二级行政区时需过滤
     * 2019/04/18 16:50 已确认仅处理简体中文
     */
    private static String filterSubAdmin(String subAdmin) {
        String[] subAdminFilterArr = new String[] {
                "北京","北京市",
                "天津","天津市",
                "上海","上海市",
                "重庆","重庆市",
        };

        for (String aSubAdminFilterArr : subAdminFilterArr) {
            if (aSubAdminFilterArr.equals(subAdmin)) {
                return "";
            }
        }
        return subAdmin;
    }

}
