package com.felix.protocal.demo3;

import com.felix.protocal.demo3.message.ChatMessage;
import com.felix.protocal.demo3.message.HeartbeatMessage;
import com.felix.protocal.demo3.message.LoginMessage;

import java.util.Map;

/**
 * 消息类型
 */
public interface MessageType {

    // 登录消息
    byte LOGIN = 1;
    // 聊天消息
    byte CHAT = 2;
    // 心跳消息
    byte HEARTBEAT = 3;
    // 退出登录
    byte LOGOUT = 4;
    // 私聊消息
    byte PRIVATE_CHAT = 5;

    Map<Byte, Class<?>> MESSAGE_TYPE_MAP = Map.of(
            LOGIN, LoginMessage.class,
            CHAT, ChatMessage.class,
            HEARTBEAT, HeartbeatMessage.class
    );
}
