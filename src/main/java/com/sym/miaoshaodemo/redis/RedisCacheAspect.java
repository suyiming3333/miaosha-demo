package com.sym.miaoshaodemo.redis;

import com.sym.miaoshaodemo.redis.annotation.MyRedisCache;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCacheEvict;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCachePut;
import com.sym.miaoshaodemo.redis.config.RedisUtil;
import com.sym.miaoshaodemo.redis.key.KeyPrefix;
import com.sym.miaoshaodemo.redis.key.RedisKeyEnum;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.util.SerializerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(com.sym.miaoshaodemo.redis.annotation.MyRedisCache)")
    public void redisCachePointcut() {
    }

    @Pointcut("@annotation(com.sym.miaoshaodemo.redis.annotation.MyRedisCacheEvict)")
    public void redisCacheEvictPointcut() {
    }

    @Pointcut("@annotation(com.sym.miaoshaodemo.redis.annotation.MyRedisCachePut)")
    public void redisCachePutPointcut() {
    }

    @Around("redisCachePutPointcut()")
    public void putCache(ProceedingJoinPoint joinPoint) {
        //获取方法参数值
        Object[] args = joinPoint.getArgs();
        //获取方法签名
        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        //获取参数名
        String[] parameterNames = methodSignature.getParameterNames();
        //获取参数类型
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];

        if(args.length>0){
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        }

        Method method = null;
        Object value = null;
        try {
            method = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(), argTypes);

            //获取方法上面的注解
            MyRedisCachePut myRedisCache = method.getAnnotation(MyRedisCachePut.class);
            String key = myRedisCache.key();
            RedisKeyEnum prefix = myRedisCache.prefix();
            Class classType = myRedisCache.classType();
            String realKey = null;

            //如果注解有指定key 而且方法参数不为空
            if(key !=null && args.length>0){
                for(int i=0;i<parameterNames.length;i++){
                    if(key.equals(parameterNames[i])){
                        realKey = (String) args[i];
                        break;
                    }
                }
            }

            value = joinPoint.proceed();

            //设置缓存
            redisUtil.set(prefix.getPrefixKey(),realKey,prefix.getExpire(),value);
            System.out.println("redis缓存成功");

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Around("redisCacheEvictPointcut()")
    public void evictCache(ProceedingJoinPoint joinPoint) {
        //获取方法参数值
        Object[] args = joinPoint.getArgs();
        //获取方法签名
        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        //获取参数名
        String[] parameterNames = methodSignature.getParameterNames();
        //获取参数类型
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];

        if(args.length>0){
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        }

        Method method = null;
        Object value = null;
        try {
            method = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(), argTypes);

            //获取方法上面的注解
            MyRedisCacheEvict myRedisCache = method.getAnnotation(MyRedisCacheEvict.class);
            String key = myRedisCache.key();
            RedisKeyEnum prefix = myRedisCache.prefix();
            String realKey = null;

            //如果注解有指定key 而且方法参数不为空
            if(key !=null && args.length>0){
                for(int i=0;i<parameterNames.length;i++){
                    if(key.equals(parameterNames[i])){
                        realKey = (String) args[i];
                        break;
                    }
                }
            }

            //删除缓存
            redisUtil.del(prefix.getPrefixKey(),realKey);
            System.out.println("redis清除缓存"+prefix+"-"+realKey);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Around("redisCachePointcut()")
    public Object doCache(ProceedingJoinPoint joinPoint) {
        //获取方法参数值
        Object[] args = joinPoint.getArgs();
        //获取方法签名
        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        //获取参数名
        String[] parameterNames = methodSignature.getParameterNames();
        //获取参数类型
        Class<?>[] argTypes = new Class[joinPoint.getArgs().length];

        if(args.length>0){
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
        }

        Method method = null;
        Object value = null;
        try {
            method = joinPoint.getTarget().getClass()
                    .getMethod(joinPoint.getSignature().getName(), argTypes);

            //获取方法上面的注解
            MyRedisCache myRedisCache = method.getAnnotation(MyRedisCache.class);
            String key = myRedisCache.key();
            RedisKeyEnum prefix = myRedisCache.prefix();
            Class classType = myRedisCache.classType();
            String realKey = key;

            //如果注解有指定key 而且方法参数不为空
            if(key !=null && args.length>0){
                for(int i=0;i<parameterNames.length;i++){
                    if(key.equals(parameterNames[i])){
                        if (args[i] instanceof Integer){
                            realKey = String.valueOf(args[i]);
                        }

                        if(args[i] instanceof String){
                            realKey = (String)args[i];
                        }

                        break;
                    }
                }
            }

            //尝试从缓存取值
            value = redisUtil.get(prefix.getPrefixKey(),realKey,classType);

            //存在直接返回值
            if(value !=null){
                System.out.println("redis 里面已经有值啦");
                return value;
            }

            //不存在，执行方法查询数据库
            value = joinPoint.proceed();
            System.out.println("redis 里面没有值，只能充数据库拿");

            //设置缓存
            redisUtil.set(prefix.getPrefixKey(),realKey,prefix.getExpire(),value);


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return value;
    }

}
