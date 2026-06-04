package com.felix.protocal.demo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            ChannelFuture future = bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel serverSocketChannel) throws Exception {
                            // 给服务端监听通道配置
                        }
                    })
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 给客户端连接配置
                            socketChannel.pipeline()
                                    .addLast(new MessageDecoder())
//                                    .addLast(new ChannelInboundHandlerAdapter() {
//                                        @Override
//                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                            ByteBuf buf = (ByteBuf) msg;
//                                            byte[] bytes = new byte[buf.readableBytes()];
//                                            buf.readBytes(bytes);
//                                            System.out.println(Arrays.toString(bytes));
//                                        }
//                                    })
                                    .addLast(new MessageEncoder())
                                    .addLast(new ServerHandler());
                        }
                    })
                    .bind(7788)
                    .sync();
            System.out.println("Server is listening on port 7788");
            future.channel().closeFuture().sync();
        } catch (InterruptedException ignored) {
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
