package com.jbc.seckill.redis;

public class UserKey extends BasePrefix {
    private UserKey(String prefix) {
        super(prefix);
    }

    private static UserKey getById = new UserKey("id");
    private static UserKey getByName = new UserKey("name");
}
