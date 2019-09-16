package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.User;
import com.sym.miaoshaodemo.redis.key.GoodKey;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private UserService userService;
//
//    @RequestMapping("/getAllUsers")
//    public List<User> getAllUsers(){
//        return userService.selectAll();
//    }

    @RequestMapping("/info")
    public Result<MiaoshaUser> info(MiaoshaUser user) {
        System.out.println(user.getNickname());
        return Result.success(user);
    }
}
