package com.logic;

import com.utils.JedisUtils;

import java.util.Objects;

public class OnlineStatusLogic {
    private static final int expireTime = 100;  // unit: seconds

    public static <T> void userLogin(Class<T> clazz, String username) {
        JedisUtils.jedis.hset(clazz.getName() + username, "online", "1");
        JedisUtils.jedis.expire(clazz.getName() + username, expireTime);
    }

    public static <T> void userLogout(Class<T> clazz, String username) {
        JedisUtils.jedis.hset(clazz.getName() + username, "online", "0");
    }

    public static <T> boolean isOnline(Class<T> clazz, String username) {
        return Objects.equals(JedisUtils.jedis.hget(clazz.getName() + username, "online"), "1");
    }
}
