package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.dao.OrderMapper;
import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
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

    public Map<String,Object> getExistsMiaoShaOrder(int userId,int goodsId){
        return orderMapper.getExistsMiaoShaOrder(userId,goodsId);
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
        int orderId = orderMapper.addOrder(orderInfo);
        orderMapper.addOrderRef(user.getId(),orderId,goods.getId());
        return orderInfo;
    }
}
