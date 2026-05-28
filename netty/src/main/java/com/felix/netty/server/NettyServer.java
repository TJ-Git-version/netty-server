package com.felix.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        // boss线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 服务启动器
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 使用NIO
                    .childHandler(new ChannelInitializer<SocketChannel>() {  // 初始化channel
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 获取 pipeline 流水线
                            ChannelPipeline pipeline = ch.pipeline();
                            // TODO 添加处理器
                            pipeline.addLast(new SimpleHandler());
                        }
                    });
            // 绑定端口
            ChannelFuture future = bootstrap.bind(8805).sync();
            System.out.println("Netty服务器启动成功");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
