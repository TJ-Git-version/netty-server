package com.felix.netty.wsserver.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WsServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textFrame) throws Exception {
        System.out.println("Received message: " + textFrame.text());
        ctx.channel().writeAndFlush(new TextWebSocketFrame("Echo: " + textFrame.text()));
    }

}
