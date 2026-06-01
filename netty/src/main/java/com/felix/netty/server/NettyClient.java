package com.felix.netty.server;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class NettyClient {

    public static void main(String[] args)
            throws Exception {

        Socket socket =
                new Socket("127.0.0.1", 8805);
        OutputStream out =
                socket.getOutputStream();

        String msg = "hello";

        byte[] data =
                msg.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer =
                ByteBuffer.allocate(4 + data.length);

        buffer.putInt(data.length);
        buffer.put(data);

        byte[] packet = buffer.array();

        // 故意拆开发送
        out.write(packet, 0, 3);
        out.flush();

        Thread.sleep(1000);

        out.write(packet, 3,
                packet.length - 3);

        out.flush();

        System.out.println("拆包发送成功");
    }
}