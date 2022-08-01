package com.actionmonitor.model;

public class WebSocketResponseMessage {
    private String content;

    public WebSocketResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
