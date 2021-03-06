package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.service.MiaoshaUserService;
import com.sym.miaoshaodemo.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: LoginController
 * @Package com.sym.miaoshaodemo.controller
 * @Description: TODO
 * @date 2019/4/28 22:04
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * @description: TODO　　
     * @params
     * @param response
     * @param loginVo
     * @return com.sym.miaoshaodemo.result.Result<java.lang.Boolean>
     * @throws
     * @author suyiming3333
     * @date 2019/5/4 15:32
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        userService.login(response, loginVo);
        return Result.success(true);
    }

    @RequestMapping("/do_login2")
    @ResponseBody
    public Result<String> doLogin2(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        return Result.success(userService.login2(response, loginVo));
    }
}
