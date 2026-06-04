package com.felix.protocal.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 消息编码器：message对象 -> 消息缓冲区
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

    /**
     * 编码器
     * @param ctx   通道上下文
     * @param message 消息
     * @param out   消息缓冲区-出
     * @throws Exception    异常
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        byte[] bytes = message.getContent().getBytes(StandardCharsets.UTF_8);
        // 写长度
        out.writeShort(bytes.length);
        // 写内容
//        out.writeBytes(bytes);

        // 模拟半包发送
        out.writeBytes(Arrays.copyOfRange(bytes, 0, 2));
        // 睡眠两秒再发送剩余内容
        TimeUnit.SECONDS.sleep(1);
        out.writeBytes(Arrays.copyOfRange(bytes, 2, bytes.length));
    }
}
