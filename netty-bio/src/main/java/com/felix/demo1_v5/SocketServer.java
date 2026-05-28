package com.felix.demo1_v5;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现：客户端给服务端发送消息
 */
public class SocketServer {
    public static void main(String[] args) throws IOException {
        // 创建一个服务器端Socket，监听端口8011
        ServerSocket serverSocket = new ServerSocket(8011);

        System.out.println("服务器启动，等待客户端连接...");
        // 会阻塞等待连接
        while (true) {
            Socket socket = serverSocket.accept();
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println(inetAddress.getHostName() + " " + inetAddress.getHostAddress() + " 客户端连接成功...");
            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = is.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        System.out.println(Thread.currentThread().getName() + " 收到消息：" + new String(buffer, 0, len));
                    }
                } catch (IOException e) {
                    System.out.println(inetAddress.getHostName() + " " + inetAddress.getHostAddress() + " 客户端下线了");
                }
            }).start();
        }
    }
}
