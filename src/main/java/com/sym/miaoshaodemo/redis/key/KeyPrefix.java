package com.sym.miaoshaodemo.redis.key;

public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}
