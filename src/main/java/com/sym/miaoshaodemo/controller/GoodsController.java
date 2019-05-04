package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.Good;
import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.redis.GoodKey;
import com.sym.miaoshaodemo.redis.RedisService;
import com.sym.miaoshaodemo.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
    private GoodService goodService;
	
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
        List<Good> list = goodService.getAllGoodList(1);
        redisService.set(GoodKey.token,"type1",list);
    	model.addAttribute("user", user);
        return "goods_list";
    }

    @RequestMapping("/getListFromRedis")
    @ResponseBody
    public String getListFromRedis() {
        return redisService.get(GoodKey.token,"type1",String.class);
    }
    
}
