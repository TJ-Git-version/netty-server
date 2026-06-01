package com.felix.netty.wcchatserver.domain;

import lombok.Data;

@Data
public class UserMessage {
    private String type;
    private String message;
    private String to;
    private String from;
}

