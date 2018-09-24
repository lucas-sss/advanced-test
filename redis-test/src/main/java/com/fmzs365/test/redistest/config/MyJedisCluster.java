package com.fmzs365.test.redistest.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.Set;

/**
 * @author liuwei liuwei@flksec.com
 * @date 2018/9/24
 * @since 1.0
 */
public class MyJedisCluster extends JedisCluster {
    public MyJedisCluster(HostAndPort node) {
        super(node);
    }

    public MyJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
        super(nodes, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, poolConfig);
    }

    @Override
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        new JedisClusterCommand<Integer>(connectionHandler, maxAttempts) {
            @Override
            public Integer execute(Jedis connection) {
                connection.psubscribe(jedisPubSub, patterns);
                return 0;
            }
        }.runWithAnyNode();
    }
}
