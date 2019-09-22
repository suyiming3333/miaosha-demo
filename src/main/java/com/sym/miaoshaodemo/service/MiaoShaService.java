package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.key.GoodKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MiaoShaService
 * @Package com.sym.miaoshaodemo.service
 * @Description: TODO
 * @date 2019/9/15 22:31
 */

@Service
public class MiaoShaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Transactional(rollbackFor = RuntimeException.class)
    public OrderInfo doMiaoSha(MiaoshaUser user, GoodsVo goods){
//        //减少库存
//        goodsService.reduceStock(goods.getId());
//        //创建订单、订单商品关联表记录添加
//        return orderService.createOrder(user,goods);

        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods.getId());
        if(success) {
            //order_info maiosha_order
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public int getMiaoshaResult(int userId,int goodsId){
        OrderInfo order = orderService.getHasMiaoShaOrder(userId, goodsId);
        if(order != null) {//秒杀成功
            return order.getId();
        }else {
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
//        return orderService.getMiaoshaResult(userId,goodsId);
    }

    private boolean getGoodsOver(int goodsId) {
        return redisService.exists(GoodKey.isGoodsOver.isGoodsOver, ""+goodsId);
    }

    private void setGoodsOver(int goodsId) {
        redisService.set(GoodKey.isGoodsOver, ""+goodsId, true);
    }
}
