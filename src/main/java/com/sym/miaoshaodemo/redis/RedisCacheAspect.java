package com.sym.miaoshaodemo.redis;

import com.sym.miaoshaodemo.redis.annotation.MyRedisCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: RedisCacheAspect
 * @Package com.sym.miaoshaodemo.redis
 * @Description: TODO
 * @date 2019/9/23 22:53
 */
@Component
@Aspect
public class RedisCacheAspect {

    @Pointcut("@annotation(com.sym.miaoshaodemo.redis.annotation.MyRedisCache)")
    public void redisCachePointcut() {
    }

    @Around("redisCachePointcut()")
    public Object doCache(ProceedingJoinPoint joinPoint) {
        try {
            //获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取方法
            Method method = joinPoint.getTarget().getClass().getMethod(
                    signature.getName(),
                    signature.getMethod().getParameterTypes());
            //获取方法上面的注解
            MyRedisCache myRedisCache = method.getAnnotation(MyRedisCache.class);


            ExpressionParser parser = new SpelExpressionParser();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
