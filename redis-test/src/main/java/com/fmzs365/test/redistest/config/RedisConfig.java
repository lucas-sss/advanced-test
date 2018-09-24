package com.fmzs365.test.redistest.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * redis 配置bean
 *
 * @author liuwei liuwei@flksec.com
 * @date 2018/9/23
 * @since 1.0
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisProperties properties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

    }

    @Bean
    public JedisPool getJedisPool() {
        JedisPool pool = new JedisPool(poolConfig(properties), properties.getHost(), properties.getPort(), 100);
        return pool;
    }

    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        List<String> nodes = properties.getCluster().getNodes();
        for (String node : nodes) {
            String[] s = node.split(":");
            hostAndPorts.add(new HostAndPort(s[0], Integer.valueOf(s[1])));
        }
        return new JedisCluster(hostAndPorts, poolConfig(properties));
    }

    public JedisPoolConfig poolConfig(RedisProperties properties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        return config;
    }
}
