package com.fmzs365.test.redistest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @author liuwei liuwei@flksec.com
 * @date 2018/9/23
 * @since 1.0
 */
@Component
public class TestService {


    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private JedisCluster jedisCluster;

    @PostConstruct
    public void init() {
        Jedis resource = jedisPool.getResource();
        new Thread(() -> {
            jedisCluster.psubscribe(new KeyExpiredListener(), "__key*__:*");
            System.out.println("-----------------------");
            //resource.psubscribe(new KeyExpiredListener(), "__key*__:*");
        }).start();

    }
}
