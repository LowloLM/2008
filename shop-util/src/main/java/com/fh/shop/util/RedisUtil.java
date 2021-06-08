package com.fh.shop.util;

import redis.clients.jedis.Jedis;
public class RedisUtil {

    public static void set(String key,String value){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            connection.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static void hset(String key, String  field,String value){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            connection.hset(key,field,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static String get(String key){
            Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            return connection.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static String hget(String key,String  field){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            return connection.hget(key,field);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }


    public static Long delete(String key){
        Jedis connection = null;
        try {
                connection = RedisPool.getConnection();
          return connection.del(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static void setEx(String key,String value,int seconds){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            connection.setex(key,seconds,value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static boolean exist(String key){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
            return connection.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

    public static void expire(String key,int seconds){
        Jedis connection = null;
        try {
            connection = RedisPool.getConnection();
             connection.expire(key , seconds);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(connection != null){
                connection.close();
            }
        }
    }

}
