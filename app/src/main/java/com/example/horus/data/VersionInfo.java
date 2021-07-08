package com.example.horus.data;

public class VersionInfo {
    private String channelPackCode;// 渠道code
    private String currentVersionCode;// 新版本编号
    private String currentVersionType;// 1小 2中 3大
    private String currentUpdateApk;// 最新Apk地址
    private String vipCode;// VIP编号（干啥用的）

    private long updateTime;// 上次更新时间
    private boolean middleClosed;// 中版本被关闭

    public String getChannelPackCode() {
        return channelPackCode;
    }

    public void setChannelPackCode(String channelPackCode) {
        this.channelPackCode = channelPackCode;
    }

    public String getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(String currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public String getCurrentVersionType() {
        return currentVersionType;
    }

    public void setCurrentVersionType(String currentVersionType) {
        this.currentVersionType = currentVersionType;
    }

    public String getCurrentUpdateApk() {
        return currentUpdateApk;
    }

    public void setCurrentUpdateApk(String currentUpdateApk) {
        this.currentUpdateApk = currentUpdateApk;
    }

    public String getVipCode() {
        return vipCode;
    }

    public void setVipCode(String vipCode) {
        this.vipCode = vipCode;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isMiddleClosed() {
        return middleClosed;
    }

    public void setMiddleClosed(boolean middleClosed) {
        this.middleClosed = middleClosed;
    }
}
