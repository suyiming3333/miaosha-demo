package com.sym.miaoshaodemo.controller;

import com.sym.miaoshaodemo.domain.User;
import com.sym.miaoshaodemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userService.selectAll();
    }
}
