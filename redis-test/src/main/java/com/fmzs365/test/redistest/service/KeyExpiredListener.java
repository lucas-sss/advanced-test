package com.fmzs365.test.redistest.service;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPubSub;

/**
 * @author liuwei liuwei@flksec.com
 * @date 2018/9/13
 * @since 1.0
 */
public class KeyExpiredListener extends JedisPubSub {

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    /**
     * 设置一个队列，作为缓冲
     *
     * @param pattern
     * @param channel
     * @param message
     */
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        System.out.println("监听事件");
        if (StringUtils.isBlank(message)) {
            System.out.println("message is blank");
            return;
        }

        if (message.indexOf("cluster") > -1) {
            System.out.println("******* 符合要求 message: " + message);
            return;
        }
        System.out.println("———————— 不符合要求 message: " + message);
    }
}
