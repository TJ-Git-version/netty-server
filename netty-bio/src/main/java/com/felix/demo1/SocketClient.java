package com.felix.demo1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8011);
        System.out.println("连接成功...");
        OutputStream os = socket.getOutputStream();
        os.write("hello netty".getBytes(StandardCharsets.UTF_8));
        System.out.println("消息发送成功");
        socket.close();
    }
}
