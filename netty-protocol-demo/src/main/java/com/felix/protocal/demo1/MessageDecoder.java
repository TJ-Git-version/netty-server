package com.felix.protocal.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 消息解码器：消息缓冲区 -> message对象
 */

public class MessageDecoder extends ByteToMessageDecoder {

    /**
     * 解码器
     * @param ctx   通道上下文
     * @param in 消息缓冲区-入
     * @param out  解码后的消息列表
     * @throws Exception    异常
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 长度字段2字节，short两个字节
        if (in.readableBytes() < 2) {
            // 数据不完整，返回，半包
            return;
        }
        // 标记当前读指针位置，配合resetReaderIndex使用，用于半包处理，将读索引恢复到最近一次标记的位置
        in.markReaderIndex();
        short length = in.readShort();
        if (in.readableBytes() < length) {
            // 数据没收完整，返回，半包
            in.resetReaderIndex();
            return;
        }
        // 创建应该bytes容器
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        // 将byte[]转换为message对象
        Message message = Message.parseFrom(bytes);
        // 添加到消息列表中，传递给下一个Handler
        out.add(message);
    }
}
