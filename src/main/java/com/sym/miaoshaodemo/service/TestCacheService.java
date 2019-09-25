package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCache;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCacheEvict;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCachePut;
import com.sym.miaoshaodemo.redis.key.RedisKeyEnum;
import org.springframework.stereotype.Service;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: TestCacheService
 * @Package com.sym.miaoshaodemo.service
 * @Description: TODO
 * @date 2019/9/24 10:19
 */
@Service
public class TestCacheService {

    @MyRedisCache(prefix = RedisKeyEnum.GOODS_STOCK_KEY,key = "id" ,classType = String.class)
    public String testMethod(String id){
        return "test-redis-value:id"+id;
    }

    @MyRedisCache(prefix = RedisKeyEnum.GOODS_STOCK_KEY,key = "isEmpty" ,classType = String.class)
    public String testMethod2(){
        return "test-redis-value:true";
    }

    @MyRedisCacheEvict(prefix = RedisKeyEnum.MS_ORDER_KEY,key = "id")
    public void testEvict(String id){
        System.out.println("删除成功");
    }

    @MyRedisCachePut(prefix = RedisKeyEnum.GOODS_STOCK_KEY,key = "id" ,classType = String.class)
    public String testPut(String id){
        return "test-redis-value:id"+id;
    }

    @MyRedisCache(prefix = RedisKeyEnum.MS_ORDER_KEY,key = "id",classType = OrderInfo.class)
    public OrderInfo testOrder(Integer id){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(id);
        return orderInfo;
    }

}
