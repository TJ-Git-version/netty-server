package com.felix.netty.privatechatserver;

import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PrivateChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8899);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("连接服务器成功");
        // 从服务器读取数据 —— 只创建一个，全局复用
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            System.out.print("请输入登录用户名：");
            String username = console.readLine();
            pw.println(StrUtil.format("LOGIN:{}", username));
            String serverMessage = serverReader.readLine();
            if ("success".equals(serverMessage)) {
                System.out.println("[系统] 登录成功");
                break;
            } else {
                System.out.println("[系统] 登录失败，用户名已存在");
            }
        }
        Thread thread = new Thread(() -> {
            try {
                String line;
                while ((line = serverReader.readLine()) != null) {
                    // 关键优化：先换行，让新消息从新的一行开始
                    System.out.println();
                    // 打印服务器消息
                    System.out.println(line);
                    // 重新显示输入提示符（不换行），让用户继续输入
//                    System.out.print("[私聊]：");
                    System.out.flush();
                }
            } catch (IOException e) {
                System.out.println("连接已断开");
            }
        });
        thread.setDaemon(true);
        thread.start();
        while (true) {
            System.out.print("[私聊]：");
            pw.println(console.readLine());
        }
    }

    private static String getServerMessage(Socket socket) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
