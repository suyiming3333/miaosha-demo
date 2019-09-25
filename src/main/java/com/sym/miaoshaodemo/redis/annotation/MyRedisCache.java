package com.sym.miaoshaodemo.redis.annotation;

import com.sym.miaoshaodemo.redis.key.RedisKeyEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MyRedisCache
 * @Package com.sym.miaoshaodemo.redis.annotation
 * @Description: TODO
 * @date 2019/9/23 22:35
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRedisCache {
    RedisKeyEnum prefix();
    String key() default "";
    Class classType();
}
