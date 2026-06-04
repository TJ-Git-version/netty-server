package com.felix.protocal.demo2;

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

}
