package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.dao.UserMapper;
import com.sym.miaoshaodemo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> selectAll(){
        return userMapper.selectAll();
    }
}
