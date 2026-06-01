package com.felix.netty.chatserver.hander;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServerHandler extends ChannelInboundHandlerAdapter {

    // 在线用户列表
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("[系统] 用户上线：{}", channel.remoteAddress());
        channel.writeAndFlush("[系统] 欢迎光临聊天室\n");
        channel.writeAndFlush(String.format("[系统] 用户上线: %s\n", channel.remoteAddress()));
        channels.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = null;
        try {
            channel = ctx.channel();
            log.info("[系统] 用户下线：{}", channel.remoteAddress());
            channel.writeAndFlush(String.format("[系统] 用户离线: %s\n", channel.remoteAddress()));
        } finally {
            if (channel != null) {
                channels.remove(channel);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel selfChannel = ctx.channel();
        String message = (String) msg;
        // 广播给所有用户
        for (Channel channel : channels) {
            if (channel == selfChannel) {
                continue;
            }
            channel.writeAndFlush( String.format("[用户] %s：%s\n", selfChannel.remoteAddress(), message));
        }
    }
}
