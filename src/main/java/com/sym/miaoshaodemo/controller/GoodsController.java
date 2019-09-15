package com.sym.miaoshaodemo.controller;


import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.exception.GlobalException;
import com.sym.miaoshaodemo.redis.key.GoodKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.service.GoodsService;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
    private GoodsService goodService;
	
	@Autowired
	RedisService redisService;

    /**
     * @description: TODO　　
     * @params
     * @param model
     * @param user webconfig里添加的自定义参数，便于获取用户信息
     * @return java.lang.String
     * @throws
     * @author suyiming3333
     * @date 2019/5/4 15:23
     */
    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user) {
        List<GoodsVo> list = goodService.getAllGoodList(0);
        redisService.set(GoodKey.token,"type1",list);
    	model.addAttribute("user", user);
        model.addAttribute("goodsList", list);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,MiaoshaUser user,
                         @PathVariable("goodsId")int goodsId) {
        model.addAttribute("user", user);

        List<GoodsVo> goodList = goodService.getAllGoodList(goodsId);
        if(goodList.size()<0){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        GoodsVo goods = goodList.get(0);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }

    @RequestMapping("/getListFromRedis")
    @ResponseBody
    public String getListFromRedis() {
        return redisService.get(GoodKey.token,"type1",String.class);
    }
    
}
