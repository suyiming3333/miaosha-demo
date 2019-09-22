package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.service.GoodsService;
import com.sym.miaoshaodemo.service.MiaoshaUserService;
import com.sym.miaoshaodemo.service.OrderService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import com.sym.miaoshaodemo.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") int orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	int goodsId = order.getGoodsId();
    	List<GoodsVo> list = goodsService.getAllGoodList(goodsId);
    	GoodsVo goods = list.get(0);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
