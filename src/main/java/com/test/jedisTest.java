package com.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

public class jedisTest {
    @Test
    public void testJedis(){
        Jedis jedis = new Jedis("127.0.0.1", 6379);

//        jedis.set("name", "weikang");
        String name = jedis.get("name");
        System.out.println(name);

        jedis.lpush("list1", "a", "b", "c");
        jedis.rpush("list1", "x");
        List<String> list1 = jedis.lrange("list1", 0, -1);
        jedis.hset("hash1","a1", "a1");

        jedis.close();
    }
}
