
package com.jbc.seckill.redis;

public class GoodsKey extends BasePrefix {
    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static GoodsKey goodsList = new GoodsKey(60,"gl");
    public static GoodsKey goodsDetail = new GoodsKey(60,"gd");
    public static GoodsKey miaoshaogoodstock = new GoodsKey(0,"gs");
}
