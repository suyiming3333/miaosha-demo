package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
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

    @Transactional(rollbackFor = RuntimeException.class)
    public OrderInfo doMiaoSha(MiaoshaUser user, GoodsVo goods){
        //减少库存
        goodsService.reduceStock(goods.getId());
        //创建订单、订单商品关联表记录添加
        return orderService.createOrder(user,goods);
    }
}
