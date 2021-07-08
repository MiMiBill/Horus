package com.example.horus.utils.converterfactory;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 用于 retrofit 返回String
 */
public class StringConverterFactory extends Converter.Factory{

    public static final StringConverterFactory INSTANCE=new StringConverterFactory();
    public static StringConverterFactory create(){
        return INSTANCE;
    }

    public Converter<ResponseBody,?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit){
        if(type==String.class){
            return StringResponseBodyConverter.INSTANCE;
        }
        return null;
    }

}
