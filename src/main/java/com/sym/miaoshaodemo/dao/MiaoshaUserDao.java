package com.sym.miaoshaodemo.dao;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface MiaoshaUserDao {
	
	@Select("select * from ms_user where id = #{id}")
	public MiaoshaUser getById(@Param("id")long id);
}
