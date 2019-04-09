package com.sym.miaoshaodemo.dao;

import com.sym.miaoshaodemo.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> selectAll();
}
