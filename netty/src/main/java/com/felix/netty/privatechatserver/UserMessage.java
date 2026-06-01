package com.felix.netty.privatechatserver;

import lombok.Data;

@Data
public class UserMessage {
    private String username;
    private String message;
}