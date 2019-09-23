package com.sym.miaoshaodemo.redis.annotation;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MyRedisCache
 * @Package com.sym.miaoshaodemo.redis.annotation
 * @Description: TODO
 * @date 2019/9/23 22:35
 */
public @interface MyRedisCache {
    String prefix();
    String key();
    int expire() default -1;
}
