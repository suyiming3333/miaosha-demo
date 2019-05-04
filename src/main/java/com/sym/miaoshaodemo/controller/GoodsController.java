package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.redis.RedisService;
import com.sym.miaoshaodemo.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	MiaoshaUserService userService;
	
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
    	model.addAttribute("user", user);
        return "goods_list";
    }
    
}
