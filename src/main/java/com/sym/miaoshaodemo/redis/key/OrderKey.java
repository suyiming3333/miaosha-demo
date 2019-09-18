package com.sym.miaoshaodemo.redis.key;

public class OrderKey extends BasePrefix {

	public OrderKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static OrderKey msOrder = new OrderKey(0,"msOrder");
}
