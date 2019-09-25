package com.sym.miaoshaodemo.redis.key;

import org.apache.commons.lang3.StringUtils;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: RedisKeyEnum
 * @Package com.sym.miaoshaodemo.redis.key
 * @Description: TODO
 * @date 2019/9/25 10:11
 */
public enum RedisKeyEnum {

    /**秒杀订单**/
    MS_ORDER_KEY("OrderKey:ms",-1,"秒杀订单key"),
    /**商品库存**/
    GOODS_STOCK_KEY("GoodsKey:stock",-1,"商品库存订单key");

    private String prefixKey;

    private Integer expire;

    private String remarks;

    public String getPrefixKey() {
        return prefixKey;
    }

    public Integer getExpire() {
        return expire;
    }

    public String getRemarks() {
        return remarks;
    }

    RedisKeyEnum(String prefixKey, Integer expire, String remarks) {
        this.prefixKey = prefixKey;
        this.expire = expire;
        this.remarks = remarks;
    }


    public static RedisKeyEnum getByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        for (RedisKeyEnum type : RedisKeyEnum.values()) {
            if (StringUtils.equalsIgnoreCase(type.getPrefixKey(), key)) {
                return type;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(RedisKeyEnum.GOODS_STOCK_KEY.remarks);
        System.out.println(RedisKeyEnum.getByKey("GoodsKey:stock").getRemarks());
    }
}
