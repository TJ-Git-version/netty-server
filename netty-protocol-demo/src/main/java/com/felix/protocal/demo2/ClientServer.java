package com.felix.protocal.demo2;

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
            ProtocolMessage protocolMessage = new ProtocolMessage();
            protocolMessage.setMagic((short) 0xCAFE);
            protocolMessage.setVersion((byte) 1);
            protocolMessage.setType(MessageType.PRIVATE_CHAT);
//            String text1 = "账号：admin，密码：123456";
//            String text = "你好，吃饭了吗？";
//            String text = "ping";
//            String text = "退出登录";
            String text = "@felix 你好";
            protocolMessage.setLength(text.getBytes().length);
            protocolMessage.setBody(text.getBytes(StandardCharsets.UTF_8));
            channel.writeAndFlush(protocolMessage);
            channel.closeFuture().sync();
            System.out.println("Client disconnected!");
        } catch (InterruptedException ignored) {
        } finally {
            group.shutdownGracefully();
        }
    }

}
