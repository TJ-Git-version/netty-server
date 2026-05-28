package com.felix.demo1_v4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SocketClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 8011);
        System.out.println("连接成功...");

        OutputStream os = socket.getOutputStream();
        os.write("hello bio".getBytes());
        System.out.println("消息发送成功");
        TimeUnit.SECONDS.sleep(20);
    }
}
