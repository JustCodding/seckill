
package com.jbc.seckill.redis;

public class MiaoshaKey extends BasePrefix {
    private MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0,"ms");

}
