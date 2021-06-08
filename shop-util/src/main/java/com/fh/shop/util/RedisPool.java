package com.fh.shop.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private RedisPool(){

    }

    private static JedisPool jedisPool;
    private static void initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //连接池中最大的连接数量
        jedisPoolConfig.setMaxTotal(1000);
        //空闲时候连接池中的连接数量
        jedisPoolConfig.setMaxIdle(200);
        jedisPoolConfig.setMinIdle(200);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPool = new JedisPool(jedisPoolConfig, "192.168.18.138", 6379);
    }

    //静态块，它里面的代码只会执行一次
    static {
        initPool();
    }

    public static Jedis getConnection(){
        return jedisPool.getResource();
    }

}
