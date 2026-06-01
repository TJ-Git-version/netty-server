package com.felix.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent idleStateEvent) {
            log.info("IdleStateEvent: {}", idleStateEvent.state());
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("客户端长时间未发送数据");
                log.info("关闭时间：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                ctx.close();
            }
        }
    }
}
