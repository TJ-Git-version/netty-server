package com.felix.netty.server.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

public class ByteBufDemo {
    public static void main(String[] args) {
        // 测试ByteBuffer
//        ByteBuffer buffer = ByteBuffer.allocate(10);
//        for (int i = 0; i < 11; i++) {
//            buffer.put((byte) i); // BufferOverflowException 当写入数据超过缓冲区容量时会抛出此异常
//        }
        // 测试ByteBuf
//        ByteBuf buffer = Unpooled.buffer(10);
//        for (int i = 0; i < 11; i++) {
//            buffer.writeByte(i);
//        }
//        for (int i = 0; i < 11; i++) {
//            System.out.println(buffer.readByte());
//        }
//        System.out.println("----");
//        System.out.println(buffer.readerIndex());
//        System.out.println(buffer.writerIndex());
//        System.out.println(buffer.capacity());
//        System.out.println(buffer.readableBytes());
//        System.out.println(buffer.readByte()); // 不能读取超过可读字节数，UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf

        // 零拷贝
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 11; i++) {
            buffer.writeByte(i);
        }
        ByteBuf copy = buffer.copy();
        System.out.println(copy == buffer);
        System.out.println(copy.equals(buffer));
        System.out.println(copy.hashCode());
        System.out.println(buffer.hashCode());
        System.out.println("----");
        ByteBuf slice = buffer.slice();
        System.out.println(slice);
        System.out.println(slice.toString());
        System.out.println(buffer == slice);
        System.out.println("buffer：" + buffer.hashCode());
        System.out.println("slice：" + slice.hashCode());
        System.out.println(slice.readByte());
        System.out.println(slice.readerIndex());
        System.out.println(buffer.readerIndex());
//        System.out.println(slice.release());
//        System.out.println(slice.readerIndex());
        System.out.println("------");
        CompositeByteBuf byteBufs = new CompositeByteBuf(ByteBufAllocator.DEFAULT, true, 10, buffer, slice, copy);
//        System.out.println(Arrays.toString(byteBufs.array())); // 只能newComponents一个才能使用
        System.out.println(byteBufs.capacity());
        System.out.println(byteBufs.isDirect());
        System.out.println(byteBufs.numComponents());
        System.out.println(byteBufs.toString());
        System.out.println(byteBufs.readByte());
        System.out.println(byteBufs.readByte());
        byteBufs.release();

    }
}
