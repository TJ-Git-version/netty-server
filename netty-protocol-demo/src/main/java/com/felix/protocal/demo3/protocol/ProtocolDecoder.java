package com.felix.protocal.demo3.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器，将ByteBuf解码为ProtocolMessage对象
 */
public class ProtocolDecoder extends ByteToMessageDecoder {

    /**
     * 解码逻辑
     * @param ctx 上下文
     * @param in 入站数据
     * @param outHandler 出站数据
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outHandler) throws Exception {
        // short：2字节，int：4字节，byte：1字节 x 2，总共8字节
        if (in.readableBytes() < 8) {
            // 数据不完整，等待更多数据
            return;
        }
        // 标记当前读指针位置
        in.markReaderIndex();

        // 读取协议唯一标识
        short magic = in.readShort();
        // 读取版本号
        byte version = in.readByte();
        // 读取类型
        byte type = in.readByte();
        // 读取长度
        int length = in.readInt();
        if (in.readableBytes() < length) {
            // 数据不完整，等待更多数据
            in.resetReaderIndex();
            return;
        }
        byte[] body = new byte[length];
        in.readBytes(body);
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setMagic(magic);
        protocolMessage.setVersion(version);
        protocolMessage.setType(type);
        protocolMessage.setLength(length);
        protocolMessage.setBody(body);
        outHandler.add(protocolMessage);
    }
}
