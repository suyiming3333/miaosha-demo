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
        if(goodsVo.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        //判断是否已经秒杀过
        Map<String,Object> orderMap = orderService.getExistsMiaoShaOrder(user.getId(),goodsId);
        if(orderMap != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }

        //减库存、记录秒杀订单，下订单(需要再同一个事务执行)
        OrderInfo orderInfo = miaoShaService.doMiaoSha(user,goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
}
