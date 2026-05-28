package com.felix.demo1_v5;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 8011);
        System.out.println("连接成功...");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入消息(输入exit退出)：");
            String message = scanner.nextLine();
            if ("exit".equalsIgnoreCase(message)) {
                break;
            }
            OutputStream os = socket.getOutputStream();
            os.write(message.getBytes());
            os.flush();
        }
        scanner.close();
        socket.close();
    }
}
