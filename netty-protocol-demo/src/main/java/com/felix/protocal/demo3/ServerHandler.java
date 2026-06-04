package com.felix.protocal.demo3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.felix.protocal.demo3.message.ChatMessage;
import com.felix.protocal.demo3.message.HeartbeatMessage;
import com.felix.protocal.demo3.message.LoginMessage;
import com.felix.protocal.demo3.protocol.ProtocolMessage;
import com.felix.protocal.demo3.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ServerHandler extends SimpleChannelInboundHandler<ProtocolMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtocolMessage protocolMessage) throws Exception {
        if (protocolMessage == null) {
            ctx.close();
            return;
        }
        if (!Objects.equals(protocolMessage.getMagic(), (short) 0xCAFE)) {
            System.out.println("协议标识错误");
            ctx.close();
            return;
        }

        String content = new String(protocolMessage.getBody(), StandardCharsets.UTF_8);
        if (protocolMessage.getVersion() == 1) {
            System.out.println("协议版本为1");
            switch (protocolMessage.getType()) {
                case MessageType.LOGIN -> {
                    LoginMessage loginMessage = parseObj(content, (Class<LoginMessage>) MessageType.MESSAGE_TYPE_MAP.get(protocolMessage.getType()));
                    System.out.println("账号: " + loginMessage.getUsername());
                    System.out.println("密码: " + loginMessage.getPassword());
                    System.out.println("--------------------------------------------------");
                }
                case MessageType.CHAT -> {
                    ChatMessage chatMessage = parseObj(content, (Class<ChatMessage>) MessageType.MESSAGE_TYPE_MAP.get(protocolMessage.getType()));
                    System.out.println("发送者: " + chatMessage.getFrom());
                    System.out.println("接收者: " + chatMessage.getTo());
                    System.out.println("内容: " + chatMessage.getMessage());
                    System.out.println("--------------------------------------------------");
                }
                case MessageType.HEARTBEAT -> {
                    HeartbeatMessage heartbeatMessage = parseObj(content, (Class<HeartbeatMessage>) MessageType.MESSAGE_TYPE_MAP.get(protocolMessage.getType()));
                    System.out.println("信号: " + heartbeatMessage.getSignal());
                }
                case MessageType.LOGOUT -> {
                    System.out.println("退出登录消息: " + content);
                    ctx.close();
                }
                case MessageType.PRIVATE_CHAT -> {
                    System.out.println("私聊消息: " + content);
                }
            }
        } else {
            System.out.println("协议版本错误");
            ctx.close();
            return;
        }
    }

    private <T> T parseObj(String body, Class<T> clazz) {
        try {
            return JsonUtil.fromJson(body, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
