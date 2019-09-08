package com.sym.miaoshaodemo.dao;

import com.sym.miaoshaodemo.domain.Goods;
import com.sym.miaoshaodemo.domain.MiaoshaGoods;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: GoodMapper
 * @Package com.sym.miaoshaodemo.dao
 * @Description: TODO
 * @date 2019/5/4 17:28
 */

@Mapper
public interface GoodMapper {

    List<GoodsVo> getAllGoodsList(@Param("id") int id);
}
