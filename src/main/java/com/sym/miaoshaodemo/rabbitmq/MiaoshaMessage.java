package com.sym.miaoshaodemo.rabbitmq;


import com.sym.miaoshaodemo.domain.MiaoshaUser;

public class MiaoshaMessage {
	private MiaoshaUser user;
	private Integer goodsId;
	public MiaoshaUser getUser() {
		return user;
	}
	public void setUser(MiaoshaUser user) {
		this.user = user;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
}
