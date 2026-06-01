package com.felix.netty.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out) throws Exception {
        // 如果可读字节小于4，则无法读取一个int，所以返回，先判断数据够不够
        int readabled = in.readableBytes();
        if (readabled < 4) {
            return;
        }
        // 标记当前读指针位置
        in.markReaderIndex();
        // 读取消息长度
        int len = in.readInt();
        // 如果可读字节小于len，数据可能没接收完整
        if (in.readableBytes() < len) {
            // 重置读指针位置
            in.resetReaderIndex();
            return;
        }
        // 读取消息体
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        String msg = new String(bytes, 0, len, StandardCharsets.UTF_8);
        //  传递给下一个Handler
        out.add(msg);
    }
}
