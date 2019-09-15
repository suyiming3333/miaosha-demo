package com.sym.miaoshaodemo.dao;

import com.sym.miaoshaodemo.domain.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: OrderMapper
 * @Package com.sym.miaoshaodemo.dao
 * @Description: TODO
 * @date 2019/9/15 22:08
 */

@Mapper
public interface OrderMapper {

    Map<String,Object> getExistsMiaoShaOrder(@Param("userId")int userId,@Param("goodsId")int goodsId);

    Integer addOrder(OrderInfo orderInfo);

    void addOrderRef(@Param("userId") int userId,@Param("orderIs")int orderIs,@Param("goodsId")int goodsId);
}
