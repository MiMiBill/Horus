package com.example.horus.utils.converterfactory;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * 用于 retrofit 返回String
 */
public  final class StringResponseBodyConverter implements Converter<ResponseBody, String> {
    public static final StringResponseBodyConverter INSTANCE=new StringResponseBodyConverter();

    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }

}
