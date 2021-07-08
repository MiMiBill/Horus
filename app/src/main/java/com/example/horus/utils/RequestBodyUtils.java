package com.example.horus.utils;

import okhttp3.RequestBody;

/**
 * des: 支付相关的参数需要直接传个 body
 * author: lognyun
 * date: 2018/10/29 15:48
 */
public class RequestBodyUtils {

    public static RequestBody string2RequestBody(String request){
      return RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), request);
    }

}
