package com.jbc.seckill.redis;

public class MiaoshaUserKey extends BasePrefix {
    public static final int expireSeconds = 3600*24*2;//2天
    private MiaoshaUserKey(String prefix) {
        super(prefix);
    }

    public MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(expireSeconds,"tk");
}
