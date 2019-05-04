package com.sym.miaoshaodemo.redis;

/**
 * 秒杀用户的key
 */
public class MiaoshaUserKey extends BasePrefix{

    //定义秒杀用户key有效时间
	public static final int TOKEN_EXPIRE = 3600*24 * 2;

	//构造方法调用父累构造方法，设置前缀以及有效时间
	private MiaoshaUserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	//单例调用token生成方法
	public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
}
