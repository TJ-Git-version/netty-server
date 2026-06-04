package com.felix.protocal.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MessageDecoder())
                                .addLast(new MessageEncoder())
                                .addLast(new ClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost", 7788).sync();
            System.out.println("Client connected!");
            future.channel().writeAndFlush(new Message("hello"));
            future.channel().writeAndFlush(new Message("world"));
            future.channel().writeAndFlush(new Message("java"));
//            for (int i = 0; i < 100; i++) {
//                future.channel().writeAndFlush(
//                        new Message("msg-" + i));
//            }
        } catch (InterruptedException ignored) {
        } finally {
//            group.shutdownGracefully();
        }
    }
}
