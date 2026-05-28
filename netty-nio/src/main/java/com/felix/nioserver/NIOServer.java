package com.felix.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建Selector
        Selector selector = Selector.open();

        // 创建一个服务器端SocketChannel，绑定端口8801
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 设置为非堵塞
        serverSocketChannel.configureBlocking(false);
        // 绑定端口8801
        serverSocketChannel.bind(new InetSocketAddress(8801));


        // 将ServerSocketChannel注册到Selector，监听连接事件，当有客户端连接时，会触发SelectionKey.OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动，等待客户端连接...");

        while (true) {
            // 监听连接事件，一直轮询，阻塞等待事件发生
            selector.select();

            // 获取活跃 SelectionKey 事件
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            // 处理 SelectionKey 事件
            while (selectionKeys.hasNext()) {
                SelectionKey selectionKey = selectionKeys.next();
                // 如果是第一次连接
                if (selectionKey.isAcceptable()) {
                    // 获取活跃的SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置为非堵塞
                    socketChannel.configureBlocking(false);
                    // 注册到Selector，监听读事件，当有数据可读时，会触发SelectionKey.OP_READ
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("新客户端连接：" + socketChannel.getRemoteAddress());
                }
                // 可读事件
                if (selectionKey.isReadable()) {
                    // 获取客户端SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 读数据
                    int len = 0;
                    try {
                        len = socketChannel.read(buffer);
                    } catch (IOException e) {
                        System.out.println(socketChannel.getRemoteAddress() + " 下线了...");
                        selectionKey.cancel();
                        socketChannel.close();
                        continue;
                    }
                    if (len > 0) {
                        // 切换为写数据
//                        buffer.flip();
                        System.out.println("收到数据：" + new String(buffer.array(), 0, len));
                        socketChannel.register(selector, SelectionKey.OP_WRITE);
                    }

                }
                // 可写事件
                if (selectionKey.isWritable()) {
                    // 获取客户端SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.write(ByteBuffer.wrap("Hello, Client!".getBytes()));
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("已向客户端发送数据：" + "Hello, Client!");
                }
                // 移除当前的SelectionKey，避免重复处理
                selectionKeys.remove();
            }
        }

    }

}
