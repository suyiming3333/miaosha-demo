package com.sym.miaoshaodemo.util;

import com.alibaba.fastjson.JSON;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: SerializerUtil
 * @Package com.sym.miaoshaodemo.util
 * @Description: TODO
 * @date 2019/9/24 15:18
 */
public class SerializerUtil {

    /**
     * json序列化
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToJsonString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * json反序列化
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonStringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


}
