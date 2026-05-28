package com.felix.demo1_v3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8011);
        System.out.println("连接成功...");

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(os), true);
        Scanner scanner = new Scanner(System.in);

        InputStream is = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            System.out.print("请输入消息（输入 exit 退出）：");
            String msg = scanner.nextLine();
            if ("exit".equalsIgnoreCase(msg)) {
                writer.println("exit");
                break;
            }
            writer.println(msg);
            String response = reader.readLine();
            System.out.println("服务端回复：" + response);
        }

        socket.close();
        System.out.println("客户端退出");
    }
}
