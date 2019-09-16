package com.sym.miaoshaodemo.dao;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface MiaoshaUserDao {
	
	@Select("select * from ms_user where mobile = #{mobile}")
	public MiaoshaUser getByMobile(@Param("mobile")long mobile);
}
