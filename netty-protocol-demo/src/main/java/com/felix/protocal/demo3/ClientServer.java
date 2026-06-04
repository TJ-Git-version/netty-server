package com.felix.protocal.demo3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.felix.protocal.demo3.message.ChatMessage;
import com.felix.protocal.demo3.message.HeartbeatMessage;
import com.felix.protocal.demo3.message.LoginMessage;
import com.felix.protocal.demo3.protocol.ProtocolDecoder;
import com.felix.protocal.demo3.protocol.ProtocolEncoder;
import com.felix.protocal.demo3.protocol.ProtocolMessage;
import com.felix.protocal.demo3.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

public class ClientServer {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ProtocolDecoder())
                                .addLast(new ProtocolEncoder());
                    }
                });
        try {
            Channel channel = bootstrap.connect("localhost", 7788).sync().channel();
            System.out.println("Client connected!");
            channel.writeAndFlush(getProtocolMessage(MessageType.CHAT, new ChatMessage("felix", "king", "hello")));
            channel.writeAndFlush(getProtocolMessage(MessageType.LOGIN, new LoginMessage("felix", "123456")));
            channel.writeAndFlush(getProtocolMessage(MessageType.HEARTBEAT, new HeartbeatMessage()));
            channel.closeFuture().sync();
            System.out.println("Client disconnected!");
        } catch (Exception ignored) {
        } finally {
            group.shutdownGracefully();
        }
    }

    private static ProtocolMessage getProtocolMessage(byte type, Object message) throws JsonProcessingException {
        ProtocolMessage protocolMessage = new ProtocolMessage();
        protocolMessage.setMagic((short) 0xCAFE);
        protocolMessage.setVersion((byte) 1);
        protocolMessage.setType(type);
        String text = JsonUtil.toJson(message);
        protocolMessage.setLength(text.getBytes().length);
        protocolMessage.setBody(text.getBytes(StandardCharsets.UTF_8));
        return protocolMessage;
    }

}
