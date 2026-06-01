package com.felix.nettyspringboot.netty.domain;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserChannelManager {

    private static final Map<Long, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<>();

    public void bind(Long userId, Channel channel) {
        USER_CHANNEL_MAP.put(userId, channel);
    }

    public void unbind(Long userId) {
        if (!USER_CHANNEL_MAP.containsKey(userId)) {
            return;
        }
        USER_CHANNEL_MAP.remove(userId);
    }

    public Channel getChannel(Long userId) {
        return USER_CHANNEL_MAP.get(userId);
    }

    public boolean containsUser(Long userId) {
        return USER_CHANNEL_MAP.containsKey(userId);
    }
}
