package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.exception.GlobalException;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    //应该在全局声明锁对象
    private Lock lock = new ReentrantLock();



    /**
     * todo 用了lock锁还是会出现超卖情况
     * @param user
     * @param goods
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public OrderInfo doMiaoSha2(MiaoshaUser user, GoodsVo goods){

        //局部声明锁，每个线程调用的时候都会新创建一个副本，导致锁不生效，还是会超卖
//        Lock lock = new ReentrantLock();
        try{
            lock.lock();
            //判断库存数量
            List<GoodsVo> list = goodsService.getAllGoodList(goods.getId());
            GoodsVo goodsVo = list.get(0);
            if(goodsVo.getStockCount()>0){
                //减少库存
                goodsService.reduceStock(goods.getId());
                //创建订单、订单商品关联表记录添加
                return orderService.createOrder(user,goods);
            }else{
                throw new RuntimeException("mmiaoshao over");
            }
        }finally {
            lock.unlock();
        }


    }

    /**
     * 同步的秒杀方法，防止商品超卖
     * @param user
     * @param goods
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public synchronized OrderInfo doMiaoSha(MiaoshaUser user, GoodsVo goods){

        //判断库存数量
        List<GoodsVo> list = goodsService.getAllGoodList(goods.getId());
        GoodsVo goodsVo = list.get(0);
        if(goodsVo.getStockCount()>0){
            //减少库存
            goodsService.reduceStock(goods.getId());
            //创建订单、订单商品关联表记录添加
            return orderService.createOrder(user,goods);
        }else{
            throw new GlobalException(CodeMsg.MIAO_SHA_OVER);
        }

    }
}
