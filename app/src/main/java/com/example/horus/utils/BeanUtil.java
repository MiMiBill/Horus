package com.example.horus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lognyun on 2019/3/19 17:52:04
 */
public class BeanUtil {


    /**
     * 将对象装换为map
     * @param bean
     * @return
     */
    /**
     * 将对象装换为map
     * @param object
     * @return
     */
    public static Map<String, Object> object2Map(Object object){
        JSONObject jsonObject = (JSONObject) JSON.toJSON(object);
        Set<Map.Entry<String,Object>> entrySet = jsonObject.entrySet();
        Map<String, Object> map=new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }


}
