package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.dao.OrderMapper;
import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.key.OrderKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: OrderService
 * @Package com.sym.miaoshaodemo.service
 * @Description: TODO
 * @date 2019/9/15 22:21
 */

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    RedisService redisService;

    public Map<String,Object> getExistsMiaoShaOrder(int userId,int goodsId){
        return orderMapper.getExistsMiaoShaOrder(userId,goodsId);
    }

    public OrderInfo getHasMiaoShaOrder(int userId,int goodsId){
//        return orderMapper.getHasMiaoShaOrder(userId,goodsId);
        return redisService.get(OrderKey.msOrder,userId+"-"+goodsId,OrderInfo.class);

    }

    public OrderInfo getOrderById(int orderId) {
        return orderMapper.getOrderById(orderId);
    }

    public int getMiaoshaResult(int userId,int goodsId){
        OrderInfo orderInfo = redisService.get(OrderKey.msOrder,userId+"-"+goodsId,OrderInfo.class);
        return orderInfo.getId();
    }


    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        int ret = orderMapper.addOrder(orderInfo);
        int orderId = orderInfo.getId();
        orderMapper.addOrderRef(user.getId(),orderId,goods.getId());
        //创建订单成功后将订单信息存入缓存
        redisService.set(OrderKey.msOrder,user.getId()+"-"+goods.getId(),orderInfo);
        return orderInfo;
    }
}
