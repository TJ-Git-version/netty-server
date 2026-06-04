package com.felix.protocal.demo1;


import java.nio.charset.StandardCharsets;

public class Message {

    private String content;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public static Message parseFrom(byte[] bytes) {
        return new Message(new String(bytes, StandardCharsets.UTF_8));
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
