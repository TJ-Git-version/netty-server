package com.felix.protocal.demo3.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 定义协议编码器：将ProtocolMessage对象编码成ByteBuf对象
 */
public class ProtocolEncoder extends MessageToByteEncoder<ProtocolMessage> {

    /**
     * 编码方法：将ProtocolMessage对象编码成ByteBuf对象
     * @param ctx   通道处理上下文
     * @param protocolMessage   协议消息对象
     * @param out   编码后的字节对象
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage protocolMessage, ByteBuf out) throws Exception {
        // 协议唯一标识：2字节
        out.writeShort(protocolMessage.getMagic());
        // 协议版本号：1字节
        out.writeByte(protocolMessage.getVersion());
        // 消息类型：1字节
        out.writeByte(protocolMessage.getType());
        // 消息长度：4字节
        out.writeInt(protocolMessage.getLength());
        // 消息内容：n字节
        out.writeBytes(protocolMessage.getBody());
    }
}
