package com.sym.miaoshaodemo.service;

import com.sym.miaoshaodemo.dao.GoodMapper;
import com.sym.miaoshaodemo.domain.Goods;
import com.sym.miaoshaodemo.domain.MiaoshaGoods;
import com.sym.miaoshaodemo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: GoodService
 * @Package com.sym.miaoshaodemo.service
 * @Description: TODO
 * @date 2019/5/4 17:39
 */

@Service
public class GoodService{
    @Autowired
    private GoodMapper goodMapper;

    public List<GoodsVo> getAllGoodList(int id){
        return goodMapper.getAllGoodsList(id);
    }
}
