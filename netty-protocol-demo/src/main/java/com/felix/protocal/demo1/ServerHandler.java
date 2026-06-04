package com.felix.protocal.demo1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println("Received message: " + message.getContent());
        ctx.channel().writeAndFlush(new Message("服务端收到：" + message.getContent()));
    }
}
