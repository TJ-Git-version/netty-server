package com.felix.netty.wcchatserver.handler;

import cn.hutool.json.JSONUtil;
import com.felix.netty.wcchatserver.domain.UserMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WsChatServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private static final Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            log.info("IdleStateEvent: {}", idleStateEvent.state());
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("客户端长时间未发送数据");
                log.info("离线时间：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                closeChannel(ctx.channel());
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textFrame) throws Exception {
        String message = textFrame.text();
        log.info("Received message: {}", message);
        UserMessage userMessage = JSONUtil.toBean(message, UserMessage.class);
        if (userMessage == null) {
            log.warn("Failed to parse message: {}", message);
            return;
        }
        /*
        {
            "type": "login",
            "from": "felix"
        }

        {
            "type": "online"
        }

        {
            "type": "users"
        }

        {
            "type": "chat",
            "message":"大家好"
        }

        {
            "type": "privateChat",
            "message":"你好，你最近怎么样？",
            "to": "felix"
        }
         */
        // 心跳包
        if ("heartbeat".equalsIgnoreCase(userMessage.getType())) {
            log.info("Received heartbeat from {}", userMessage.getFrom());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("heartbeat"));
            return;
        }
        // 登录
        if ("login".equalsIgnoreCase(userMessage.getType())) {
            log.info("User {} logged in", userMessage.getFrom());
            userChannelMap.put(userMessage.getFrom(), ctx.channel());
            channelUserMap.put(ctx.channel(), userMessage.getFrom());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("登录成功"));
            return;
        }
        // 当前在线人数
        if ("online".equalsIgnoreCase(userMessage.getType())) {
            log.info("There are {} users online", userChannelMap.size());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("当前在线人数: " + userChannelMap.size()));
            return;
        }
        // 在线人员
        if ("users".equalsIgnoreCase(userMessage.getType())) {
            log.info("There are {} users online", userChannelMap.size());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("当前在线人员: " + userChannelMap.keySet()));
            return;
        }
        // 群聊
        if ("chat".equalsIgnoreCase(userMessage.getType())) {
            log.info("User {} sent a message: {}", channelUserMap.get(ctx.channel()), userMessage.getFrom());
            for (Channel channel : userChannelMap.values()) {
                if (channel != ctx.channel() && channel.isActive()) {
                    channel.writeAndFlush(new TextWebSocketFrame("[群聊]" + channelUserMap.get(ctx.channel()) + ": " + userMessage.getMessage()));
                }
            }
            return;
        }
        // 私聊
        if ("privateChat".equalsIgnoreCase(userMessage.getType())) {
            log.info("User {} sent a private message to {}", channelUserMap.get(ctx.channel()), userMessage.getTo());
            Channel targetChannel = userChannelMap.get(userMessage.getTo());
            if (targetChannel == null) {
                log.warn("User {} is not online", userMessage.getTo());
                ctx.channel().writeAndFlush(new TextWebSocketFrame("用户[" + userMessage.getTo() + "]不在线"));
            } else if (targetChannel.isActive()) {
                targetChannel.writeAndFlush(new TextWebSocketFrame("[私聊]" + channelUserMap.get(ctx.channel()) + ": " + userMessage.getMessage()));
            }
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String removeUser = closeChannel(ctx.channel());
        log.info("User {} logged out", removeUser);
    }

    private String closeChannel(Channel channel) {
        String username = channelUserMap.get(channel);
        if (username != null) {
            userChannelMap.remove(username);
        }
        channelUserMap.remove(channel);
        channel.close();
        return username;
    }

}
