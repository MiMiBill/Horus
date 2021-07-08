package com.example.horus.retrofit;





public class BaseResponse<T> {
    private int code;// 状态码
    private String info;// 描述/错误信息
    private T data;// JSON数据
    private String reqCode;// 请求码，可用于辨别请求是否一致
    private boolean noEndLoading;


    private boolean isCache;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public boolean isNoEndLoading() {
        return noEndLoading;
    }

    public void setNoEndLoading(boolean noEndLoading) {
        this.noEndLoading = noEndLoading;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }
}
