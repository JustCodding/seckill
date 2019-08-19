package com.jbc.seckill.redis;

public interface KeyPrefix {
    int expireSeconds();
    String getPrefix();
}
