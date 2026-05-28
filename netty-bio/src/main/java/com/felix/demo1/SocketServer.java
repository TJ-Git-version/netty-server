package com.felix.demo1;

import java.io.IOException;
import java.io.InputStream;
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
        Socket socket = serverSocket.accept();
        System.out.println("客户端连接成功...");

        // 获取输入流，读取客户端发送的数据
        InputStream is = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int len = is.read(buffer);
        System.out.println("收到客户端消息：" + new String(buffer, 0, len));

        serverSocket.close();
    }
}
