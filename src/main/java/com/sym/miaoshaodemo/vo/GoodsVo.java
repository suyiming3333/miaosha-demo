package com.sym.miaoshaodemo.vo;

import com.sym.miaoshaodemo.domain.Goods;

import java.util.Date;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: GoodVo
 * @Package com.sym.miaoshaodemo.vo
 * @Description: TODO
 * @date 2019/9/8 23:16
 */
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    public Integer getStockCount() {
        return stockCount;
    }
    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }
    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
