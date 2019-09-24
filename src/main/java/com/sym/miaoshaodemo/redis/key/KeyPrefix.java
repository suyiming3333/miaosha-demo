package com.sym.miaoshaodemo.redis.key;

public interface KeyPrefix {
		
	int expireSeconds();
	
	String getPrefix();
	
}
