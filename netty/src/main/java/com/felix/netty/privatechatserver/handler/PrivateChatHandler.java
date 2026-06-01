package com.felix.netty.privatechatserver.handler;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PrivateChatHandler extends ChannelInboundHandlerAdapter {

    // 用户通道映射
    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    // 通道用户映射
    private static final Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("客户端长时间未发送数据");
                log.info("关闭时间：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                closeChannel(ctx.channel());
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        message = message.trim();
        log.info("[系统] 接收到的消息：{}", message);
        if (message.startsWith("LOGIN:")) {
            String username = message.substring(6);
            log.info("[系统] 用户 {} 登录", username);
            if (userChannelMap.containsKey(username)) {
                closeChannel(userChannelMap.get(username));
            }
            userChannelMap.putIfAbsent(username, ctx.channel());
            channelUserMap.putIfAbsent(ctx.channel(), username);
            ctx.channel().writeAndFlush("success\n");
            return;
        }

        if ("list".equalsIgnoreCase(message)) {
            for (String username : userChannelMap.keySet()) {
                if (ctx.channel().equals(userChannelMap.get(username))) {
                    ctx.channel().writeAndFlush(String.format("用户 %s 在线(me)\n", username));
                } else {
                    ctx.channel().writeAndFlush(String.format("用户 %s 在线\n", username));
                }
            }
        }

        if (StrUtil.isBlank(message) || !message.startsWith("@")) {
            return;
        }
        int length = message.indexOf(" ");
        if (length <= 0) {
            return;
        }
        String username = message.substring(1, length);
        Channel targetChannel = userChannelMap.get(username);
        if (targetChannel != null) {
            targetChannel.writeAndFlush(String.format("[%s]：%s\n", username, message.substring(length)));
        } else {
            log.info("[%s]：用户 {} 不在线", username);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("[系统] 异常堆栈：{}", cause.getStackTrace());
        printUserClose(ctx.channel());
        closeChannel(ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        log.info("[系统] 用户上线：{}", channel.remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        printUserClose(ctx.channel());
        closeChannel(ctx.channel());
    }

    private static void printUserClose(Channel channel) {
        String username = channelUserMap.get(channel);
        if (username != null) {
            log.info("[系统] 用户下线：{}", username);
        }
    }

    private static void closeChannel(Channel channel) {
        printUserClose(channel);
        channelUserMap.remove(channel);
        channel.close();
    }
}
