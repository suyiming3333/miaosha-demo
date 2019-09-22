package com.sym.miaoshaodemo.redis.key;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: GoodKey
 * @Package com.sym.miaoshaodemo.redis
 * @Description: TODO
 * @date 2019/5/4 17:50
 */
public class GoodKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24 * 2;

    private GoodKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodKey token = new GoodKey(TOKEN_EXPIRE,"gd");

    public static GoodKey goodList = new GoodKey(120,"goodList");

    public static GoodKey goodDetail = new GoodKey(360,"goodDetail");

    public static GoodKey gDetail = new GoodKey(300,"gDetail");

    public static GoodKey goodStock = new GoodKey(0,"gStock");

    public static GoodKey isGoodsOver = new GoodKey(0,"gOver");

}
