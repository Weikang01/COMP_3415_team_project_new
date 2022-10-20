package com.utils;
import redis.clients.jedis.Jedis;

public class JedisUtils {
    public final static String SITE = "127.0.0.1";
    public final static int PORT = 6379;
    public final static Jedis jedis = new Jedis(SITE, PORT);
}


