package com.felix.netty.chatserver;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8899);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        log.info("连接服务器成功");
        new Thread(() -> {
            while (true) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String serverMsg = br.readLine();
                    log.info("服务器返回消息：{}", serverMsg);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        log.info("请输入消息：");
        while (true) {
            String msg = console.readLine();
            pw.println(msg);
        }
    }
}
