package com.felix.protocal.demo3.protocol;

/**
 * 定义协议消息
 */
public class ProtocolMessage {

    /**
     * 协议唯一标识：2字节
     */
    private short magic;

    /**
     * 协议版本号：1字节
     */
    private byte version;

    /**
     * 消息类型：1字节
     */
    private byte type;

    /**
     * 消息长度：4字节
     */
    private int length;

    /**
     * 消息内容：n字节
     */
    private byte[] body;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
