package com.felix.nioserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8801));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> key = selectionKeys.iterator();
            while (key.hasNext()) {
                SelectionKey selectionKey = key.next();
                if (selectionKey.isConnectable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    if (channel.finishConnect()) { // 完成连接
                        channel.register(selector, SelectionKey.OP_READ);
                        channel.write(ByteBuffer.wrap("Hello, Server!".getBytes()));
                    }
                }
                if (selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = channel.read(buffer);
                    if (len > 0) {
                        buffer.flip();
                        System.out.println("Client received: " + new String(buffer.array(), 0 , len));
                    }
                }
                key.remove();
            }
        }
    }

}
