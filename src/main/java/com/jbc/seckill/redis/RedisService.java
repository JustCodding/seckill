package com.jbc.seckill.redis;

import com.alibaba.fastjson.JSON;
import com.jbc.seckill.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    //获取当个对象
    public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis = jedisPool.getResource();
        String objStr = null;
        try {
            String realKey = prefix.getPrefix()+key;
            objStr = jedis.get(realKey);
            //return JSON.parseObject(objStr,clazz);
            return stringToBean(objStr,clazz);
        }finally {
            returnToPool(jedis);
        }

    }

    //设置对象
    public <T> boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis = jedisPool.getResource();
        try {
            String realKey = prefix.getPrefix()+key;
            String str = beanToString(value);
            if(str==null||str.length()<=0){
                return false;
            }
            int expireSeconds = prefix.expireSeconds();
            if(expireSeconds<=0){//不过期
                jedis.set(realKey,str) ;
            }else {
                jedis.setex(realKey,prefix.expireSeconds(),str);
            }
           return true;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    public <T> T stringToBean(String str,Class<T> clazz){
        if(str==null||str.length()<=0||clazz==null){
            return null;
        }
        if(clazz == int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }

    }

    private <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }



}
