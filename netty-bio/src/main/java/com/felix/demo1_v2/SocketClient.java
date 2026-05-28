package com.felix.demo1_v2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8011);
        System.out.println("连接成功...");

        OutputStream os = socket.getOutputStream();
        for (int i = 1; i < 4; i++) {
            os.write(("hello" + i).getBytes(StandardCharsets.UTF_8));
        }
        os.flush();
        socket.shutdownOutput(); // 告诉服务端：我不会再发数据了

        System.out.println("消息发送成功");

        InputStream is = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int len = is.read(buffer);
        System.out.println("收到服务端消息：" + new String(buffer, 0, len));
        socket.close();
    }
}
