package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.service.GoodsService;
import com.sym.miaoshaodemo.service.MiaoShaService;
import com.sym.miaoshaodemo.service.OrderService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: MiaoShaController
 * @Package com.sym.miaoshaodemo.controller
 * @Description: TODO
 * @date 2019/9/15 21:59
 */

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController {

    @Autowired
    private GoodsService goodService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoShaService miaoShaService;


    @RequestMapping("/do_miaosha")
    public String doMiaoSha(Model model,
                            MiaoshaUser user,
                            @RequestParam("goodsId")int goodsId){

        model.addAttribute("user", user);
        if(user == null) {
            return "login";
        }

        //判断库存数量
        List<GoodsVo> list = goodService.getAllGoodList(goodsId);
        GoodsVo goodsVo = list.get(0);
        /**
         * stock =1,此时有同一个用户，有两个request进来，继续往下执行
         * (解决同一个用户同时有两个请求的过来的方法：秒杀前要输入验证码)
         *
         */
        if(goodsVo.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        /**
         * 此时该用户还没创建过秒杀订单，继续往下执行
         * (可以通过goodsId userId 唯一索引去控制)
         * create unique index index_user_good on ms_order_ref (userId,goodsId)
         */
        //判断是否已经秒杀过
        Map<String,Object> orderMap = orderService.getExistsMiaoShaOrder(user.getId(),goodsId);
        if(orderMap != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        /**
         * 同时减库存，下订单，创建秒杀订单，导致超卖
         */
        //减库存、记录秒杀订单，下订单(需要再同一个事务执行)
        OrderInfo orderInfo = miaoShaService.doMiaoSha(user,goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
}
