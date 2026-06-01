package com.felix.netty.server;

import com.felix.netty.server.handler.HeartBeatHandler;
import com.felix.netty.server.handler.LogHandler;
import com.felix.netty.server.handler.SimpleHandler;
import com.felix.netty.server.handler.SimpleHandler2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

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
                            /*
                            maxFrameLength：最大帧长度，作用：防止恶意攻击
                            lengthFieldOffset：长度字段偏移量
                            lengthFieldLength：长度字段占几个字节
                            lengthAdjustment：长度调整
                            initialBytesToStrip：是否去掉长度字段
                            failFast：是否快速失败
                             */
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(
//                                    1024,
//                                    0,
//                                    4,
//                                    0,
//                                    4
//                            ));
//                            pipeline.addLast(new MyDecoder());
                            /*
                            readerIdleTime：读空闲时间，单位：秒
                            writerIdleTime：写空闲时间，单位：秒
                            allIdleTime：所有空闲时间，单位：秒
                             */
                            pipeline.addLast(new IdleStateHandler(
                                    90,
                                    0,
                                    0,
                                    TimeUnit.SECONDS
                            ));
                            pipeline.addLast(new HeartBeatHandler());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            // TODO 添加处理器
                            pipeline.addLast(new LogHandler());
                            pipeline.addLast(new SimpleHandler());
                            pipeline.addLast(new SimpleHandler2());
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
