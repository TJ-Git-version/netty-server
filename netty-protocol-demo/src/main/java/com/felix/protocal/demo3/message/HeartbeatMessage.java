package com.felix.protocal.demo3.message;

public class HeartbeatMessage {

    private String signal = "ping";

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }
}
