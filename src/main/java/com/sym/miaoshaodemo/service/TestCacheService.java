package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCache;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCacheEvict;
import com.sym.miaoshaodemo.redis.annotation.MyRedisCachePut;
import com.sym.miaoshaodemo.redis.key.OrderKey;
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

    @MyRedisCache(prefix = "test",key = "id",expire = 600 ,classType = String.class)
    public String testMethod(String id){
        return "test-redis-value:id"+id;
    }

    @MyRedisCacheEvict(prefix = "test",key = "id")
    public void testEvict(String id){
        System.out.println("删除成功");
    }

    @MyRedisCachePut(prefix = "test",key = "id",expire = 600 ,classType = String.class)
    public String testPut(String id){
        return "test-redis-value:id"+id;
    }

    @MyRedisCache(prefix = "OrderKey",key = "id",classType = OrderInfo.class)
    public OrderInfo testOrder(Integer id){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(id);
        return orderInfo;
    }

}
