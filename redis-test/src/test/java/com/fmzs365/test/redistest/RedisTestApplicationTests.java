package com.fmzs365.test.redistest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTestApplicationTests {

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void contextLoads() {
        jedisCluster.set("cluster122", "122");
        jedisCluster.expire("cluster122", 5);
    }

}
