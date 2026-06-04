package com.felix.protocal.demo2;

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
                    System.out.println("登录消息: " + content);
                }
                case MessageType.CHAT -> {
                    System.out.println("聊天消息: " + content);
                }
                case MessageType.HEARTBEAT -> {
                    System.out.println("心跳消息: " + content);
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
}
