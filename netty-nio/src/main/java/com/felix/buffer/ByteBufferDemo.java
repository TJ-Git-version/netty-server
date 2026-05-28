package com.felix.buffer;

import java.nio.ByteBuffer;

public class ByteBufferDemo {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        System.out.println("--------------------写数据----------------------");

        buffer.put((byte) 1);
        buffer.put((byte) 124);
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());

        System.out.println("-------------------写数据 -> 读数据 flip----------------------");
        buffer.flip();
        byte b1 = buffer.get();
        System.out.println(b1);
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
//        System.out.println("-----------------------");
//        byte b2 = buffer.get();
//        System.out.println(b2);
//        System.out.println(buffer.position());
//        System.out.println(buffer.limit());
//        System.out.println(buffer.capacity());

        System.out.println("-------------------读数据 -> 写数据 clear----------------------");
        buffer.compact();
        System.out.println(buffer.get());
        System.out.println(buffer.position());
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
    }
}
